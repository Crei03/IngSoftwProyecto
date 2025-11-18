package com.seguratuauto.api.handler;

import com.seguratuauto.service.ClienteService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * Controlador para gestionar la confirmación de cuentas vía correo electrónico
 */
@RestController
@RequestMapping("/api/verificacion")
@CrossOrigin(origins = "*")
public class VerificacionController {
    
    private final ClienteService clienteService;
    private final String frontendUrl;
    
    public VerificacionController(ClienteService clienteService,
                                  @Value("${app.frontend-url:http://localhost:5173}") String frontendUrl) {
        this.clienteService = clienteService;
        this.frontendUrl = frontendUrl;
    }
    
    @GetMapping("/confirmar")
    public ResponseEntity<Void> confirmarCuenta(@RequestParam String token) {
        String estado = "success";
        String mensaje = "Cuenta verificada con éxito. Ahora puedes iniciar sesión.";
        
        try {
            clienteService.verificarCliente(token);
        } catch (IllegalArgumentException e) {
            estado = "error";
            mensaje = e.getMessage();
        }
        
        String redirectUrl = String.format("%s?verificacion=%s&mensaje=%s",
                frontendUrl,
                estado,
                URLEncoder.encode(mensaje, StandardCharsets.UTF_8));
        
        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(redirectUrl))
                .build();
    }
}

