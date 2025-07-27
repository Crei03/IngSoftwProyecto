package com.seguratuauto.api.dto;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.DecimalMin;

/**
 * DTO para solicitudes de cotización de precios
 */
public class CotizacionRequest {
    
    @NotBlank(message = "Tipo de seguro es requerido")
    private String tipoSeguro;
    
    @NotNull(message = "ID del cliente es requerido")
    private UUID clienteId;
    
    private Map<String, Object> factoresRiesgo;
    
    // Constructor por defecto
    public CotizacionRequest() {}
    
    // Constructor completo
    public CotizacionRequest(String tipoSeguro, UUID clienteId, Map<String, Object> factoresRiesgo) {
        this.tipoSeguro = tipoSeguro;
        this.clienteId = clienteId;
        this.factoresRiesgo = factoresRiesgo;
    }
    
    // Getters y Setters
    public String getTipoSeguro() {
        return tipoSeguro;
    }
    
    public void setTipoSeguro(String tipoSeguro) {
        this.tipoSeguro = tipoSeguro;
    }
    
    public UUID getClienteId() {
        return clienteId;
    }
    
    public void setClienteId(UUID clienteId) {
        this.clienteId = clienteId;
    }
    
    public Map<String, Object> getFactoresRiesgo() {
        return factoresRiesgo;
    }
    
    public void setFactoresRiesgo(Map<String, Object> factoresRiesgo) {
        this.factoresRiesgo = factoresRiesgo;
    }
}
