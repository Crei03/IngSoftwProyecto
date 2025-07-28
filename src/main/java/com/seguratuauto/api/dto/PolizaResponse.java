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
    private String clienteId; // Mantener por compatibilidad
    private String agenteId;  // Mantener por compatibilidad
    private ClienteResponse cliente; // Nuevo objeto cliente
    private AgenteResponse agente;   // Nuevo objeto agente
    private BigDecimal prima;
    private String tipoSeguro;
    private String observaciones;
    private String marca;
    private String modelo;
    private String anioVehiculo;
    
    // Constructor por defecto
    public PolizaResponse() {}
    
    // Constructor completo
    public PolizaResponse(String idPoliza, String numeroPoliza, String fechaEmision, 
                         String fechaVencimiento, String estado, String clienteId, 
                         String agenteId, ClienteResponse cliente, AgenteResponse agente,
                         BigDecimal prima, String tipoSeguro, String observaciones,
                         String marca, String modelo, String anioVehiculo) {
        this.idPoliza = idPoliza;
        this.numeroPoliza = numeroPoliza;
        this.fechaEmision = fechaEmision;
        this.fechaVencimiento = fechaVencimiento;
        this.estado = estado;
        this.clienteId = clienteId;
        this.agenteId = agenteId;
        this.cliente = cliente;
        this.agente = agente;
        this.prima = prima;
        this.tipoSeguro = tipoSeguro;
        this.observaciones = observaciones;
        this.marca = marca;
        this.modelo = modelo;
        this.anioVehiculo = anioVehiculo;
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
    
    public ClienteResponse getCliente() {
        return cliente;
    }
    
    public void setCliente(ClienteResponse cliente) {
        this.cliente = cliente;
    }
    
    public AgenteResponse getAgente() {
        return agente;
    }
    
    public void setAgente(AgenteResponse agente) {
        this.agente = agente;
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
    
    public String getMarca() {
        return marca;
    }
    
    public void setMarca(String marca) {
        this.marca = marca;
    }
    
    public String getModelo() {
        return modelo;
    }
    
    public void setModelo(String modelo) {
        this.modelo = modelo;
    }
    
    public String getAnioVehiculo() {
        return anioVehiculo;
    }
    
    public void setAnioVehiculo(String anioVehiculo) {
        this.anioVehiculo = anioVehiculo;
    }
}
