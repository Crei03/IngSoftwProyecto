package com.seguratuauto.api.handler;

import com.seguratuauto.api.dto.*;
import com.seguratuauto.api.mapper.AgenteMapper;
import com.seguratuauto.api.mapper.ClienteMapper;
import com.seguratuauto.model.Agente;
import com.seguratuauto.model.Cliente;
import com.seguratuauto.service.AgenteService;
import com.seguratuauto.service.ClienteService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador para manejar la autenticación básica de clientes y agentes.
 */
@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {
    
    private final ClienteService clienteService;
    private final AgenteService agenteService;
    private final ClienteMapper clienteMapper;
    private final AgenteMapper agenteMapper;
    
    public AuthController(ClienteService clienteService,
                          AgenteService agenteService,
                          ClienteMapper clienteMapper,
                          AgenteMapper agenteMapper) {
        this.clienteService = clienteService;
        this.agenteService = agenteService;
        this.clienteMapper = clienteMapper;
        this.agenteMapper = agenteMapper;
    }
    
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@Valid @RequestBody LoginRequest request) {
        try {
            String tipo = request.getTipoUsuario().toLowerCase();
            LoginResponse response = new LoginResponse();
            response.setTipoUsuario(tipo);
            
            if ("cliente".equals(tipo)) {
                Cliente cliente = clienteService.autenticarCliente(request.getEmail(), request.getPassword());
                response.setCliente(clienteMapper.toResponse(cliente));
            } else {
                Agente agente = agenteService.autenticarAgente(request.getEmail(), request.getPassword());
                response.setAgente(agenteMapper.toResponse(agente));
            }
            
            return ResponseEntity.ok(ApiResponse.success("Inicio de sesión exitoso", response));
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error al iniciar sesión", e.getMessage()));
        }
    }
}

