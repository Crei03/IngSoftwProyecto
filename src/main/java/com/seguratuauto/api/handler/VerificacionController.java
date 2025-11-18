package com.seguratuauto.api.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.seguratuauto.service.EmailVerificationService;
import jakarta.validation.constraints.NotBlank;

@RestController
@RequestMapping("/api/verificacion")
@Validated
@CrossOrigin(origins = "*")
public class VerificacionController {

    @Autowired
    private EmailVerificationService emailVerificationService;

    @GetMapping("/verificar-email/{token}")
    public ResponseEntity<?> verificarEmail(@PathVariable @NotBlank String token) {
        try {
            boolean verificado = emailVerificationService.verificarEmail(token);
            if (verificado) {
                return ResponseEntity.ok("Email verificado exitosamente");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Token inválido o expirado");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al verificar email: " + e.getMessage());
        }
    }

    @PostMapping("/reenviar-email/{email}")
    public ResponseEntity<?> reenviarEmail(@PathVariable @NotBlank String email) {
        try {
            boolean reenviado = emailVerificationService.reenviarEmail(email);
            if (reenviado) {
                return ResponseEntity.ok("Email de verificación reenviado");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("El email no está registrado");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al reenviar email: " + e.getMessage());
        }
    }

    @GetMapping("/estado/{email}")
    public ResponseEntity<?> estadoVerificacion(@PathVariable @NotBlank String email) {
        try {
            boolean verificado = emailVerificationService.estaVerificado(email);
            return ResponseEntity.ok(verificado);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(false);
        }
    }
}
