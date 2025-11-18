package com.seguratuauto.service;

import com.seguratuauto.model.Cliente;

/**
 * Servicio encargado de enviar correos relacionados con el restablecimiento de contraseña.
 */
public interface PasswordResetService {
    
    /**
     * Envía un correo con instrucciones para restablecer la contraseña
     * @param cliente cliente que solicitó el restablecimiento
     * @param token token generado
     */
    void enviarCorreoRecuperacion(Cliente cliente, String token);
}

