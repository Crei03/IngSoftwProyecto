package com.seguratuauto.api.dto;


/**
 * DTO para las respuestas que contienen información de agentes
 */
public class AgenteResponse {
    
    private String idAgente;
    private String nombre;
    private String codigo;
    private String email;
    private String telefono;
    private String cantidadPolizas;
    
    // Constructor por defecto
    public AgenteResponse() {}
    
    // Constructor con parámetros básicos
    public AgenteResponse(String idAgente, String nombre, String codigo, String email, String telefono) {
        this.idAgente = idAgente;
        this.nombre = nombre;
        this.codigo = codigo;
        this.email = email;
        this.telefono = telefono;
        this.cantidadPolizas = "0";
    }
    
    // Constructor completo
    public AgenteResponse(String idAgente, String nombre, String codigo, String email, String telefono, String cantidadPolizas) {
        this.idAgente = idAgente;
        this.nombre = nombre;
        this.codigo = codigo;
        this.email = email;
        this.telefono = telefono;
        this.cantidadPolizas = cantidadPolizas;
    }
    
    // Getters y Setters
    public String getIdAgente() {
        return idAgente;
    }
    
    public void setIdAgente(String idAgente) {
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
    
    public String getCantidadPolizas() {
        return cantidadPolizas;
    }
    
    public void setCantidadPolizas(String cantidadPolizas) {
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
