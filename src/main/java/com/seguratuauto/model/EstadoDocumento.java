package com.seguratuauto.model;

/**
 * Enum para los estados de un documento adjunto
 */
public enum EstadoDocumento {
    ACTIVO("Activo"),
    ELIMINADO("Eliminado"),
    PROCESANDO("Procesando"),
    ERROR("Error en procesamiento");
    
    private final String descripcion;
    
    EstadoDocumento(String descripcion) {
        this.descripcion = descripcion;
    }
    
    public String getDescripcion() {
        return descripcion;
    }
    
    @Override
    public String toString() {
        return descripcion;
    }
}