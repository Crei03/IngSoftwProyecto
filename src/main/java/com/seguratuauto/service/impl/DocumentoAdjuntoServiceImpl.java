package com.seguratuauto.service.impl;

import com.seguratuauto.config.FileStorageConfig;
import com.seguratuauto.dao.DocumentoAdjuntoRepository;
import com.seguratuauto.model.DocumentoAdjunto;
import com.seguratuauto.model.EstadoDocumento;
import com.seguratuauto.service.DocumentoAdjuntoService;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Implementación del servicio de documentos adjuntos
 */
@Service
@Transactional
public class DocumentoAdjuntoServiceImpl implements DocumentoAdjuntoService {
    
    private final DocumentoAdjuntoRepository documentoRepository;
    private final FileStorageConfig fileConfig;
    private final Tika tika;
    
    @Autowired
    public DocumentoAdjuntoServiceImpl(DocumentoAdjuntoRepository documentoRepository, 
                                    FileStorageConfig fileConfig) {
        this.documentoRepository = documentoRepository;
        this.fileConfig = fileConfig;
        this.tika = new Tika();
    }
    
    @Override
    public DocumentoAdjunto subirDocumento(Long reclamacionId, MultipartFile archivo, String descripcion) throws Exception {
        // Validaciones
        validarArchivo(archivo);
        
        if (!puedeSubirMasDocumentos(reclamacionId)) {
            throw new RuntimeException("Se ha alcanzado el límite máximo de documentos para esta reclamación");
        }
        
        // Generar nombre único para el archivo
        String nombreUnico = generarNombreUnico(archivo.getOriginalFilename());
        
        // Detectar tipo MIME real del archivo
        String tipoContenido = detectarTipoContenido(archivo);
        
        // Guardar archivo en el sistema de archivos
        Path rutaCompleta = guardarArchivo(archivo, nombreUnico);
        
        // Crear entidad DocumentoAdjunto
        DocumentoAdjunto documento = new DocumentoAdjunto(
            reclamacionId,
            archivo.getOriginalFilename(),
            nombreUnico,
            tipoContenido,
            archivo.getSize(),
            rutaCompleta.toString()
        );
        
        if (descripcion != null && !descripcion.trim().isEmpty()) {
            documento.setDescripcion(descripcion.trim());
        }
        
        return documentoRepository.save(documento);
    }
    
    @Override
    public List<DocumentoAdjunto> subirMultiplesDocumentos(Long reclamacionId, MultipartFile[] archivos, String descripcion) throws Exception {
        List<DocumentoAdjunto> documentosSubidos = new ArrayList<>();
        List<String> errores = new ArrayList<>();
        
        if (archivos.length == 0) {
            throw new RuntimeException("No se han proporcionado archivos para subir");
        }
        
        // Verificar que no exceda el límite total
        int documentosActuales = contarDocumentosActivos(reclamacionId);
        if (documentosActuales + archivos.length > fileConfig.getMaxFilesPerReclamacion()) {
            throw new RuntimeException(String.format(
                "No se pueden subir %d archivos. Límite máximo: %d. Actuales: %d", 
                archivos.length, fileConfig.getMaxFilesPerReclamacion(), documentosActuales));
        }
        
        for (int i = 0; i < archivos.length; i++) {
            MultipartFile archivo = archivos[i];
            try {
                DocumentoAdjunto documento = subirDocumento(reclamacionId, archivo, descripcion);
                documentosSubidos.add(documento);
            } catch (Exception e) {
                errores.add(String.format("Error en archivo %d (%s): %s", 
                    i + 1, archivo.getOriginalFilename(), e.getMessage()));
            }
        }
        
        if (!errores.isEmpty() && documentosSubidos.isEmpty()) {
            throw new RuntimeException("Error al subir archivos: " + String.join("; ", errores));
        }
        
        return documentosSubidos;
    }
    
    @Override
    @Transactional(readOnly = true)
    public Resource descargarDocumento(Long documentoId) throws Exception {
        DocumentoAdjunto documento = obtenerDocumentoPorId(documentoId);
        
        if (documento.getEstado() == EstadoDocumento.ELIMINADO) {
            throw new RuntimeException("El documento ha sido eliminado");
        }
        
        Path rutaArchivo = Path.of(documento.getRutaArchivo());
        
        if (!Files.exists(rutaArchivo)) {
            throw new RuntimeException("El archivo físico no existe en el servidor");
        }
        
        return new UrlResource(rutaArchivo.toUri());
    }
    
