package com.seguratuauto.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Entidad Descuento para aplicar descuentos en pricing
 */
public class Descuento {
    
    private Long idDescuento;
    private String nombre;
    private String tipo; // CLIENTE_NUEVO, BUEN_CONDUCTOR, VEHICULO_SEGURO, etc.
    private BigDecimal porcentaje;
    private LocalDateTime vigenciaInicio;
    private LocalDateTime vigenciaFin;
    private String descripcion;
    private boolean activo;
    
    // Constructor por defecto
    public Descuento() {}
    
    // Constructor completo
    public Descuento(Long idDescuento, String nombre, String tipo, BigDecimal porcentaje, 
                    LocalDateTime vigenciaInicio, LocalDateTime vigenciaFin, String descripcion, boolean activo) {
        this.idDescuento = idDescuento;
        this.nombre = nombre;
        this.tipo = tipo;
        this.porcentaje = porcentaje;
        this.vigenciaInicio = vigenciaInicio;
        this.vigenciaFin = vigenciaFin;
        this.descripcion = descripcion;
        this.activo = activo;
    }
    
    // Getters y Setters
    public Long getIdDescuento() {
        return idDescuento;
    }
    
    public void setIdDescuento(Long idDescuento) {
        this.idDescuento = idDescuento;
    }
    
    public String getNombre() {
        return nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public String getTipo() {
        return tipo;
    }
    
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    
    public BigDecimal getPorcentaje() {
        return porcentaje;
    }
    
    public void setPorcentaje(BigDecimal porcentaje) {
        this.porcentaje = porcentaje;
    }
    
    public LocalDateTime getVigenciaInicio() {
        return vigenciaInicio;
    }
    
    public void setVigenciaInicio(LocalDateTime vigenciaInicio) {
        this.vigenciaInicio = vigenciaInicio;
    }
    
    public LocalDateTime getVigenciaFin() {
        return vigenciaFin;
    }
    
    public void setVigenciaFin(LocalDateTime vigenciaFin) {
        this.vigenciaFin = vigenciaFin;
    }
    
    public String getDescripcion() {
        return descripcion;
    }
    
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    
    public boolean isActivo() {
        return activo;
    }
    
    public void setActivo(boolean activo) {
        this.activo = activo;
    }
    
    /**
     * Verifica si el descuento está vigente en la fecha actual
     */
    public boolean esVigente() {
        LocalDateTime ahora = LocalDateTime.now();
        return activo && 
               (vigenciaInicio == null || !ahora.isBefore(vigenciaInicio)) &&
               (vigenciaFin == null || !ahora.isAfter(vigenciaFin));
    }
    
    // Métodos equals, hashCode y toString
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Descuento descuento = (Descuento) o;
        return Objects.equals(idDescuento, descuento.idDescuento);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(idDescuento);
    }
    
    @Override
    public String toString() {
        return "Descuento{" +
                "idDescuento=" + idDescuento +
                ", nombre='" + nombre + '\'' +
                ", tipo='" + tipo + '\'' +
                ", porcentaje=" + porcentaje +
                ", vigente=" + esVigente() +
                '}';
    }
}
