package com.seguratuauto.api.dto;

import java.math.BigDecimal;
import java.util.Map;
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
    private Long clienteId;
    
    private Map<String, Object> factoresRiesgo;
    
    // Constructor por defecto
    public CotizacionRequest() {}
    
    // Constructor completo
    public CotizacionRequest(String tipoSeguro, Long clienteId, Map<String, Object> factoresRiesgo) {
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
    
    public Long getClienteId() {
        return clienteId;
    }
    
    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }
    
    public Map<String, Object> getFactoresRiesgo() {
        return factoresRiesgo;
    }
    
    public void setFactoresRiesgo(Map<String, Object> factoresRiesgo) {
        this.factoresRiesgo = factoresRiesgo;
    }
}
