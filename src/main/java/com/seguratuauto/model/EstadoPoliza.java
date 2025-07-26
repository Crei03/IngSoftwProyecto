package com.seguratuauto.model;

/**
 * Enum que representa los estados posibles de una póliza
 */
public enum EstadoPoliza {
    PENDIENTE("PENDIENTE"),
    APROBADA("APROBADA"),
    RECHAZADA("RECHAZADA"),
    CANCELADA("CANCELADA");
    
    private final String estado;
    
    EstadoPoliza(String estado) {
        this.estado = estado;
    }
    
    public String getEstado() {
        return estado;
    }
    
    public static EstadoPoliza fromString(String estado) {
        for (EstadoPoliza e : EstadoPoliza.values()) {
            if (e.estado.equalsIgnoreCase(estado)) {
                return e;
            }
        }
        throw new IllegalArgumentException("Estado no válido: " + estado);
    }
    
    @Override
    public String toString() {
        return estado;
    }
}
