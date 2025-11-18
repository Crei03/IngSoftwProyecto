package com.seguratuauto.api.handler;

import com.seguratuauto.api.dto.ApiResponse;
import com.seguratuauto.api.dto.PasswordRecoveryRequest;
import com.seguratuauto.api.dto.PasswordResetConfirmRequest;
import com.seguratuauto.service.ClienteService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador para flujo de restablecimiento de contraseña de clientes
 */
@RestController
@RequestMapping("/api/password")
@CrossOrigin(origins = "*")
public class PasswordController {
    
    private final ClienteService clienteService;
    
    public PasswordController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }
    
    @PostMapping("/recuperar")
    public ResponseEntity<ApiResponse<Void>> solicitarRecuperacion(@Valid @RequestBody PasswordRecoveryRequest request) {
        try {
            clienteService.solicitarRecuperacionPassword(request.getEmail());
            return ResponseEntity.ok(ApiResponse.success("Te enviamos un correo con los siguientes pasos.", null));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("No se pudo procesar la solicitud", e.getMessage()));
        }
    }
    
    @PostMapping("/restablecer")
    public ResponseEntity<ApiResponse<Void>> restablecerPassword(@Valid @RequestBody PasswordResetConfirmRequest request) {
        try {
            clienteService.restablecerPassword(request.getToken(), request.getPassword());
            return ResponseEntity.ok(ApiResponse.success("Contraseña actualizada correctamente.", null));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("No se pudo restablecer la contraseña", e.getMessage()));
        }
    }
}

