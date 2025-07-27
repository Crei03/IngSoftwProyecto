package com.seguratuauto.model;

/**
 * Enum para los estados del workflow de reclamaciones
 */
public enum EstadoReclamacion {
    REGISTRADA("Reclamación registrada en el sistema"),
    EN_EVALUACION("Reclamación en proceso de evaluación"),
    APROBADA("Reclamación aprobada para pago"),
    RECHAZADA("Reclamación rechazada"),
    PAGADA("Reclamación pagada al cliente");
    
    private final String descripcion;
    
    EstadoReclamacion(String descripcion) {
        this.descripcion = descripcion;
    }
    
    public String getDescripcion() {
        return descripcion;
    }
    
    /**
     * Verifica si la reclamación puede cambiar al estado destino
     */
    public boolean puedeTransicionarA(EstadoReclamacion estadoDestino) {
        switch (this) {
            case REGISTRADA:
                return estadoDestino == EN_EVALUACION || estadoDestino == RECHAZADA;
            case EN_EVALUACION:
                return estadoDestino == APROBADA || estadoDestino == RECHAZADA;
            case APROBADA:
                return estadoDestino == PAGADA;
            case RECHAZADA:
            case PAGADA:
                return false; // Estados finales
            default:
                return false;
        }
    }
    
    /**
     * Verifica si es un estado final (no permite más transiciones)
     */
    public boolean esFinal() {
        return this == RECHAZADA || this == PAGADA;
    }
}
