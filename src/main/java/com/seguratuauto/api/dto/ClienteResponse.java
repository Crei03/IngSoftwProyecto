package com.seguratuauto.api.dto;

/**
 * DTO para respuestas de cliente
 */
public class ClienteResponse {
    
    private String id;  // Campo principal
    private String idCliente;  // Alias para compatibilidad
    private String nombre;
    private String email;
    private String telefono;
    
    // Constructor por defecto
    public ClienteResponse() {}
    
    // Constructor completo
    public ClienteResponse(String id, String nombre, String email, String telefono) {
        this.id = id;
        this.idCliente = id;  // Mantener sincronizado
        this.nombre = nombre;
        this.email = email;
        this.telefono = telefono;
    }
    
    // Getters y Setters
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
        this.idCliente = id;  // Mantener sincronizado
    }
    
    public String getIdCliente() {
        return idCliente;
    }
    
    public void setIdCliente(String idCliente) {
        this.id = idCliente;  // Mantener sincronizado
        this.idCliente = idCliente;
    }
    
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
}
