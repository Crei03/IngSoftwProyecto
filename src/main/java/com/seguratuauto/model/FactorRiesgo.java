package com.seguratuauto.model;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Entidad FactorRiesgo para el cálculo de recargos en pricing
 */
public class FactorRiesgo {
    
    private Long idFactor;
    private String nombre;
    private String tipo; // EDAD, EXPERIENCIA, UBICACION, VEHICULO, etc.
    private BigDecimal porcentajeRecargo;
    private String descripcion;
    private boolean activo;
    
    // Constructor por defecto
    public FactorRiesgo() {}
    
    // Constructor completo
    public FactorRiesgo(Long idFactor, String nombre, String tipo, BigDecimal porcentajeRecargo, String descripcion, boolean activo) {
        this.idFactor = idFactor;
        this.nombre = nombre;
        this.tipo = tipo;
        this.porcentajeRecargo = porcentajeRecargo;
        this.descripcion = descripcion;
        this.activo = activo;
    }
    
    // Getters y Setters
    public Long getIdFactor() {
        return idFactor;
    }
    
    public void setIdFactor(Long idFactor) {
        this.idFactor = idFactor;
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
    
    public BigDecimal getPorcentajeRecargo() {
        return porcentajeRecargo;
    }
    
    public void setPorcentajeRecargo(BigDecimal porcentajeRecargo) {
        this.porcentajeRecargo = porcentajeRecargo;
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
    
    // Métodos equals, hashCode y toString
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FactorRiesgo that = (FactorRiesgo) o;
        return Objects.equals(idFactor, that.idFactor);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(idFactor);
    }
    
    @Override
    public String toString() {
        return "FactorRiesgo{" +
                "idFactor=" + idFactor +
                ", nombre='" + nombre + '\'' +
                ", tipo='" + tipo + '\'' +
                ", porcentajeRecargo=" + porcentajeRecargo +
                ", activo=" + activo +
                '}';
    }
}
