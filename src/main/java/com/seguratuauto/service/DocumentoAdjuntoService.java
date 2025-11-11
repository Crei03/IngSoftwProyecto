package com.seguratuauto.service;

import com.seguratuauto.model.DocumentoAdjunto;
import com.seguratuauto.model.EstadoDocumento;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * Interfaz para el servicio de documentos adjuntos
 */
public interface DocumentoAdjuntoService {
    
    /**
     * Subir un documento adjunto a una reclamación
     * 
     * @param reclamacionId ID de la reclamación
     * @param archivo Archivo multipart
     * @param descripcion Descripción opcional del documento
     * @return DocumentoAdjunto creado
     * @throws Exception Si hay errores en la validación o almacenamiento
     */
    DocumentoAdjunto subirDocumento(Long reclamacionId, MultipartFile archivo, String descripcion) throws Exception;
    
    /**
     * Subir múltiples documentos a una reclamación
     * 
     * @param reclamacionId ID de la reclamación
     * @param archivos Array de archivos multipart
     * @param descripcion Descripción opcional común
     * @return Lista de DocumentoAdjunto creados
     * @throws Exception Si hay errores en la validación o almacenamiento
     */
    List<DocumentoAdjunto> subirMultiplesDocumentos(Long reclamacionId, MultipartFile[] archivos, String descripcion) throws Exception;
    
    /**
     * Descargar un documento por su ID
     * 
     * @param documentoId ID del documento
     * @return Resource del archivo para descarga
     * @throws Exception Si el documento no existe o hay errores de lectura
     */
    Resource descargarDocumento(Long documentoId) throws Exception;
    
    /**
     * Obtener información de un documento por su ID
     * 
     * @param documentoId ID del documento
     * @return DocumentoAdjunto
     * @throws Exception Si el documento no existe
     */
    DocumentoAdjunto obtenerDocumentoPorId(Long documentoId) throws Exception;
    
    /**
     * Obtener todos los documentos de una reclamación
     * 
     * @param reclamacionId ID de la reclamación
     * @return Lista de DocumentoAdjunto
     */
    List<DocumentoAdjunto> obtenerDocumentosPorReclamacion(Long reclamacionId);
    
    /**
     * Obtener documentos activos de una reclamación
     * 
     * @param reclamacionId ID de la reclamación
     * @return Lista de DocumentoAdjunto activos
     */
    List<DocumentoAdjunto> obtenerDocumentosActivosPorReclamacion(Long reclamacionId);
    
    /**
     * Eliminar un documento (marca como eliminado)
     * 
     * @param documentoId ID del documento
     * @throws Exception Si el documento no existe
     */
    void eliminarDocumento(Long documentoId) throws Exception;
    
    /**
     * Eliminar físicamente un documento del sistema de archivos
     * 
     * @param documentoId ID del documento
     * @throws Exception Si hay errores al eliminar
     */
    void eliminarDocumentoFisicamente(Long documentoId) throws Exception;
    
    /**
     * Validar si se puede subir más documentos a una reclamación
     * 
     * @param reclamacionId ID de la reclamación
     * @return true si se puede subir más documentos
     */
    boolean puedeSubirMasDocumentos(Long reclamacionId);
    
    /**
     * Obtener el número de documentos activos en una reclamación
     * 
     * @param reclamacionId ID de la reclamación
     * @return Número de documentos activos
     */
    int contarDocumentosActivos(Long reclamacionId);
    
    /**
     * Obtener el tamaño total de archivos de una reclamación
     * 
     * @param reclamacionId ID de la reclamación
     * @return Tamaño total en bytes
     */
    long obtenerTamañoTotalPorReclamacion(Long reclamacionId);
    
    /**
     * Validar archivo antes de subir
     * 
     * @param archivo Archivo a validar
     * @throws Exception Si la validación falla
     */
    void validarArchivo(MultipartFile archivo) throws Exception;
    
    /**
     * Obtener estadísticas generales de documentos
     * 
     * @return Map con estadísticas
     */
    Map<String, Object> obtenerEstadisticas();
    
    /**
     * Buscar documentos por criterios
     * 
     * @param nombreOriginal Nombre original del archivo (opcional)
     * @param tipoContenido Tipo de contenido (opcional)
     * @param estado Estado del documento (opcional)
     * @return Lista de DocumentoAdjunto que cumplen los criterios
     */
    List<DocumentoAdjunto> buscarDocumentos(String nombreOriginal, String tipoContenido, EstadoDocumento estado);
}