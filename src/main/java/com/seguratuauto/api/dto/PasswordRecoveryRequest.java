package com.seguratuauto.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class PasswordRecoveryRequest {
    @NotBlank(message = "El email es requerido")
    @Email(message = "El email debe ser válido")
    private String email;

    public PasswordRecoveryRequest() {
    }

    public PasswordRecoveryRequest(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
