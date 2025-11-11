package com.seguratuauto.api.dto;

/**
 * DTO para las respuestas que contienen información de evaluadores
 * 
 * Ejemplo de uso con Builder pattern:
 * <pre>
 * EvaluadorResponse response = EvaluadorResponse.builder()
 *     .idEvaluador("1")
 *     .nombre("Juan Pérez")
 *     .codigo("EV001")
 *     .email("juan.perez@seguratuauto.com")
 *     .telefono("555-0123")
 *     .especialidad("Autos de lujo")
 *     .activo("true")
 *     .fechaIngreso("2024-01-15")
 *     .build();
 * </pre>
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
    
    // Constructor con parámetros básicos (mantener para compatibilidad)
    public EvaluadorResponse(String idEvaluador, String nombre, String codigo, String email, String telefono) {
        this.idEvaluador = idEvaluador;
        this.nombre = nombre;
        this.codigo = codigo;
        this.email = email;
        this.telefono = telefono;
        this.activo = "true";
    }
    
    // Constructor privado para el Builder
    private EvaluadorResponse(Builder builder) {
        this.idEvaluador = builder.idEvaluador;
        this.nombre = builder.nombre;
        this.codigo = builder.codigo;
        this.email = builder.email;
        this.telefono = builder.telefono;
        this.especialidad = builder.especialidad;
        this.activo = builder.activo;
        this.fechaIngreso = builder.fechaIngreso;
    }
    
    // Método estático para crear el Builder
    public static Builder builder() {
        return new Builder();
    }
    
    // Clase Builder para construcción fluida
    public static class Builder {
        private String idEvaluador;
        private String nombre;
        private String codigo;
        private String email;
        private String telefono;
        private String especialidad;
        private String activo = "true"; // Valor por defecto
        private String fechaIngreso;
        
        public Builder idEvaluador(String idEvaluador) {
            this.idEvaluador = idEvaluador;
            return this;
        }
        
        public Builder nombre(String nombre) {
            this.nombre = nombre;
            return this;
        }
        
        public Builder codigo(String codigo) {
            this.codigo = codigo;
            return this;
        }
        
        public Builder email(String email) {
            this.email = email;
            return this;
        }
        
        public Builder telefono(String telefono) {
            this.telefono = telefono;
            return this;
        }
        
        public Builder especialidad(String especialidad) {
            this.especialidad = especialidad;
            return this;
        }
        
        public Builder activo(String activo) {
            this.activo = activo;
            return this;
        }
        
        public Builder fechaIngreso(String fechaIngreso) {
            this.fechaIngreso = fechaIngreso;
            return this;
        }
        
        public EvaluadorResponse build() {
            return new EvaluadorResponse(this);
        }
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
