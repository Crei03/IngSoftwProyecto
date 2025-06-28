package com.seguratuauto.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * Entidad Poliza que representa una póliza de seguro en el sistema
 */
public class Poliza {
    
    private UUID idPoliza;
    private LocalDateTime fechaEmision;
    private EstadoPoliza estado;
    private UUID clienteId;
    private UUID agenteId;
    private String numeroPoliza;
    private BigDecimal prima;
    private String tipoSeguro;
    private LocalDateTime fechaVencimiento;
    private String observaciones;
    
    // Constructor por defecto
    public Poliza() {}
    
    // Constructor con parámetros principales
    public Poliza(UUID idPoliza, LocalDateTime fechaEmision, EstadoPoliza estado, UUID clienteId, UUID agenteId) {
        this.idPoliza = idPoliza;
        this.fechaEmision = fechaEmision;
        this.estado = estado;
        this.clienteId = clienteId;
        this.agenteId = agenteId;
    }
    
    // Constructor completo
    public Poliza(UUID idPoliza, LocalDateTime fechaEmision, EstadoPoliza estado, 
                  UUID clienteId, UUID agenteId, String numeroPoliza, 
                  BigDecimal prima, String tipoSeguro, LocalDateTime fechaVencimiento, 
                  String observaciones) {
        this.idPoliza = idPoliza;
        this.fechaEmision = fechaEmision;
        this.estado = estado;
        this.clienteId = clienteId;
        this.agenteId = agenteId;
        this.numeroPoliza = numeroPoliza;
        this.prima = prima;
        this.tipoSeguro = tipoSeguro;
        this.fechaVencimiento = fechaVencimiento;
        this.observaciones = observaciones;
    }
    
    // Getters y Setters
    public UUID getIdPoliza() {
        return idPoliza;
    }
    
    public void setIdPoliza(UUID idPoliza) {
        this.idPoliza = idPoliza;
    }
    
    public LocalDateTime getFechaEmision() {
        return fechaEmision;
    }
    
    public void setFechaEmision(LocalDateTime fechaEmision) {
        this.fechaEmision = fechaEmision;
    }
    
    public EstadoPoliza getEstado() {
        return estado;
    }
    
    public void setEstado(EstadoPoliza estado) {
        this.estado = estado;
    }
    
    public UUID getClienteId() {
        return clienteId;
    }
    
    public void setClienteId(UUID clienteId) {
        this.clienteId = clienteId;
    }
    
    public UUID getAgenteId() {
        return agenteId;
    }
    
    public void setAgenteId(UUID agenteId) {
        this.agenteId = agenteId;
    }
    
    public String getNumeroPoliza() {
        return numeroPoliza;
    }
    
    public void setNumeroPoliza(String numeroPoliza) {
        this.numeroPoliza = numeroPoliza;
    }
    
    public BigDecimal getPrima() {
        return prima;
    }
    
    public void setPrima(BigDecimal prima) {
        this.prima = prima;
    }
    
    public String getTipoSeguro() {
        return tipoSeguro;
    }
    
    public void setTipoSeguro(String tipoSeguro) {
        this.tipoSeguro = tipoSeguro;
    }
    
    public LocalDateTime getFechaVencimiento() {
        return fechaVencimiento;
    }
    
    public void setFechaVencimiento(LocalDateTime fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }
    
    public String getObservaciones() {
        return observaciones;
    }
    
    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
    
    // Métodos equals, hashCode y toString
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Poliza poliza = (Poliza) o;
        return Objects.equals(idPoliza, poliza.idPoliza);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(idPoliza);
    }
    
    @Override
    public String toString() {
        return "Poliza{" +
                "idPoliza=" + idPoliza +
                ", fechaEmision=" + fechaEmision +
                ", estado=" + estado +
                ", clienteId=" + clienteId +
                ", agenteId=" + agenteId +
                ", numeroPoliza='" + numeroPoliza + '\'' +
                ", prima=" + prima +
                ", tipoSeguro='" + tipoSeguro + '\'' +
                ", fechaVencimiento=" + fechaVencimiento +
                ", observaciones='" + observaciones + '\'' +
                '}';
    }
}
