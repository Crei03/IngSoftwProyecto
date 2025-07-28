package com.seguratuauto.api.handler;

import com.seguratuauto.api.dto.AgenteRequest;
import com.seguratuauto.api.dto.AgenteResponse;
import com.seguratuauto.api.dto.ApiResponse;
import com.seguratuauto.api.mapper.AgenteMapper;
import com.seguratuauto.model.Agente;
import com.seguratuauto.service.AgenteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Controlador REST para el manejo de agentes
 */
@RestController
@RequestMapping("/api/agentes")
@CrossOrigin(origins = "*")
public class AgenteController {
    
    private final AgenteService agenteService;
    private final AgenteMapper agenteMapper;
    
    @Autowired
    public AgenteController(AgenteService agenteService, AgenteMapper agenteMapper) {
        this.agenteService = agenteService;
        this.agenteMapper = agenteMapper;
    }
    
    /**
     * Crear un nuevo agente
     * POST /api/agentes
     */
    @PostMapping
    public ResponseEntity<ApiResponse<AgenteResponse>> crearAgente(@Valid @RequestBody AgenteRequest request) {
        try {
            Agente agente = agenteMapper.toEntity(request);
            Agente agenteCreado = agenteService.crearAgente(agente);
            AgenteResponse response = agenteMapper.toResponse(agenteCreado);
            
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse<>(true, "Agente creado exitosamente", response));
                    
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Error interno del servidor", null));
        }
    }
    
    /**
     * Obtener todos los agentes
     * GET /api/agentes
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<AgenteResponse>>> obtenerTodosLosAgentes() {
        try {
            List<Agente> agentes = agenteService.obtenerTodosLosAgentes();
            List<AgenteResponse> response = agentes.stream()
                    .map(agenteMapper::toResponseBasic) // Versión optimizada para listados
                    .collect(Collectors.toList());
            
            return ResponseEntity.ok(new ApiResponse<>(true, "Agentes obtenidos exitosamente", response));
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Error interno del servidor", null));
        }
    }
    
    /**
     * Obtener agentes activos
     * GET /api/agentes/activos
     */
    @GetMapping("/activos")
    public ResponseEntity<ApiResponse<List<AgenteResponse>>> obtenerAgentesActivos() {
        try {
            List<Agente> agentes = agenteService.obtenerAgentesActivos();
            List<AgenteResponse> response = agentes.stream()
                    .map(agenteMapper::toResponseBasic)
                    .collect(Collectors.toList());
            
            return ResponseEntity.ok(new ApiResponse<>(true, "Agentes activos obtenidos exitosamente", response));
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Error interno del servidor", null));
        }
    }
    
    /**
     * Obtener agente por ID
     * GET /api/agentes/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<AgenteResponse>> obtenerAgentePorId(@PathVariable Long id) {
        try {
            Agente agente = agenteService.buscarAgentePorId(id);
            
            if (agente == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>(false, "Agente no encontrado", null));
            }
            
            AgenteResponse response = agenteMapper.toResponse(agente);
            return ResponseEntity.ok(new ApiResponse<>(true, "Agente encontrado", response));
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Error interno del servidor", null));
        }
    }
    
    /**
     * Obtener agente por código
     * GET /api/agentes/codigo/{codigo}
     */
    @GetMapping("/codigo/{codigo}")
    public ResponseEntity<ApiResponse<AgenteResponse>> obtenerAgentePorCodigo(@PathVariable String codigo) {
        try {
            Agente agente = agenteService.buscarAgentePorCodigo(codigo);
            
            if (agente == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>(false, "Agente no encontrado con el código especificado", null));
            }
            
            AgenteResponse response = agenteMapper.toResponse(agente);
            return ResponseEntity.ok(new ApiResponse<>(true, "Agente encontrado", response));
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Error interno del servidor", null));
        }
    }
    
    /**
     * Obtener agente por email
     * GET /api/agentes/email/{email}
     */
    @GetMapping("/email/{email}")
    public ResponseEntity<ApiResponse<AgenteResponse>> obtenerAgentePorEmail(@PathVariable String email) {
        try {
            Agente agente = agenteService.buscarAgentePorEmail(email);
            
            if (agente == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>(false, "Agente no encontrado con el email especificado", null));
            }
            
            AgenteResponse response = agenteMapper.toResponse(agente);
            return ResponseEntity.ok(new ApiResponse<>(true, "Agente encontrado", response));
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Error interno del servidor", null));
        }
    }
    
    /**
     * Buscar agentes por nombre
     * GET /api/agentes/buscar?nombre={nombre}
     */
    @GetMapping("/buscar")
    public ResponseEntity<ApiResponse<List<AgenteResponse>>> buscarAgentesPorNombre(@RequestParam String nombre) {
        try {
            List<Agente> agentes = agenteService.buscarAgentesPorNombre(nombre);
            List<AgenteResponse> response = agentes.stream()
                    .map(agenteMapper::toResponseBasic)
                    .collect(Collectors.toList());
            
            return ResponseEntity.ok(new ApiResponse<>(true, "Búsqueda completada", response));
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Error interno del servidor", null));
        }
    }
    
    /**
     * Búsqueda múltiple de agentes
     * GET /api/agentes/buscar-multiple?nombre={nombre}&codigo={codigo}&email={email}
     */
    @GetMapping("/buscar-multiple")
    public ResponseEntity<ApiResponse<List<AgenteResponse>>> buscarAgentesPorCriterios(
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) String codigo,
            @RequestParam(required = false) String email) {
        try {
            List<Agente> agentes = agenteService.buscarAgentesPorCriterios(nombre, codigo, email);
            List<AgenteResponse> response = agentes.stream()
                    .map(agenteMapper::toResponseBasic)
                    .collect(Collectors.toList());
            
            return ResponseEntity.ok(new ApiResponse<>(true, "Búsqueda múltiple completada", response));
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Error interno del servidor", null));
        }
    }
    
    /**
     * Actualizar agente
     * PUT /api/agentes/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<AgenteResponse>> actualizarAgente(
            @PathVariable Long id, 
            @Valid @RequestBody AgenteRequest request) {
        try {
            Agente agenteExistente = agenteService.buscarAgentePorId(id);
            
            if (agenteExistente == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>(false, "Agente no encontrado", null));
            }
            
            agenteMapper.updateEntity(agenteExistente, request);
            Agente agenteActualizado = agenteService.actualizarAgente(agenteExistente);
            AgenteResponse response = agenteMapper.toResponse(agenteActualizado);
            
            return ResponseEntity.ok(new ApiResponse<>(true, "Agente actualizado exitosamente", response));
            
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Error interno del servidor", null));
        }
    }
    
    /**
     * Eliminar agente
     * DELETE /api/agentes/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> eliminarAgente(@PathVariable Long id) {
        try {
            boolean eliminado = agenteService.eliminarAgente(id);
            
            if (!eliminado) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>(false, "Agente no encontrado", null));
            }
            
            return ResponseEntity.ok(new ApiResponse<>(true, "Agente eliminado exitosamente", null));
            
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Error interno del servidor", null));
        }
    }
    
    /**
     * Obtener estadísticas de agentes
     * GET /api/agentes/estadisticas
     */
    @GetMapping("/estadisticas")
    public ResponseEntity<ApiResponse<Object>> obtenerEstadisticasAgentes() {
        try {
            long totalAgentes = agenteService.contarAgentes();
            
            // Crear objeto con estadísticas
            Object estadisticas = new Object() {
                public final long totalAgentes = agenteService.contarAgentes();
            };
            
            return ResponseEntity.ok(new ApiResponse<>(true, "Estadísticas obtenidas exitosamente", estadisticas));
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Error interno del servidor", null));
        }
    }
    
    /**
     * Generar nuevo código de agente
     * GET /api/agentes/nuevo-codigo
     */
    @GetMapping("/nuevo-codigo")
    public ResponseEntity<ApiResponse<String>> generarNuevoCodigo() {
        try {
            String nuevoCodigo = agenteService.generarSiguienteCodigoAgente();
            return ResponseEntity.ok(new ApiResponse<>(true, "Código generado exitosamente", nuevoCodigo));
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Error interno del servidor", null));
        }
    }
}
