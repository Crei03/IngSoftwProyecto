package com.seguratuauto.api.dto;

import java.math.BigDecimal;

/**
 * DTO para responder datos de la Póliza via API
 */
public class PolizaResponse {
    
    private String idPoliza;
    private String numeroPoliza;
    private String fechaEmision;
    private String fechaVencimiento;
    private String estado;
    private String clienteId;
    private String agenteId;
    private BigDecimal prima;
    private String tipoSeguro;
    private String observaciones;
    
    // Constructor por defecto
    public PolizaResponse() {}
    
    // Constructor completo
    public PolizaResponse(String idPoliza, String numeroPoliza, String fechaEmision, 
                         String fechaVencimiento, String estado, String clienteId, 
                         String agenteId, BigDecimal prima, String tipoSeguro, String observaciones) {
        this.idPoliza = idPoliza;
        this.numeroPoliza = numeroPoliza;
        this.fechaEmision = fechaEmision;
        this.fechaVencimiento = fechaVencimiento;
        this.estado = estado;
        this.clienteId = clienteId;
        this.agenteId = agenteId;
        this.prima = prima;
        this.tipoSeguro = tipoSeguro;
        this.observaciones = observaciones;
    }
    
    // Getters y Setters
    public String getIdPoliza() {
        return idPoliza;
    }
    
    public void setIdPoliza(String idPoliza) {
        this.idPoliza = idPoliza;
    }
    
    public String getNumeroPoliza() {
        return numeroPoliza;
    }
    
    public void setNumeroPoliza(String numeroPoliza) {
        this.numeroPoliza = numeroPoliza;
    }
    
    public String getFechaEmision() {
        return fechaEmision;
    }
    
    public void setFechaEmision(String fechaEmision) {
        this.fechaEmision = fechaEmision;
    }
    
    public String getFechaVencimiento() {
        return fechaVencimiento;
    }
    
    public void setFechaVencimiento(String fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }
    
    public String getEstado() {
        return estado;
    }
    
    public void setEstado(String estado) {
        this.estado = estado;
    }
    
    public String getClienteId() {
        return clienteId;
    }
    
    public void setClienteId(String clienteId) {
        this.clienteId = clienteId;
    }
    
    public String getAgenteId() {
        return agenteId;
    }
    
    public void setAgenteId(String agenteId) {
        this.agenteId = agenteId;
    }
    
    public BigDecimal getPrima() {
        return prima;
    }
    
    public void setPrima(BigDecimal prima) {
        this.prima = prima;
    }
    
    public String getTipoSeguro() {
        return tipoSeguro;
    }
    
    public void setTipoSeguro(String tipoSeguro) {
        this.tipoSeguro = tipoSeguro;
    }
    
    public String getObservaciones() {
        return observaciones;
    }
    
    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
}
