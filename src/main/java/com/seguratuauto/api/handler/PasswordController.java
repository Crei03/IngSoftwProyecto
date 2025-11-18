package com.seguratuauto.api.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.seguratuauto.api.dto.PasswordRecoveryRequest;
import com.seguratuauto.api.dto.PasswordResetConfirmRequest;
import com.seguratuauto.service.PasswordResetService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/password")
@Validated
@CrossOrigin(origins = "*")
public class PasswordController {

    @Autowired
    private PasswordResetService passwordResetService;

    @PostMapping("/recovery")
    public ResponseEntity<?> solicitarRecuperacion(@Valid @RequestBody PasswordRecoveryRequest request) {
        try {
            boolean exitoso = passwordResetService.solicitarRecuperacion(request.getEmail());
            if (exitoso) {
                return ResponseEntity.ok("Se ha enviado un correo de recuperación de contraseña");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("El email no está registrado");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al solicitar recuperación: " + e.getMessage());
        }
    }

    @PostMapping("/reset-confirm")
    public ResponseEntity<?> confirmarRestablecimiento(@Valid @RequestBody PasswordResetConfirmRequest request) {
        try {
            if (!request.getNewPassword().equals(request.getConfirmPassword())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Las contraseñas no coinciden");
            }

            boolean exitoso = passwordResetService.restablecerContraseña(request.getToken(), request.getNewPassword());
            if (exitoso) {
                return ResponseEntity.ok("Contraseña restablecida exitosamente");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Token inválido o expirado");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al restablecer contraseña: " + e.getMessage());
        }
    }

    @GetMapping("/validar-token/{token}")
    public ResponseEntity<?> validarToken(@PathVariable String token) {
        try {
            boolean valido = passwordResetService.validarToken(token);
            return ResponseEntity.ok(valido);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(false);
        }
    }
}
