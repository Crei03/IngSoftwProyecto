package com.seguratuauto.api.dto;

/**
 * DTO para solicitudes de rechazo de reclamaciones
 */
public class RechazoRequest {
    
    private String motivo;
    private Long evaluadorId;
    
    // Constructor por defecto
    public RechazoRequest() {}
    
    // Constructor completo
    public RechazoRequest(String motivo, Long evaluadorId) {
        this.motivo = motivo;
        this.evaluadorId = evaluadorId;
    }
    
    // Getters y Setters
    public String getMotivo() {
        return motivo;
    }
    
    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }
    
    public Long getEvaluadorId() {
        return evaluadorId;
    }
    
    public void setEvaluadorId(Long evaluadorId) {
        this.evaluadorId = evaluadorId;
    }
}
