package com.seguratuauto.model;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Entidad TarifaBase para definir las tarifas base de cada tipo de seguro
 */
public class TarifaBase {
    
    private Long idTarifa;
    private String tipoSeguro;
    private BigDecimal primaBase;
    private String descripcion;
    private boolean activo;
    
    // Constructor por defecto
    public TarifaBase() {}
    
    // Constructor completo
    public TarifaBase(Long idTarifa, String tipoSeguro, BigDecimal primaBase, String descripcion, boolean activo) {
        this.idTarifa = idTarifa;
        this.tipoSeguro = tipoSeguro;
        this.primaBase = primaBase;
        this.descripcion = descripcion;
        this.activo = activo;
    }
    
    // Getters y Setters
    public Long getIdTarifa() {
        return idTarifa;
    }
    
    public void setIdTarifa(Long idTarifa) {
        this.idTarifa = idTarifa;
    }
    
    public String getTipoSeguro() {
        return tipoSeguro;
    }
    
    public void setTipoSeguro(String tipoSeguro) {
        this.tipoSeguro = tipoSeguro;
    }
    
    public BigDecimal getPrimaBase() {
        return primaBase;
    }
    
    public void setPrimaBase(BigDecimal primaBase) {
        this.primaBase = primaBase;
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
        TarifaBase that = (TarifaBase) o;
        return Objects.equals(idTarifa, that.idTarifa);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(idTarifa);
    }
    
    @Override
    public String toString() {
        return "TarifaBase{" +
                "idTarifa=" + idTarifa +
                ", tipoSeguro='" + tipoSeguro + '\'' +
                ", primaBase=" + primaBase +
                ", activo=" + activo +
                '}';
    }
}
