package com.seguratuauto.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * DTO para las peticiones de creación y actualización de agentes
 */
public class AgenteRequest {
    
    @NotBlank(message = "El nombre del agente es obligatorio")
    @Size(max = 100, message = "El nombre no puede exceder 100 caracteres")
    private String nombre;
    
    @Size(max = 20, message = "El código no puede exceder 20 caracteres")
    @Pattern(regexp = "^[A-Za-z0-9]*$", message = "El código solo puede contener letras y números")
    private String codigo;
    
    @Email(message = "El formato del email no es válido")
    @Size(max = 150, message = "El email no puede exceder 150 caracteres")
    private String email;
    
    @Size(max = 20, message = "El teléfono no puede exceder 20 caracteres")
    private String telefono;
    
    @NotBlank(message = "La contraseña es obligatoria")
    @Pattern(
        regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&.#^()_+\\-=/])[A-Za-z\\d@$!%*?&.#^()_+\\-=/]{8,16}$",
        message = "La contraseña debe tener entre 8 y 16 caracteres e incluir mayúsculas, minúsculas, números y un carácter especial"
    )
    private String password;
    
    // Constructor por defecto
    public AgenteRequest() {}
    
    // Constructor con parámetros
    public AgenteRequest(String nombre, String codigo, String email, String telefono, String password) {
        this.nombre = nombre;
        this.codigo = codigo;
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
    
    public String getCodigo() {
        return codigo;
    }
    
    public void setCodigo(String codigo) {
        this.codigo = codigo;
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
    
    @Override
    public String toString() {
        return "AgenteRequest{" +
                "nombre='" + nombre + '\'' +
                ", codigo='" + codigo + '\'' +
                ", email='" + email + '\'' +
                ", telefono='" + telefono + '\'' +
                '}';
    }
}
