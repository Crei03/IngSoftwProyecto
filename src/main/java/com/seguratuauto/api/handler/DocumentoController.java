package com.seguratuauto.api.handler;

import com.seguratuauto.api.dto.ApiResponse;
import com.seguratuauto.api.dto.DocumentoResponse;
import com.seguratuauto.model.DocumentoAdjunto;
import com.seguratuauto.model.EstadoDocumento;
import com.seguratuauto.service.DocumentoAdjuntoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Controlador REST para operaciones con documentos adjuntos
 */
@RestController
@RequestMapping("/api/documentos")
@CrossOrigin(origins = "*")
public class DocumentoController {
    
    private final DocumentoAdjuntoService documentoService;
    
    @Autowired
    public DocumentoController(DocumentoAdjuntoService documentoService) {
        this.documentoService = documentoService;
    }
    
    /**
     * Subir un documento adjunto a una reclamación
     * POST /api/documentos/subir
     */
    @PostMapping("/subir")
    public ResponseEntity<ApiResponse<DocumentoResponse>> subirDocumento(
            @RequestParam("reclamacionId") Long reclamacionId,
            @RequestParam("archivo") MultipartFile archivo,
            @RequestParam(value = "descripcion", required = false) String descripcion) {
        
        try {
            DocumentoAdjunto documento = documentoService.subirDocumento(reclamacionId, archivo, descripcion);
            DocumentoResponse response = mapToResponse(documento);
            
            return ResponseEntity.ok(ApiResponse.success("Documento subido exitosamente", response));
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Error al subir documento", e.getMessage()));
        }
    }
    
    /**
     * Subir múltiples documentos a una reclamación
     * POST /api/documentos/subir-multiples
     */
    @PostMapping("/subir-multiples")
    public ResponseEntity<ApiResponse<List<DocumentoResponse>>> subirMultiplesDocumentos(
            @RequestParam("reclamacionId") Long reclamacionId,
            @RequestParam("archivos") MultipartFile[] archivos,
            @RequestParam(value = "descripcion", required = false) String descripcion) {
        
        try {
            List<DocumentoAdjunto> documentos = documentoService.subirMultiplesDocumentos(reclamacionId, archivos, descripcion);
            List<DocumentoResponse> responses = documentos.stream()
                    .map(this::mapToResponse)
                    .collect(Collectors.toList());
            
            return ResponseEntity.ok(ApiResponse.success(
                    String.format("Se subieron %d documentos exitosamente", documentos.size()), 
                    responses));
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Error al subir documentos", e.getMessage()));
        }
    }
    
