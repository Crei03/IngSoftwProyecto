package com.seguratuauto.api.dto;

import java.math.BigDecimal;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.DecimalMin;

/**
 * DTO para solicitudes de registro de reclamaciones
 */
public class ReclamacionRequest {
    
    @NotNull(message = "ID de póliza es requerido")
    private Long polizaId;
    
    @NotBlank(message = "Descripción es requerida")
    private String descripcion;
    
    @NotNull(message = "Monto reclamado es requerido")
    @DecimalMin(value = "0.01", message = "Monto reclamado debe ser mayor a cero")
    private BigDecimal montoReclamado;
    
    // Constructor por defecto
    public ReclamacionRequest() {}
    
    // Constructor completo
    public ReclamacionRequest(Long polizaId, String descripcion, BigDecimal montoReclamado) {
        this.polizaId = polizaId;
        this.descripcion = descripcion;
        this.montoReclamado = montoReclamado;
    }
    
    // Getters y Setters
    public Long getPolizaId() {
        return polizaId;
    }
    
    public void setPolizaId(Long polizaId) {
        this.polizaId = polizaId;
    }
    
    public String getDescripcion() {
        return descripcion;
    }
    
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    
    public BigDecimal getMontoReclamado() {
        return montoReclamado;
    }
    
    public void setMontoReclamado(BigDecimal montoReclamado) {
        this.montoReclamado = montoReclamado;
    }
}
