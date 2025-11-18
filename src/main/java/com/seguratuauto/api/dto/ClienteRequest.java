package com.seguratuauto.api.dto;

import jakarta.validation.constraints.*;

/**
 * DTO para peticiones de creación y actualización de clientes
 */
public class ClienteRequest {
    
    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100, message = "El nombre no puede exceder 100 caracteres")
    private String nombre;
    
    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El formato del email no es válido")
    @Size(max = 150, message = "El email no puede exceder 150 caracteres")
    private String email;
    
    @Pattern(regexp = "^[+]?[0-9\\s\\-()]{0,20}$", message = "El formato del teléfono no es válido")
    @Size(max = 20, message = "El teléfono no puede exceder 20 caracteres")
    private String telefono;
    
    @NotBlank(message = "La contraseña es obligatoria")
    @Pattern(
        regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&.#^()_+\\-=/])[A-Za-z\\d@$!%*?&.#^()_+\\-=/]{8,16}$",
        message = "La contraseña debe tener entre 8 y 16 caracteres, incluir mayúsculas, minúsculas, números y un carácter especial"
    )
    private String password;
    
    // Constructor por defecto
    public ClienteRequest() {}
    
    // Constructor completo
    public ClienteRequest(String nombre, String email, String telefono, String password) {
        this.nombre = nombre;
        this.email = email;
        this.telefono = telefono;
        this.password = password;
    }
    
    // Getters y Setters
    public String getNombre() {
        return nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getTelefono() {
        return telefono;
    }
    
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
}
