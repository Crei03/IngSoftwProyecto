package com.seguratuauto.service;

import com.seguratuauto.model.Cliente;

/**
 * Servicio encargado de enviar correos de verificación de cuenta
 */
public interface EmailVerificationService {
    
    /**
     * Envía un correo de confirmación al cliente con el token generado
     * @param cliente cliente recién registrado
     */
    void enviarCorreoConfirmacion(Cliente cliente);
}

