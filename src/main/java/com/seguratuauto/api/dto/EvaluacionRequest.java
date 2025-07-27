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
    
    private String evaluador;
    
    // Constructor por defecto
    public EvaluacionRequest() {}
    
    // Constructor completo
    public EvaluacionRequest(BigDecimal montoAprobado, String observaciones, String evaluador) {
        this.montoAprobado = montoAprobado;
        this.observaciones = observaciones;
        this.evaluador = evaluador;
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
    
    public String getEvaluador() {
        return evaluador;
    }
    
    public void setEvaluador(String evaluador) {
        this.evaluador = evaluador;
    }
}
