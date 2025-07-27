package com.seguratuauto.api.dto;

import java.math.BigDecimal;
import java.util.Map;

/**
 * DTO para respuestas de cotización de precios
 */
public class CotizacionResponse {
    
    private String tipoSeguro;
    private BigDecimal precioBase;
    private BigDecimal descuentosAplicados;
    private BigDecimal recargosAplicados;
    private BigDecimal precioTotal;
    private Map<String, BigDecimal> detalleCalculos;
    private String mensaje;
    
    // Constructor por defecto
    public CotizacionResponse() {}
    
    // Constructor completo
    public CotizacionResponse(String tipoSeguro, BigDecimal precioBase, BigDecimal descuentosAplicados, 
                             BigDecimal recargosAplicados, BigDecimal precioTotal, 
                             Map<String, BigDecimal> detalleCalculos, String mensaje) {
        this.tipoSeguro = tipoSeguro;
        this.precioBase = precioBase;
        this.descuentosAplicados = descuentosAplicados;
        this.recargosAplicados = recargosAplicados;
        this.precioTotal = precioTotal;
        this.detalleCalculos = detalleCalculos;
        this.mensaje = mensaje;
    }
    
    // Getters y Setters
    public String getTipoSeguro() {
        return tipoSeguro;
    }
    
    public void setTipoSeguro(String tipoSeguro) {
        this.tipoSeguro = tipoSeguro;
    }
    
    public BigDecimal getPrecioBase() {
        return precioBase;
    }
    
    public void setPrecioBase(BigDecimal precioBase) {
        this.precioBase = precioBase;
    }
    
    public BigDecimal getDescuentosAplicados() {
        return descuentosAplicados;
    }
    
    public void setDescuentosAplicados(BigDecimal descuentosAplicados) {
        this.descuentosAplicados = descuentosAplicados;
    }
    
    public BigDecimal getRecargosAplicados() {
        return recargosAplicados;
    }
    
    public void setRecargosAplicados(BigDecimal recargosAplicados) {
        this.recargosAplicados = recargosAplicados;
    }
    
    public BigDecimal getPrecioTotal() {
        return precioTotal;
    }
    
    public void setPrecioTotal(BigDecimal precioTotal) {
        this.precioTotal = precioTotal;
    }
    
    public Map<String, BigDecimal> getDetalleCalculos() {
        return detalleCalculos;
    }
    
    public void setDetalleCalculos(Map<String, BigDecimal> detalleCalculos) {
        this.detalleCalculos = detalleCalculos;
    }
    
    public String getMensaje() {
        return mensaje;
    }
    
    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
}
