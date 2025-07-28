package com.seguratuauto.api.dto;

/**
 * DTO para las respuestas que contienen información de evaluadores
 */
public class EvaluadorResponse {
    
    private String idEvaluador;
    private String nombre;
    private String codigo;
    private String email;
    private String telefono;
    private String especialidad;
    private String activo;
    private String fechaIngreso;
    
    // Constructor por defecto
    public EvaluadorResponse() {}
    
    // Constructor con parámetros básicos
    public EvaluadorResponse(String idEvaluador, String nombre, String codigo, String email, String telefono) {
        this.idEvaluador = idEvaluador;
        this.nombre = nombre;
        this.codigo = codigo;
        this.email = email;
        this.telefono = telefono;
        this.activo = "true";
    }
    
    // Constructor completo
    public EvaluadorResponse(String idEvaluador, String nombre, String codigo, String email, 
                           String telefono, String especialidad, String activo, String fechaIngreso) {
        this.idEvaluador = idEvaluador;
        this.nombre = nombre;
        this.codigo = codigo;
        this.email = email;
        this.telefono = telefono;
        this.especialidad = especialidad;
        this.activo = activo;
        this.fechaIngreso = fechaIngreso;
    }
    
    // Getters y Setters
    public String getIdEvaluador() {
        return idEvaluador;
    }
    
    public void setIdEvaluador(String idEvaluador) {
        this.idEvaluador = idEvaluador;
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
    
    public String getEspecialidad() {
        return especialidad;
    }
    
    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }
    
    public String getActivo() {
        return activo;
    }
    
    public void setActivo(String activo) {
        this.activo = activo;
    }
    
    public String getFechaIngreso() {
        return fechaIngreso;
    }
    
    public void setFechaIngreso(String fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }
    
    @Override
    public String toString() {
        return "EvaluadorResponse{" +
                "idEvaluador='" + idEvaluador + '\'' +
                ", nombre='" + nombre + '\'' +
                ", codigo='" + codigo + '\'' +
                ", email='" + email + '\'' +
                ", telefono='" + telefono + '\'' +
                ", especialidad='" + especialidad + '\'' +
                ", activo='" + activo + '\'' +
                ", fechaIngreso='" + fechaIngreso + '\'' +
                '}';
    }
}
