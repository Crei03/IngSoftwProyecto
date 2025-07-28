package com.seguratuauto.api.handler;

import com.seguratuauto.api.dto.EvaluadorRequest;
import com.seguratuauto.api.dto.EvaluadorResponse;
import com.seguratuauto.api.dto.ApiResponse;
import com.seguratuauto.api.mapper.EvaluadorMapper;
import com.seguratuauto.model.Evaluador;
import com.seguratuauto.service.EvaluadorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Controlador REST para el manejo de evaluadores
 */
@RestController
@RequestMapping("/api/evaluadores")
@CrossOrigin(origins = "*")
public class EvaluadorController {
    
    private final EvaluadorService evaluadorService;
    private final EvaluadorMapper evaluadorMapper;
    
    @Autowired
    public EvaluadorController(EvaluadorService evaluadorService, EvaluadorMapper evaluadorMapper) {
        this.evaluadorService = evaluadorService;
        this.evaluadorMapper = evaluadorMapper;
    }
    
    /**
     * Crear un nuevo evaluador
     * POST /api/evaluadores
     */
    @PostMapping
    public ResponseEntity<ApiResponse<EvaluadorResponse>> crearEvaluador(@Valid @RequestBody EvaluadorRequest request) {
        try {
            Evaluador evaluador = evaluadorMapper.toEntity(request);
            Evaluador evaluadorCreado = evaluadorService.crearEvaluador(evaluador);
            EvaluadorResponse response = evaluadorMapper.toResponse(evaluadorCreado);
            
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse<>(true, "Evaluador creado exitosamente", response));
                    
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, "Error en los datos: " + e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Error interno del servidor", null));
        }
    }
    
    /**
     * Obtener todos los evaluadores
     * GET /api/evaluadores
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<EvaluadorResponse>>> obtenerTodosLosEvaluadores() {
        try {
            List<Evaluador> evaluadores = evaluadorService.obtenerTodosLosEvaluadores();
            List<EvaluadorResponse> response = evaluadores.stream()
                    .map(evaluadorMapper::toResponse)
                    .collect(Collectors.toList());
            
            return ResponseEntity.ok(new ApiResponse<>(true, 
                    "Evaluadores obtenidos exitosamente (" + response.size() + " registros)", response));
                    
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Error al obtener evaluadores", null));
        }
    }
    
    /**
     * Obtener evaluadores activos
     * GET /api/evaluadores/activos
     */
    @GetMapping("/activos")
    public ResponseEntity<ApiResponse<List<EvaluadorResponse>>> obtenerEvaluadoresActivos() {
        try {
            List<Evaluador> evaluadores = evaluadorService.obtenerEvaluadoresActivos();
            List<EvaluadorResponse> response = evaluadores.stream()
                    .map(evaluadorMapper::toResponse)
                    .collect(Collectors.toList());
            
            return ResponseEntity.ok(new ApiResponse<>(true, 
                    "Evaluadores activos obtenidos exitosamente (" + response.size() + " registros)", response));
                    
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Error al obtener evaluadores activos", null));
        }
    }
    
    /**
     * Obtener un evaluador por ID
     * GET /api/evaluadores/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<EvaluadorResponse>> obtenerEvaluadorPorId(@PathVariable Long id) {
        try {
            Evaluador evaluador = evaluadorService.buscarEvaluadorPorId(id);
            
            if (evaluador == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>(false, "Evaluador no encontrado", null));
            }
            
            EvaluadorResponse response = evaluadorMapper.toResponse(evaluador);
            return ResponseEntity.ok(new ApiResponse<>(true, "Evaluador encontrado", response));
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Error al obtener el evaluador", null));
        }
    }
    
    /**
     * Buscar evaluador por código
     * GET /api/evaluadores/codigo/{codigo}
     */
    @GetMapping("/codigo/{codigo}")
    public ResponseEntity<ApiResponse<EvaluadorResponse>> buscarEvaluadorPorCodigo(@PathVariable String codigo) {
        try {
            Evaluador evaluador = evaluadorService.buscarEvaluadorPorCodigo(codigo);
            
            if (evaluador == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>(false, "Evaluador no encontrado con código: " + codigo, null));
            }
            
            EvaluadorResponse response = evaluadorMapper.toResponse(evaluador);
            return ResponseEntity.ok(new ApiResponse<>(true, "Evaluador encontrado", response));
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Error al buscar el evaluador", null));
        }
    }
    
    /**
     * Buscar evaluador por email
     * GET /api/evaluadores/email/{email}
     */
    @GetMapping("/email/{email}")
    public ResponseEntity<ApiResponse<EvaluadorResponse>> buscarEvaluadorPorEmail(@PathVariable String email) {
        try {
            Evaluador evaluador = evaluadorService.buscarEvaluadorPorEmail(email);
            
            if (evaluador == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>(false, "Evaluador no encontrado con email: " + email, null));
            }
            
            EvaluadorResponse response = evaluadorMapper.toResponse(evaluador);
            return ResponseEntity.ok(new ApiResponse<>(true, "Evaluador encontrado", response));
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Error al buscar el evaluador", null));
        }
    }
    
    /**
     * Buscar evaluadores por nombre
     * GET /api/evaluadores/buscar?nombre={nombre}
     */
    @GetMapping("/buscar")
    public ResponseEntity<ApiResponse<List<EvaluadorResponse>>> buscarEvaluadoresPorNombre(@RequestParam String nombre) {
        try {
            List<Evaluador> evaluadores = evaluadorService.buscarEvaluadoresPorNombre(nombre);
            List<EvaluadorResponse> response = evaluadores.stream()
                    .map(evaluadorMapper::toResponse)
                    .collect(Collectors.toList());
            
            return ResponseEntity.ok(new ApiResponse<>(true, 
                    "Búsqueda completada (" + response.size() + " resultados)", response));
                    
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Error en la búsqueda", null));
        }
    }
    
    /**
     * Buscar evaluadores por especialidad
     * GET /api/evaluadores/especialidad/{especialidad}
     */
    @GetMapping("/especialidad/{especialidad}")
    public ResponseEntity<ApiResponse<List<EvaluadorResponse>>> buscarEvaluadoresPorEspecialidad(@PathVariable String especialidad) {
        try {
            List<Evaluador> evaluadores = evaluadorService.buscarEvaluadoresPorEspecialidad(especialidad);
            List<EvaluadorResponse> response = evaluadores.stream()
                    .map(evaluadorMapper::toResponse)
                    .collect(Collectors.toList());
            
            return ResponseEntity.ok(new ApiResponse<>(true, 
                    "Evaluadores con especialidad '" + especialidad + "' (" + response.size() + " resultados)", response));
                    
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Error al buscar por especialidad", null));
        }
    }
    
    /**
     * Búsqueda avanzada de evaluadores
     * GET /api/evaluadores/buscar-avanzado?nombre={nombre}&email={email}&especialidad={especialidad}
     */
    @GetMapping("/buscar-avanzado")
    public ResponseEntity<ApiResponse<List<EvaluadorResponse>>> buscarEvaluadoresAvanzado(
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String especialidad) {
        try {
            List<Evaluador> evaluadores;
            
            if (nombre != null && email != null) {
                evaluadores = evaluadorService.buscarEvaluadoresPorNombreYEmail(nombre, email);
            } else {
                evaluadores = evaluadorService.buscarEvaluadoresConCriterios(nombre, null, email, especialidad);
            }
            
            List<EvaluadorResponse> response = evaluadores.stream()
                    .map(evaluadorMapper::toResponse)
                    .collect(Collectors.toList());
            
            return ResponseEntity.ok(new ApiResponse<>(true, 
                    "Búsqueda avanzada completada (" + response.size() + " resultados)", response));
                    
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Error en la búsqueda avanzada", null));
        }
    }
    
    /**
     * Actualizar un evaluador
     * PUT /api/evaluadores/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<EvaluadorResponse>> actualizarEvaluador(
            @PathVariable Long id, @Valid @RequestBody EvaluadorRequest request) {
        try {
            Evaluador evaluadorActualizado = evaluadorMapper.toEntity(request);
            evaluadorActualizado = evaluadorService.actualizarEvaluador(id, evaluadorActualizado);
            
            EvaluadorResponse response = evaluadorMapper.toResponse(evaluadorActualizado);
            return ResponseEntity.ok(new ApiResponse<>(true, "Evaluador actualizado exitosamente", response));
            
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, "Error en los datos: " + e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Error al actualizar el evaluador", null));
        }
    }
    
    /**
     * Eliminar un evaluador (eliminación lógica)
     * DELETE /api/evaluadores/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<EvaluadorResponse>> eliminarEvaluador(@PathVariable Long id) {
        try {
            Evaluador evaluadorEliminado = evaluadorService.eliminarEvaluador(id);
            EvaluadorResponse response = evaluadorMapper.toResponse(evaluadorEliminado);
            
            return ResponseEntity.ok(new ApiResponse<>(true, "Evaluador marcado como inactivo", response));
            
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Error al eliminar el evaluador", null));
        }
    }
    
    /**
     * Verificar si existe un evaluador
     * GET /api/evaluadores/{id}/existe
     */
    @GetMapping("/{id}/existe")
    public ResponseEntity<ApiResponse<Boolean>> verificarExistenciaEvaluador(@PathVariable Long id) {
        try {
            boolean existe = evaluadorService.existeEvaluador(id);
            return ResponseEntity.ok(new ApiResponse<>(true, 
                    existe ? "El evaluador existe" : "El evaluador no existe", existe));
                    
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Error al verificar existencia", null));
        }
    }
    
    /**
     * Obtener estadísticas de evaluadores
     * GET /api/evaluadores/estadisticas
     */
    @GetMapping("/estadisticas")
    public ResponseEntity<ApiResponse<Map<String, Object>>> obtenerEstadisticas() {
        try {
            Map<String, Object> estadisticas = new HashMap<>();
            estadisticas.put("totalEvaluadores", evaluadorService.contarTotalEvaluadores());
            estadisticas.put("evaluadoresActivos", evaluadorService.contarEvaluadoresActivos());
            estadisticas.put("evaluadoresInactivos", 
                    evaluadorService.contarTotalEvaluadores() - evaluadorService.contarEvaluadoresActivos());
            
            return ResponseEntity.ok(new ApiResponse<>(true, "Estadísticas obtenidas exitosamente", estadisticas));
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Error al obtener estadísticas", null));
        }
    }
    
    /**
     * Generar nuevo código de evaluador
     * GET /api/evaluadores/nuevo-codigo
     */
    @GetMapping("/nuevo-codigo")
    public ResponseEntity<ApiResponse<Map<String, String>>> generarNuevoCodigo() {
        try {
            String nuevoCodigo = evaluadorService.generarNuevoCodigo();
            Map<String, String> response = new HashMap<>();
            response.put("codigo", nuevoCodigo);
            
            return ResponseEntity.ok(new ApiResponse<>(true, "Nuevo código generado", response));
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Error al generar código", null));
        }
    }
    
    /**
     * Reactivar un evaluador
     * PUT /api/evaluadores/{id}/reactivar
     */
    @PutMapping("/{id}/reactivar")
    public ResponseEntity<ApiResponse<EvaluadorResponse>> reactivarEvaluador(@PathVariable Long id) {
        try {
            Evaluador evaluadorReactivado = evaluadorService.reactivarEvaluador(id);
            EvaluadorResponse response = evaluadorMapper.toResponse(evaluadorReactivado);
            
            return ResponseEntity.ok(new ApiResponse<>(true, "Evaluador reactivado exitosamente", response));
            
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Error al reactivar el evaluador", null));
        }
    }
}
