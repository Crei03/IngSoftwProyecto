package com.seguratuauto.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * DTO para solicitar el restablecimiento de contraseña
 */
public class PasswordRecoveryRequest {
    
    @NotBlank(message = "El email es obligatorio")
    @Email(message = "Proporciona un email válido")
    private String email;
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
}

