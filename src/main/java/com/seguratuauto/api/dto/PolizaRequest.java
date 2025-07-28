package com.seguratuauto.api.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

/**
 * DTO para transferir datos de la Póliza via API con validaciones
 */
public class PolizaRequest {
    
    @NotBlank(message = "El número de póliza es obligatorio")
    @Size(max = 50, message = "El número de póliza no puede exceder 50 caracteres")
    private String numeroPoliza;
    
    @NotNull(message = "La fecha de emisión es obligatoria")
    @NotBlank(message = "La fecha de emisión no puede estar vacía")
    private String fechaEmision;
    
    private String fechaVencimiento;
    
    @NotBlank(message = "El estado es obligatorio")
    private String estado;
    
    @NotBlank(message = "El ID del cliente es obligatorio")
    private String clienteId;
    
    @NotBlank(message = "El ID del agente es obligatorio")
    private String agenteId;
    
    @NotNull(message = "La prima es obligatoria")
    @DecimalMin(value = "0.01", message = "La prima debe ser mayor a 0")
    @Digits(integer = 8, fraction = 2, message = "La prima debe tener máximo 8 dígitos enteros y 2 decimales")
    private BigDecimal prima;
    
    @NotBlank(message = "El tipo de seguro es obligatorio")
    @Size(max = 100, message = "El tipo de seguro no puede exceder 100 caracteres")
    private String tipoSeguro;
    
    @Size(max = 1000, message = "Las observaciones no pueden exceder 1000 caracteres")
    private String observaciones;
    
    @Size(max = 100, message = "La marca no puede exceder 100 caracteres")
    private String marca;
    
    @Size(max = 100, message = "El modelo no puede exceder 100 caracteres")
    private String modelo;
    
    private String anioVehiculo; // Formato: YYYY-MM-DD
    
    // Constructor por defecto
    public PolizaRequest() {}
    
    // Constructor completo
    public PolizaRequest(String numeroPoliza, String fechaEmision, String fechaVencimiento, 
                        String estado, String clienteId, String agenteId, 
                        BigDecimal prima, String tipoSeguro, String observaciones,
                        String marca, String modelo, String anioVehiculo) {
        this.numeroPoliza = numeroPoliza;
        this.fechaEmision = fechaEmision;
        this.fechaVencimiento = fechaVencimiento;
        this.estado = estado;
        this.clienteId = clienteId;
        this.agenteId = agenteId;
        this.prima = prima;
        this.tipoSeguro = tipoSeguro;
        this.observaciones = observaciones;
        this.marca = marca;
        this.modelo = modelo;
        this.anioVehiculo = anioVehiculo;
    }
    
    // Getters y Setters
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
