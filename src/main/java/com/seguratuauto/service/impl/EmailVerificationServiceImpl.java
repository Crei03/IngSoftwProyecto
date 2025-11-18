package com.seguratuauto.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.seguratuauto.service.EmailVerificationService;
import java.util.UUID;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service
public class EmailVerificationServiceImpl implements EmailVerificationService {

    private static final long TOKEN_EXPIRATION_HOURS = 24;

    @Autowired
    private com.seguratuauto.dao.ClienteRepository clienteRepository;

    @Override
    public boolean verificarEmail(String token) {
        try {
            // Implementación básica - puede ampliarse con persistencia de tokens
            return token != null && !token.isEmpty();
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean reenviarEmail(String email) {
        try {
            // Verificar que el email existe en la base de datos
            var cliente = clienteRepository.findByCorreo(email);
            if (cliente.isEmpty()) {
                return false;
            }
            // Aquí se enviaría el correo de verificación
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean estaVerificado(String email) {
        try {
            var cliente = clienteRepository.findByCorreo(email);
            return cliente.isPresent();
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public String generarTokenVerificacion(String email) {
        return UUID.randomUUID().toString();
    }
}
