package com.seguratuauto.api.handler;

import com.seguratuauto.api.dto.ApiResponse;
import com.seguratuauto.api.dto.PolizaRequest;
import com.seguratuauto.api.dto.PolizaResponse;
import com.seguratuauto.api.mapper.PolizaMapper;
import com.seguratuauto.dao.ClienteRepository;
import com.seguratuauto.model.Cliente;
import com.seguratuauto.model.EstadoPoliza;
import com.seguratuauto.model.Poliza;
import com.seguratuauto.service.PolizaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Controlador REST para operaciones con pólizas
 */
@RestController
@RequestMapping("/api/polizas")
@CrossOrigin(origins = "*") // Para desarrollo, en producción especificar dominios
public class PolizaController {
    
    private final PolizaService polizaService;
    private final ClienteRepository clienteRepository;
    private final PolizaMapper polizaMapper;
    
    @Autowired
    public PolizaController(PolizaService polizaService, 
                          ClienteRepository clienteRepository,
                          PolizaMapper polizaMapper) {
        this.polizaService = polizaService;
        this.clienteRepository = clienteRepository;
        this.polizaMapper = polizaMapper;
    }
    
    /**
     * Crear una nueva póliza
     */
    @PostMapping
    public ResponseEntity<ApiResponse<PolizaResponse>> crearPoliza(@Valid @RequestBody PolizaRequest request) {
        try {
            // Buscar el cliente
            Long clienteId = Long.parseLong(request.getClienteId());
            Cliente cliente = clienteRepository.findById(clienteId)
                    .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado"));
            
            // Convertir request a entidad
            Poliza polizaDatos = polizaMapper.toEntity(request);
            
            // Crear la póliza
            Poliza polizaCreada = polizaService.crearPoliza(cliente, polizaDatos);
            
            // Convertir a response
            PolizaResponse response = polizaMapper.toResponse(polizaCreada);
            
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success("Póliza creada exitosamente", response));
                    
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Error en los datos: " + e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error interno del servidor", e.getMessage()));
        }
    }
    
    /**
     * Obtener todas las pólizas
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<PolizaResponse>>> obtenerTodasLasPolizas() {
        try {
            List<Poliza> polizas = polizaService.obtenerTodasLasPolizas();
            List<PolizaResponse> responses = polizas.stream()
                    .map(polizaMapper::toResponse)
                    .collect(Collectors.toList());
            
            return ResponseEntity.ok(ApiResponse.success("Pólizas obtenidas exitosamente", responses));
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error al obtener pólizas", e.getMessage()));
        }
    }
    
    /**
     * Obtener póliza por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PolizaResponse>> obtenerPolizaPorId(@PathVariable String id) {
        try {
            Long polizaId = Long.parseLong(id);
            Poliza poliza = polizaService.buscarPolizaPorId(polizaId);
            
            if (poliza == null) {
                return ResponseEntity.notFound().build();
            }
            
            PolizaResponse response = polizaMapper.toResponse(poliza);
            return ResponseEntity.ok(ApiResponse.success("Póliza encontrada", response));
            
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("ID de póliza no válido"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error al obtener póliza", e.getMessage()));
        }
    }
    
    /**
     * Obtener póliza por número
     */
    @GetMapping("/numero/{numeroPoliza}")
    public ResponseEntity<ApiResponse<PolizaResponse>> obtenerPolizaPorNumero(@PathVariable String numeroPoliza) {
        try {
            Poliza poliza = polizaService.buscarPolizaPorNumero(numeroPoliza);
            
            if (poliza == null) {
                return ResponseEntity.notFound().build();
            }
            
            PolizaResponse response = polizaMapper.toResponse(poliza);
            return ResponseEntity.ok(ApiResponse.success("Póliza encontrada", response));
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error al obtener póliza", e.getMessage()));
        }
    }
    
    /**
     * Obtener pólizas por cliente
     */
    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<ApiResponse<List<PolizaResponse>>> obtenerPolizasPorCliente(@PathVariable String clienteId) {
        try {
            Long id = Long.parseLong(clienteId);
            List<Poliza> polizas = polizaService.obtenerPolizasPorCliente(id);
            List<PolizaResponse> responses = polizas.stream()
                    .map(polizaMapper::toResponse)
                    .collect(Collectors.toList());
            
            return ResponseEntity.ok(ApiResponse.success("Pólizas del cliente obtenidas", responses));
            
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("ID de cliente no válido"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error al obtener pólizas", e.getMessage()));
        }
    }
    
    /**
     * Obtener pólizas por estado
     */
    @GetMapping("/estado/{estado}")
    public ResponseEntity<ApiResponse<List<PolizaResponse>>> obtenerPolizasPorEstado(@PathVariable String estado) {
        try {
            EstadoPoliza estadoPoliza = EstadoPoliza.valueOf(estado.toUpperCase());
            List<Poliza> polizas = polizaService.obtenerPolizasPorEstado(estadoPoliza);
            List<PolizaResponse> responses = polizas.stream()
                    .map(polizaMapper::toResponse)
                    .collect(Collectors.toList());
            
            return ResponseEntity.ok(ApiResponse.success("Pólizas por estado obtenidas", responses));
            
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Estado no válido: " + estado));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error al obtener pólizas", e.getMessage()));
        }
    }
    
    /**
     * Aprobar una póliza
     */
    @PutMapping("/{id}/aprobar")
    public ResponseEntity<ApiResponse<PolizaResponse>> aprobarPoliza(@PathVariable String id) {
        try {
            Long polizaId = Long.parseLong(id);
            Poliza polizaAprobada = polizaService.aprobarPoliza(polizaId);
            PolizaResponse response = polizaMapper.toResponse(polizaAprobada);
            
            return ResponseEntity.ok(ApiResponse.success("Póliza aprobada exitosamente", response));
            
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("ID de póliza no válido"));
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Estado no válido: " + e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error al aprobar póliza", e.getMessage()));
        }
    }
    
    /**
     * Rechazar una póliza
     */
    @PutMapping("/{id}/rechazar")
    public ResponseEntity<ApiResponse<PolizaResponse>> rechazarPoliza(@PathVariable String id, 
                                                                    @RequestParam(required = false) String motivo) {
        try {
            Long polizaId = Long.parseLong(id);
            Poliza polizaRechazada = polizaService.rechazarPoliza(polizaId, motivo);
            PolizaResponse response = polizaMapper.toResponse(polizaRechazada);
            
            return ResponseEntity.ok(ApiResponse.success("Póliza rechazada exitosamente", response));
            
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("ID de póliza no válido"));
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Estado no válido: " + e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error al rechazar póliza", e.getMessage()));
        }
    }
    
    /**
     * Actualizar una póliza
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<PolizaResponse>> actualizarPoliza(@PathVariable String id, 
                                                                       @Valid @RequestBody PolizaRequest request) {
        try {
            Long polizaId = Long.parseLong(id);
            
            // Buscar la póliza existente
            Poliza polizaExistente = polizaService.buscarPolizaPorId(polizaId);
            if (polizaExistente == null) {
                return ResponseEntity.notFound().build();
            }
            
            // Actualizar los datos
            polizaMapper.updateEntity(polizaExistente, request);
            
            // Guardar los cambios
            Poliza polizaActualizada = polizaService.actualizarPoliza(polizaExistente);
            PolizaResponse response = polizaMapper.toResponse(polizaActualizada);
            
            return ResponseEntity.ok(ApiResponse.success("Póliza actualizada exitosamente", response));
            
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("ID de póliza no válido"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error al actualizar póliza", e.getMessage()));
        }
    }
}
