package com.seguratuauto.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

/**
 * DTO para confirmar el restablecimiento de contraseña
 */
public class PasswordResetConfirmRequest {
    
    @NotBlank(message = "El token es obligatorio")
    private String token;
    
    @NotBlank(message = "La contraseña es obligatoria")
    @Pattern(
        regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&.#^()_+\\-=/])[A-Za-z\\d@$!%*?&.#^()_+\\-=/]{8,16}$",
        message = "La contraseña debe tener entre 8 y 16 caracteres e incluir mayúsculas, minúsculas, números y un carácter especial"
    )
    private String password;
    
    public String getToken() {
        return token;
    }
    
    public void setToken(String token) {
        this.token = token;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
}

