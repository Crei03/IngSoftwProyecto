package com.seguratuauto.api.dto;


/**
 * DTO para las respuestas que contienen información de agentes
 */
public class AgenteResponse {
    
    private Long idAgente;
    private String nombre;
    private String codigo;
    private String email;
    private String telefono;
    private Long cantidadPolizas;
    
    // Constructor por defecto
    public AgenteResponse() {}
    
    // Constructor con parámetros básicos
    public AgenteResponse(Long idAgente, String nombre, String codigo, String email, String telefono) {
        this.idAgente = idAgente;
        this.nombre = nombre;
        this.codigo = codigo;
        this.email = email;
        this.telefono = telefono;
        this.cantidadPolizas = 0L;
    }
    
    // Constructor completo
    public AgenteResponse(Long idAgente, String nombre, String codigo, String email, String telefono, Long cantidadPolizas) {
        this.idAgente = idAgente;
        this.nombre = nombre;
        this.codigo = codigo;
        this.email = email;
        this.telefono = telefono;
        this.cantidadPolizas = cantidadPolizas;
    }
    
    // Getters y Setters
    public Long getIdAgente() {
        return idAgente;
    }
    
    public void setIdAgente(Long idAgente) {
        this.idAgente = idAgente;
    }
    
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
    
    public Long getCantidadPolizas() {
        return cantidadPolizas;
    }
    
    public void setCantidadPolizas(Long cantidadPolizas) {
        this.cantidadPolizas = cantidadPolizas;
    }
    
    @Override
    public String toString() {
        return "AgenteResponse{" +
                "idAgente=" + idAgente +
                ", nombre='" + nombre + '\'' +
                ", codigo='" + codigo + '\'' +
                ", email='" + email + '\'' +
                ", telefono='" + telefono + '\'' +
                ", cantidadPolizas=" + cantidadPolizas +
                '}';
    }
}