    @Override
    @Transactional(readOnly = true)
    public DocumentoAdjunto obtenerDocumentoPorId(Long documentoId) throws Exception {
        return documentoRepository.findById(documentoId)
            .orElseThrow(() -> new RuntimeException("Documento no encontrado con ID: " + documentoId));
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<DocumentoAdjunto> obtenerDocumentosPorReclamacion(Long reclamacionId) {
        return documentoRepository.findByReclamacionId(reclamacionId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<DocumentoAdjunto> obtenerDocumentosActivosPorReclamacion(Long reclamacionId) {
        return documentoRepository.findByReclamacionIdAndEstado(reclamacionId, EstadoDocumento.ACTIVO);
    }
    
    @Override
    public void eliminarDocumento(Long documentoId) throws Exception {
        DocumentoAdjunto documento = obtenerDocumentoPorId(documentoId);
        documento.setEstado(EstadoDocumento.ELIMINADO);
        documentoRepository.save(documento);
    }
    
    @Override
    public void eliminarDocumentoFisicamente(Long documentoId) throws Exception {
        DocumentoAdjunto documento = obtenerDocumentoPorId(documentoId);
        
        // Eliminar archivo físico
        Path rutaArchivo = Path.of(documento.getRutaArchivo());
        if (Files.exists(rutaArchivo)) {
            Files.delete(rutaArchivo);
        }
        
        // Eliminar registro de base de datos
        documentoRepository.delete(documento);
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean puedeSubirMasDocumentos(Long reclamacionId) {
        int documentosActivos = contarDocumentosActivos(reclamacionId);
        return documentosActivos < fileConfig.getMaxFilesPerReclamacion();
    }
    
    @Override
    @Transactional(readOnly = true)
    public int contarDocumentosActivos(Long reclamacionId) {
        return documentoRepository.countByReclamacionIdAndEstado(reclamacionId, EstadoDocumento.ACTIVO);
    }
    
    @Override
    @Transactional(readOnly = true)
    public long obtenerTamañoTotalPorReclamacion(Long reclamacionId) {
        return documentoRepository.getTamañoTotalByReclamacionIdAndEstado(reclamacionId, EstadoDocumento.ACTIVO);
    }
    
    @Override
    public void validarArchivo(MultipartFile archivo) throws Exception {
        if (archivo == null || archivo.isEmpty()) {
            throw new RuntimeException("El archivo no puede estar vacío");
        }
        
        // Validar tamaño
        if (archivo.getSize() > fileConfig.getMaxFileSize()) {
            throw new RuntimeException(String.format(
                "El archivo es demasiado grande. Tamaño máximo permitido: %s", 
                fileConfig.getMaxFileSizeFormatted()));
        }
        
        // Validar nombre del archivo
        String nombreOriginal = archivo.getOriginalFilename();
        if (nombreOriginal == null || nombreOriginal.trim().isEmpty()) {
            throw new RuntimeException("El nombre del archivo no puede estar vacío");
        }
        
        // Validar extensión
        String extension = obtenerExtension(nombreOriginal);
        if (!fileConfig.isExtensionAllowed(extension)) {
            throw new RuntimeException(String.format(
                "Tipo de archivo no permitido: %s. Tipos permitidos: %s", 
                extension, fileConfig.getAllowedExtensions()));
        }
        
        // Validar tipo MIME
        String tipoContenido = detectarTipoContenido(archivo);
        if (!fileConfig.isMimeTypeAllowed(tipoContenido)) {
            throw new RuntimeException(String.format(
                "Tipo de contenido no permitido: %s", tipoContenido));
        }
    }
    
    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> obtenerEstadisticas() {
        Object[] resultado = documentoRepository.getEstadisticasDocumentos();
        
        Map<String, Object> estadisticas = new HashMap<>();
        estadisticas.put("totalDocumentos", resultado[0]);
        estadisticas.put("documentosActivos", resultado[1]);
        estadisticas.put("documentosEliminados", resultado[2]);
        estadisticas.put("tamañoTotalBytes", resultado[3]);
        
        // Calcular tamaño formateado
        Long tamañoTotal = (Long) resultado[3];
        estadisticas.put("tamañoTotalFormatted", formatearTamaño(tamañoTotal));
        
        return estadisticas;
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<DocumentoAdjunto> buscarDocumentos(String nombreOriginal, String tipoContenido, EstadoDocumento estado) {
        // Implementación básica - puede mejorarse con Criteria API para consultas más complejas
        List<DocumentoAdjunto> todos = documentoRepository.findAll();
        
        return todos.stream()
            .filter(doc -> nombreOriginal == null || 
                        doc.getNombreOriginal().toLowerCase().contains(nombreOriginal.toLowerCase()))
            .filter(doc -> tipoContenido == null || 
                        doc.getTipoContenido().equals(tipoContenido))
            .filter(doc -> estado == null || 
                        doc.getEstado() == estado)
            .toList();
    }
    
    // Métodos auxiliares privados
    
    private String generarNombreUnico(String nombreOriginal) {
        String extension = obtenerExtension(nombreOriginal);
        String timestamp = String.valueOf(System.currentTimeMillis());
        String uuid = UUID.randomUUID().toString().substring(0, 8);
        return timestamp + "_" + uuid + extension;
    }
    
    private String obtenerExtension(String nombreArchivo) {
        if (nombreArchivo == null) return "";
        int lastDot = nombreArchivo.lastIndexOf('.');
        return lastDot > 0 ? nombreArchivo.substring(lastDot) : "";
    }
    
    private String detectarTipoContenido(MultipartFile archivo) throws IOException {
        // Usar Apache Tika para detectar el tipo real del archivo
        return tika.detect(archivo.getInputStream(), archivo.getOriginalFilename());
    }
    
    private Path guardarArchivo(MultipartFile archivo, String nombreUnico) throws IOException {
        Path targetPath = fileConfig.getUploadPath().resolve(nombreUnico);
        Files.copy(archivo.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);
        return targetPath;
    }
    
    private String formatearTamaño(Long bytes) {
        if (bytes == null || bytes == 0) return "0 B";
        
        String[] unidades = {"B", "KB", "MB", "GB"};
        int unidadIndex = 0;
        double tamaño = bytes.doubleValue();
        
        while (tamaño >= 1024 && unidadIndex < unidades.length - 1) {
            tamaño /= 1024;
            unidadIndex++;
        }
        
        return String.format("%.2f %s", tamaño, unidades[unidadIndex]);
    }
}