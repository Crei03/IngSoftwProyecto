package com.seguratuauto.api.handler;

import com.seguratuauto.api.dto.ApiResponse;
import com.seguratuauto.api.dto.ClienteRequest;
import com.seguratuauto.api.dto.ClienteResponse;
import com.seguratuauto.api.mapper.ClienteMapper;
import com.seguratuauto.model.Cliente;
import com.seguratuauto.service.ClienteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Controlador REST para operaciones con clientes
 */
@RestController
@RequestMapping("/api/clientes")
@CrossOrigin(origins = "*") // Para desarrollo, en producción especificar dominios
public class ClienteController {
    
    private final ClienteService clienteService;
    private final ClienteMapper clienteMapper;
    
    @Autowired
    public ClienteController(ClienteService clienteService, ClienteMapper clienteMapper) {
        this.clienteService = clienteService;
        this.clienteMapper = clienteMapper;
    }
    
    /**
     * Crear un nuevo cliente
     */
    @PostMapping
    public ResponseEntity<ApiResponse<ClienteResponse>> crearCliente(@Valid @RequestBody ClienteRequest request) {
        try {
            // Convertir request a entidad
            Cliente cliente = clienteMapper.toEntity(request);
            
            // Crear el cliente
            Cliente clienteCreado = clienteService.crearCliente(cliente);
            
            // Convertir a response
            ClienteResponse response = clienteMapper.toResponse(clienteCreado);
            
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success("Cliente creado exitosamente", response));
                    
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Error en los datos: " + e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error interno del servidor", e.getMessage()));
        }
    }
    
    /**
     * Obtener todos los clientes
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<ClienteResponse>>> obtenerTodosLosClientes() {
        try {
            List<Cliente> clientes = clienteService.obtenerTodosLosClientes();
            List<ClienteResponse> responses = clientes.stream()
                    .map(clienteMapper::toResponse)
                    .collect(Collectors.toList());
            
            return ResponseEntity.ok(ApiResponse.success("Clientes obtenidos exitosamente", responses));
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error al obtener clientes", e.getMessage()));
        }
    }
    
    /**
     * Obtener cliente por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ClienteResponse>> obtenerClientePorId(@PathVariable String id) {
        try {
            Long clienteId = Long.parseLong(id);
            Cliente cliente = clienteService.buscarClientePorId(clienteId);
            
            if (cliente == null) {
                return ResponseEntity.notFound().build();
            }
            
            ClienteResponse response = clienteMapper.toResponse(cliente);
            return ResponseEntity.ok(ApiResponse.success("Cliente encontrado", response));
            
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("ID de cliente no válido"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error al obtener cliente", e.getMessage()));
        }
    }
    
    /**
     * Obtener cliente por email
     */
    @GetMapping("/email/{email}")
    public ResponseEntity<ApiResponse<ClienteResponse>> obtenerClientePorEmail(@PathVariable String email) {
        try {
            Cliente cliente = clienteService.buscarClientePorEmail(email);
            
            if (cliente == null) {
                return ResponseEntity.notFound().build();
            }
            
            ClienteResponse response = clienteMapper.toResponse(cliente);
            return ResponseEntity.ok(ApiResponse.success("Cliente encontrado", response));
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error al obtener cliente", e.getMessage()));
        }
    }
    
    /**
     * Buscar clientes por nombre
     */
    @GetMapping("/buscar")
    public ResponseEntity<ApiResponse<List<ClienteResponse>>> buscarClientesPorNombre(@RequestParam String nombre) {
        try {
            List<Cliente> clientes = clienteService.buscarClientesPorNombre(nombre);
            List<ClienteResponse> responses = clientes.stream()
                    .map(clienteMapper::toResponse)
                    .collect(Collectors.toList());
            
            return ResponseEntity.ok(ApiResponse.success("Búsqueda completada", responses));
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error en la búsqueda", e.getMessage()));
        }
    }
    
    /**
     * Buscar clientes por múltiples criterios
     */
    @GetMapping("/buscar-avanzado")
    public ResponseEntity<ApiResponse<List<ClienteResponse>>> buscarClientesPorCriterios(
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) String email) {
        try {
            List<Cliente> clientes = clienteService.buscarClientesPorCriterios(nombre, email);
            List<ClienteResponse> responses = clientes.stream()
                    .map(clienteMapper::toResponse)
                    .collect(Collectors.toList());
            
            return ResponseEntity.ok(ApiResponse.success("Búsqueda avanzada completada", responses));
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error en la búsqueda", e.getMessage()));
        }
    }
    
    /**
     * Actualizar un cliente
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ClienteResponse>> actualizarCliente(@PathVariable String id, 
                                                                         @Valid @RequestBody ClienteRequest request) {
        try {
            Long clienteId = Long.parseLong(id);
            
            // Buscar el cliente existente
            Cliente clienteExistente = clienteService.buscarClientePorId(clienteId);
            if (clienteExistente == null) {
                return ResponseEntity.notFound().build();
            }
            
            // Actualizar los datos
            clienteMapper.updateEntity(clienteExistente, request);
            
            // Guardar los cambios
            Cliente clienteActualizado = clienteService.actualizarCliente(clienteExistente);
            ClienteResponse response = clienteMapper.toResponse(clienteActualizado);
            
            return ResponseEntity.ok(ApiResponse.success("Cliente actualizado exitosamente", response));
            
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Error en los datos: " + e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error al actualizar cliente", e.getMessage()));
        }
    }
    
    /**
     * Eliminar un cliente
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> eliminarCliente(@PathVariable String id) {
        try {
            Long clienteId = Long.parseLong(id);
            
            boolean eliminado = clienteService.eliminarCliente(clienteId);
            
            if (!eliminado) {
                return ResponseEntity.notFound().build();
            }
            
            return ResponseEntity.ok(ApiResponse.success("Cliente eliminado exitosamente", null));
            
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("ID de cliente no válido"));
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("No se puede eliminar: " + e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error al eliminar cliente", e.getMessage()));
        }
    }
    
    /**
     * Verificar si existe un cliente
     */
    @GetMapping("/{id}/existe")
    public ResponseEntity<ApiResponse<Boolean>> verificarExistenciaCliente(@PathVariable String id) {
        try {
            Long clienteId = Long.parseLong(id);
            boolean existe = clienteService.existeCliente(clienteId);
            
            return ResponseEntity.ok(ApiResponse.success("Verificación completada", existe));
            
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("ID de cliente no válido"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error en la verificación", e.getMessage()));
        }
    }
    
    /**
     * Obtener estadísticas de clientes
     */
    @GetMapping("/estadisticas")
    public ResponseEntity<ApiResponse<Long>> obtenerEstadisticasClientes() {
        try {
            long totalClientes = clienteService.contarClientes();
            
            return ResponseEntity.ok(ApiResponse.success("Estadísticas obtenidas", totalClientes));
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error al obtener estadísticas", e.getMessage()));
        }
    }
}
