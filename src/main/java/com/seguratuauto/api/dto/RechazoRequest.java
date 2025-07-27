package com.seguratuauto.api.dto;

/**
 * DTO para solicitudes de rechazo de reclamaciones
 */
public class RechazoRequest {
    
    private String motivo;
    private String evaluador;
    
    // Constructor por defecto
    public RechazoRequest() {}
    
    // Constructor completo
    public RechazoRequest(String motivo, String evaluador) {
        this.motivo = motivo;
        this.evaluador = evaluador;
    }
    
    // Getters y Setters
    public String getMotivo() {
        return motivo;
    }
    
    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }
    
    public String getEvaluador() {
        return evaluador;
    }
    
    public void setEvaluador(String evaluador) {
        this.evaluador = evaluador;
    }
}
