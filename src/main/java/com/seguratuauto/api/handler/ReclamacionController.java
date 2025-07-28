package com.seguratuauto.api.handler;

import com.seguratuauto.api.dto.ReclamacionRequest;
import com.seguratuauto.api.dto.EvaluacionRequest;
import com.seguratuauto.api.dto.RechazoRequest;
import com.seguratuauto.model.Reclamacion;
import com.seguratuauto.model.EstadoReclamacion;
import com.seguratuauto.service.ReclamacionService;
import com.seguratuauto.service.impl.ReclamacionServiceImpl;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controlador REST para funcionalidades de reclamaciones
 * Utiliza el patrón Strategy para evaluación flexible de reclamaciones
 */
@RestController
@RequestMapping("/api/reclamaciones")
@Validated
@CrossOrigin(origins = "*")
public class ReclamacionController {
    
    private final ReclamacionService reclamacionService;
    
    public ReclamacionController() {
        this.reclamacionService = new ReclamacionServiceImpl();
    }
    
    /**
     * Registra una nueva reclamación
     * 
     * @param request Datos de la reclamación
     * @return Reclamación creada
     */
    @PostMapping("/registrar")
    public ResponseEntity<Map<String, Object>> registrarReclamacion(@Valid @RequestBody ReclamacionRequest request) {
        try {
            Reclamacion reclamacion = reclamacionService.registrarReclamacion(
                request.getPolizaId(),
                request.getDescripcion(),
                request.getMontoReclamado()
            );
            
            Map<String, Object> response = new HashMap<>();
            response.put("reclamacion", reclamacion);
            response.put("mensaje", "Reclamación registrada exitosamente");
            response.put("numeroReclamacion", reclamacion.getNumeroReclamacion());
            
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
            
        } catch (IllegalArgumentException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Datos inválidos");
            errorResponse.put("mensaje", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
            
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Error interno del servidor");
            errorResponse.put("mensaje", "No se pudo registrar la reclamación");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
    
    /**
     * Evalúa una reclamación usando el patrón Strategy
     * 
     * @param reclamacionId ID de la reclamación
     * @param request Datos de la evaluación
     * @return Resultado de la evaluación
     */
    @PostMapping("/{reclamacionId}/evaluar")
    public ResponseEntity<Map<String, Object>> evaluarReclamacion(
            @PathVariable String reclamacionId,
            @Valid @RequestBody EvaluacionRequest request) {
        try {
            Long id = Long.parseLong(reclamacionId);
            
            boolean resultado = reclamacionService.evaluarReclamacion(
                id,
                request.getMontoAprobado(),
                request.getObservaciones(),
                request.getEvaluador()
            );
            
            Reclamacion reclamacion = reclamacionService.buscarReclamacionPorId(id);
            
            Map<String, Object> response = new HashMap<>();
            response.put("resultado", resultado);
            response.put("reclamacion", reclamacion);
            response.put("mensaje", "Reclamación evaluada exitosamente");
            response.put("estrategiaUsada", determinarEstrategiaUsada(reclamacion));
            
            return ResponseEntity.ok(response);
            
        } catch (NumberFormatException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "ID de reclamación inválido o datos incorrectos");
            errorResponse.put("mensaje", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
            
        } catch (IllegalStateException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Estado de reclamación inválido");
            errorResponse.put("mensaje", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
            
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Error interno del servidor");
            errorResponse.put("mensaje", "No se pudo evaluar la reclamación");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
    
    /**
     * Aprueba una reclamación
     * 
     * @param reclamacionId ID de la reclamación
     * @param evaluador Nombre del evaluador
     * @return Resultado de la aprobación
     */
    @PostMapping("/{reclamacionId}/aprobar")
    public ResponseEntity<Map<String, Object>> aprobarReclamacion(
            @PathVariable String reclamacionId,
            @RequestParam String evaluador) {
        try {
            Long id = Long.parseLong(reclamacionId);
            
            boolean resultado = reclamacionService.aprobarReclamacion(id, evaluador);
            Reclamacion reclamacion = reclamacionService.buscarReclamacionPorId(id);
            
            Map<String, Object> response = new HashMap<>();
            response.put("resultado", resultado);
            response.put("reclamacion", reclamacion);
            response.put("mensaje", "Reclamación aprobada exitosamente");
            
            return ResponseEntity.ok(response);
            
        } catch (IllegalArgumentException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "ID de reclamación inválido");
            errorResponse.put("mensaje", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
            
        } catch (IllegalStateException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Estado de reclamación inválido para aprobación");
            errorResponse.put("mensaje", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
    
    /**
     * Rechaza una reclamación
     * 
     * @param reclamacionId ID de la reclamación
     * @param request Datos del rechazo
     * @return Resultado del rechazo
     */
    @PostMapping("/{reclamacionId}/rechazar")
    public ResponseEntity<Map<String, Object>> rechazarReclamacion(
            @PathVariable String reclamacionId,
            @Valid @RequestBody RechazoRequest request) {
        try {
            Long id = Long.parseLong(reclamacionId);
            
            boolean resultado = reclamacionService.rechazarReclamacion(
                id,
                request.getMotivo(),
                request.getEvaluador()
            );
            
            Reclamacion reclamacion = reclamacionService.buscarReclamacionPorId(id);
            
            Map<String, Object> response = new HashMap<>();
            response.put("resultado", resultado);
            response.put("reclamacion", reclamacion);
            response.put("mensaje", "Reclamación rechazada exitosamente");
            
            return ResponseEntity.ok(response);
            
        } catch (NumberFormatException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "ID de reclamación inválido");
            errorResponse.put("mensaje", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
            
        } catch (IllegalStateException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Estado de reclamación inválido para rechazo");
            errorResponse.put("mensaje", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
    
    /**
     * Procesa el pago de una reclamación aprobada
     * 
     * @param reclamacionId ID de la reclamación
     * @return Resultado del procesamiento de pago
     */
    @PostMapping("/{reclamacionId}/pagar")
    public ResponseEntity<Map<String, Object>> procesarPago(@PathVariable String reclamacionId) {
        try {
            Long id = Long.parseLong(reclamacionId);
            
            boolean resultado = reclamacionService.procesarPago(id);
            Reclamacion reclamacion = reclamacionService.buscarReclamacionPorId(id);
            
            Map<String, Object> response = new HashMap<>();
            response.put("resultado", resultado);
            response.put("reclamacion", reclamacion);
            response.put("mensaje", "Pago procesado exitosamente");
            
            return ResponseEntity.ok(response);
            
        } catch (NumberFormatException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "ID de reclamación inválido");
            errorResponse.put("mensaje", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
            
        } catch (IllegalStateException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Estado de reclamación inválido para pago");
            errorResponse.put("mensaje", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
    
    /**
     * Busca una reclamación por ID
     * 
     * @param reclamacionId ID de la reclamación
     * @return Reclamación encontrada
     */
    @GetMapping("/{reclamacionId}")
    public ResponseEntity<Map<String, Object>> buscarReclamacion(@PathVariable String reclamacionId) {
        try {
            Long id = Long.parseLong(reclamacionId);
            Reclamacion reclamacion = reclamacionService.buscarReclamacionPorId(id);
            
            if (reclamacion == null) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("error", "Reclamación no encontrada");
                errorResponse.put("mensaje", "No existe una reclamación con el ID proporcionado");
                return ResponseEntity.notFound().build();
            }
            
            Map<String, Object> response = new HashMap<>();
            response.put("reclamacion", reclamacion);
            response.put("mensaje", "Reclamación encontrada");
            
            return ResponseEntity.ok(response);
            
        } catch (NumberFormatException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "ID de reclamación inválido");
            errorResponse.put("mensaje", "El formato del ID no es válido");
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
    
    /**
     * Obtiene todas las reclamaciones de una póliza
     * 
     * @param polizaId ID de la póliza
     * @return Lista de reclamaciones
     */
    @GetMapping("/poliza/{polizaId}")
    public ResponseEntity<Map<String, Object>> obtenerReclamacionesPorPoliza(@PathVariable String polizaId) {
        try {
            Long id = Long.parseLong(polizaId);
            List<Reclamacion> reclamaciones = reclamacionService.obtenerReclamacionesPorPoliza(id);
            
            Map<String, Object> response = new HashMap<>();
            response.put("reclamaciones", reclamaciones);
            response.put("total", reclamaciones.size());
            response.put("mensaje", "Reclamaciones obtenidas exitosamente");
            
            return ResponseEntity.ok(response);
            
        } catch (NumberFormatException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "ID de póliza inválido");
            errorResponse.put("mensaje", "El formato del ID no es válido");
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
    
    /**
     * Obtiene reclamaciones por estado
     * 
     * @param estado Estado de las reclamaciones
     * @return Lista de reclamaciones
     */
    @GetMapping("/estado/{estado}")
    public ResponseEntity<Map<String, Object>> obtenerReclamacionesPorEstado(@PathVariable String estado) {
        try {
            EstadoReclamacion estadoEnum = EstadoReclamacion.valueOf(estado.toUpperCase());
            List<Reclamacion> reclamaciones = reclamacionService.obtenerReclamacionesPorEstado(estadoEnum);
            
            Map<String, Object> response = new HashMap<>();
            response.put("reclamaciones", reclamaciones);
            response.put("estado", estadoEnum);
            response.put("total", reclamaciones.size());
            response.put("mensaje", "Reclamaciones obtenidas exitosamente");
            
            return ResponseEntity.ok(response);
            
        } catch (IllegalArgumentException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Estado inválido");
            errorResponse.put("mensaje", "Estados válidos: REGISTRADA, EN_EVALUACION, APROBADA, RECHAZADA, PAGADA");
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
    
    /**
     * Obtiene todas las reclamaciones
     * 
     * @return Lista completa de reclamaciones
     */
    @GetMapping("/todas")
    public ResponseEntity<Map<String, Object>> obtenerTodasLasReclamaciones() {
        List<Reclamacion> reclamaciones = reclamacionService.obtenerTodasLasReclamaciones();
        
        Map<String, Object> response = new HashMap<>();
        response.put("reclamaciones", reclamaciones);
        response.put("total", reclamaciones.size());
        response.put("mensaje", "Todas las reclamaciones obtenidas exitosamente");
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * Endpoint de prueba para verificar el funcionamiento del controlador
     * 
     * @return Mensaje de estado
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "OK");
        response.put("service", "Reclamaciones Service");
        response.put("message", "Servicio de reclamaciones funcionando correctamente");
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * Determina qué estrategia fue usada basada en el monto de la reclamación
     */
    private String determinarEstrategiaUsada(Reclamacion reclamacion) {
        if (reclamacion.getMontoReclamado().compareTo(java.math.BigDecimal.valueOf(5000)) <= 0) {
            return "Evaluación Automática";
        } else if (reclamacion.getMontoReclamado().compareTo(java.math.BigDecimal.valueOf(20000)) <= 0) {
            return "Evaluación Manual";
        } else {
            return "Evaluación Especializada";
        }
    }
}