    /**
     * Descargar un documento por ID
     * GET /api/documentos/{id}/descargar
     */
    @GetMapping("/{id}/descargar")
    public ResponseEntity<Resource> descargarDocumento(@PathVariable Long id, HttpServletRequest request) {
        try {
            DocumentoAdjunto documento = documentoService.obtenerDocumentoPorId(id);
            Resource resource = documentoService.descargarDocumento(id);
            
            // Determinar el tipo de contenido
            String contentType = null;
            try {
                contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
            } catch (IOException ex) {
                contentType = documento.getTipoContenido();
            }
            
            if (contentType == null) {
                contentType = "application/octet-stream";
            }
            
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, 
                            "attachment; filename=\"" + documento.getNombreOriginal() + "\"")
                    .body(resource);
                    
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * Obtener información de un documento
     * GET /api/documentos/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<DocumentoResponse>> obtenerDocumento(@PathVariable Long id) {
        try {
            DocumentoAdjunto documento = documentoService.obtenerDocumentoPorId(id);
            DocumentoResponse response = mapToResponse(documento);
            
            return ResponseEntity.ok(ApiResponse.success("Documento obtenido exitosamente", response));
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Documento no encontrado", e.getMessage()));
        }
    }
    
    /**
     * Obtener documentos de una reclamación
     * GET /api/documentos/reclamacion/{reclamacionId}
     */
    @GetMapping("/reclamacion/{reclamacionId}")
    public ResponseEntity<ApiResponse<List<DocumentoResponse>>> obtenerDocumentosPorReclamacion(
            @PathVariable Long reclamacionId,
            @RequestParam(value = "soloActivos", defaultValue = "true") boolean soloActivos) {
        
        try {
            List<DocumentoAdjunto> documentos;
            if (soloActivos) {
                documentos = documentoService.obtenerDocumentosActivosPorReclamacion(reclamacionId);
            } else {
                documentos = documentoService.obtenerDocumentosPorReclamacion(reclamacionId);
            }
            
            List<DocumentoResponse> responses = documentos.stream()
                    .map(this::mapToResponse)
                    .collect(Collectors.toList());
            
            return ResponseEntity.ok(ApiResponse.success(
                    String.format("Se encontraron %d documentos", documentos.size()), 
                    responses));
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error al obtener documentos", e.getMessage()));
        }
    }
    
    /**
     * Eliminar un documento (marca como eliminado)
     * DELETE /api/documentos/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> eliminarDocumento(@PathVariable Long id) {
        try {
            documentoService.eliminarDocumento(id);
            
            return ResponseEntity.ok(ApiResponse.success("Documento eliminado exitosamente"));
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Error al eliminar documento", e.getMessage()));
        }
    }
    
    /**
     * Eliminar físicamente un documento
     * DELETE /api/documentos/{id}/fisico
     */
    @DeleteMapping("/{id}/fisico")
    public ResponseEntity<ApiResponse<String>> eliminarDocumentoFisicamente(@PathVariable Long id) {
        try {
            documentoService.eliminarDocumentoFisicamente(id);
            
            return ResponseEntity.ok(ApiResponse.success("Documento eliminado físicamente"));
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error al eliminar documento físicamente", e.getMessage()));
        }
    }
    
    /**
     * Verificar si se pueden subir más documentos a una reclamación
     * GET /api/documentos/reclamacion/{reclamacionId}/puede-subir
     */
    @GetMapping("/reclamacion/{reclamacionId}/puede-subir")
    public ResponseEntity<ApiResponse<Map<String, Object>>> verificarPuedeSubirMas(@PathVariable Long reclamacionId) {
        try {
            boolean puedeSubir = documentoService.puedeSubirMasDocumentos(reclamacionId);
            int documentosActivos = documentoService.contarDocumentosActivos(reclamacionId);
            long tamañoTotal = documentoService.obtenerTamañoTotalPorReclamacion(reclamacionId);
            
            Map<String, Object> info = new HashMap<>();
            info.put("puedeSubir", puedeSubir);
            info.put("documentosActivos", documentosActivos);
            info.put("tamañoTotalBytes", tamañoTotal);
            info.put("tamañoTotalFormatted", formatearTamaño(tamañoTotal));
            
            return ResponseEntity.ok(ApiResponse.success("Información de carga obtenida", info));
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error al verificar capacidad de carga", e.getMessage()));
        }
    }
    
    /**
     * Obtener estadísticas de documentos
     * GET /api/documentos/estadisticas
     */
    @GetMapping("/estadisticas")
    public ResponseEntity<ApiResponse<Map<String, Object>>> obtenerEstadisticas() {
        try {
            Map<String, Object> estadisticas = documentoService.obtenerEstadisticas();
            
            return ResponseEntity.ok(ApiResponse.success("Estadísticas obtenidas", estadisticas));
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error al obtener estadísticas", e.getMessage()));
        }
    }
    
    /**
     * Buscar documentos por criterios
     * GET /api/documentos/buscar
     */
    @GetMapping("/buscar")
    public ResponseEntity<ApiResponse<List<DocumentoResponse>>> buscarDocumentos(
            @RequestParam(value = "nombre", required = false) String nombreOriginal,
            @RequestParam(value = "tipo", required = false) String tipoContenido,
            @RequestParam(value = "estado", required = false) EstadoDocumento estado) {
        
        try {
            List<DocumentoAdjunto> documentos = documentoService.buscarDocumentos(nombreOriginal, tipoContenido, estado);
            List<DocumentoResponse> responses = documentos.stream()
                    .map(this::mapToResponse)
                    .collect(Collectors.toList());
            
            return ResponseEntity.ok(ApiResponse.success(
                    String.format("Se encontraron %d documentos", documentos.size()), 
                    responses));
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error en la búsqueda", e.getMessage()));
        }
    }
    
    /**
     * Health check del servicio
     * GET /api/documentos/health
     */
    @GetMapping("/health")
    public ResponseEntity<ApiResponse<Map<String, String>>> healthCheck() {
        Map<String, String> health = new HashMap<>();
        health.put("status", "OK");
        health.put("service", "Documento Service");
        health.put("timestamp", java.time.LocalDateTime.now().toString());
        
        return ResponseEntity.ok(ApiResponse.success("Servicio funcionando correctamente", health));
    }
    
    // Métodos auxiliares privados
    
    private DocumentoResponse mapToResponse(DocumentoAdjunto documento) {
        DocumentoResponse response = new DocumentoResponse(
            documento.getIdDocumento(),
            documento.getReclamacionId(),
            documento.getNombreOriginal(),
            documento.getNombreArchivo(),
            documento.getTipoContenido(),
            documento.getTamaño(),
            "/api/documentos/" + documento.getIdDocumento() + "/descargar",
            documento.getFechaSubida(),
            documento.getDescripcion(),
            documento.getEstado()
        );
        
        return response;
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