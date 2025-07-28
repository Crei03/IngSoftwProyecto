package com.seguratuauto.api.dto;

import java.math.BigDecimal;
import jakarta.validation.constraints.DecimalMin;

/**
 * DTO para solicitudes de evaluación de reclamaciones
 */
public class EvaluacionRequest {
    
    @DecimalMin(value = "0.0", message = "Monto aprobado no puede ser negativo")
    private BigDecimal montoAprobado;
    
    private String observaciones;
    
    private Long evaluadorId;
    
    // Constructor por defecto
    public EvaluacionRequest() {}
    
    // Constructor completo
    public EvaluacionRequest(BigDecimal montoAprobado, String observaciones, Long evaluadorId) {
        this.montoAprobado = montoAprobado;
        this.observaciones = observaciones;
        this.evaluadorId = evaluadorId;
    }
    
    // Getters y Setters
    public BigDecimal getMontoAprobado() {
        return montoAprobado;
    }
    
    public void setMontoAprobado(BigDecimal montoAprobado) {
        this.montoAprobado = montoAprobado;
    }
    
    public String getObservaciones() {
        return observaciones;
    }
    
    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
    
    public Long getEvaluadorId() {
        return evaluadorId;
    }
    
    public void setEvaluadorId(Long evaluadorId) {
        this.evaluadorId = evaluadorId;
    }
}
