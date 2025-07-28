package com.seguratuauto.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Entidad Reclamacion para el manejo de reclamaciones de seguros
 */
@Entity
@Table(name = "reclamaciones")
public class Reclamacion {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_reclamacion")
    private Long idReclamacion;
    
    @Column(name = "poliza_id")
    private Long polizaId;
    
    @Column(name = "numero_reclamacion")
    private String numeroReclamacion;
    
    @Column(name = "descripcion")
    private String descripcion;
    
    @Column(name = "monto_reclamado")
    private BigDecimal montoReclamado;
    
    @Column(name = "monto_aprobado")
    private BigDecimal montoAprobado;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "estado")
    private EstadoReclamacion estado;
    
    @Column(name = "fecha_reclamacion")
    private LocalDateTime fechaReclamacion;
    
    @Column(name = "fecha_evaluacion")
    private LocalDateTime fechaEvaluacion;
    
    @Column(name = "fecha_resolucion")
    private LocalDateTime fechaResolucion;
    
    @Column(name = "observaciones")
    private String observaciones;
    
    @Column(name = "evaluador_id")
    private String evaluador;
    
    // Constructor por defecto
    public Reclamacion() {}
    
    // Constructor para nueva reclamación
    public Reclamacion(Long polizaId, String descripcion, BigDecimal montoReclamado) {
        this.polizaId = polizaId;
        this.descripcion = descripcion;
        this.montoReclamado = montoReclamado;
        this.estado = EstadoReclamacion.REGISTRADA;
        this.fechaReclamacion = LocalDateTime.now();
    }
    
    // Constructor completo
    public Reclamacion(Long idReclamacion, Long polizaId, String numeroReclamacion, String descripcion,
                      BigDecimal montoReclamado, BigDecimal montoAprobado, EstadoReclamacion estado,
                      LocalDateTime fechaReclamacion, LocalDateTime fechaEvaluacion, LocalDateTime fechaResolucion,
                      String observaciones, String evaluador) {
        this.idReclamacion = idReclamacion;
        this.polizaId = polizaId;
        this.numeroReclamacion = numeroReclamacion;
        this.descripcion = descripcion;
        this.montoReclamado = montoReclamado;
        this.montoAprobado = montoAprobado;
        this.estado = estado;
        this.fechaReclamacion = fechaReclamacion;
        this.fechaEvaluacion = fechaEvaluacion;
        this.fechaResolucion = fechaResolucion;
        this.observaciones = observaciones;
        this.evaluador = evaluador;
    }
    
    // Getters y Setters
    public Long getIdReclamacion() {
        return idReclamacion;
    }
    
    public void setIdReclamacion(Long idReclamacion) {
        this.idReclamacion = idReclamacion;
    }
    
    public Long getPolizaId() {
        return polizaId;
    }
    
    public void setPolizaId(Long polizaId) {
        this.polizaId = polizaId;
    }
    
    public String getNumeroReclamacion() {
        return numeroReclamacion;
    }
    
    public void setNumeroReclamacion(String numeroReclamacion) {
        this.numeroReclamacion = numeroReclamacion;
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
    
    public BigDecimal getMontoAprobado() {
        return montoAprobado;
    }
    
    public void setMontoAprobado(BigDecimal montoAprobado) {
        this.montoAprobado = montoAprobado;
    }
    
    public EstadoReclamacion getEstado() {
        return estado;
    }
    
    public void setEstado(EstadoReclamacion estado) {
        this.estado = estado;
    }
    
    public LocalDateTime getFechaReclamacion() {
        return fechaReclamacion;
    }
    
    public void setFechaReclamacion(LocalDateTime fechaReclamacion) {
        this.fechaReclamacion = fechaReclamacion;
    }
    
    public LocalDateTime getFechaEvaluacion() {
        return fechaEvaluacion;
    }
    
    public void setFechaEvaluacion(LocalDateTime fechaEvaluacion) {
        this.fechaEvaluacion = fechaEvaluacion;
    }
    
    public LocalDateTime getFechaResolucion() {
        return fechaResolucion;
    }
    
    public void setFechaResolucion(LocalDateTime fechaResolucion) {
        this.fechaResolucion = fechaResolucion;
    }
    
    public String getEvaluador() {
        return evaluador;
    }
    
    public void setEvaluador(String evaluador) {
        this.evaluador = evaluador;
    }
    
    public String getObservaciones() {
        return observaciones;
    }
    
    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
    
    // Métodos de negocio
    
    /**
     * Verifica si la reclamación puede cambiar de estado
     */
    public boolean puedeTransicionarA(EstadoReclamacion nuevoEstado) {
        return this.estado.puedeTransicionarA(nuevoEstado);
    }
    
    /**
     * Verifica si la reclamación está en estado final
     */
    public boolean esFinal() {
        return this.estado.esFinal();
    }
    
    /**
     * Calcula el porcentaje de aprobación respecto al monto reclamado
     */
    public BigDecimal getPorcentajeAprobacion() {
        if (montoReclamado == null || montoReclamado.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        if (montoAprobado == null) {
            return BigDecimal.ZERO;
        }
        return montoAprobado.divide(montoReclamado, 4, BigDecimal.ROUND_HALF_UP)
                          .multiply(new BigDecimal("100"));
    }
    
    // Métodos equals, hashCode y toString
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reclamacion that = (Reclamacion) o;
        return Objects.equals(idReclamacion, that.idReclamacion);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(idReclamacion);
    }
    
    @Override
    public String toString() {
        return "Reclamacion{" +
                "idReclamacion=" + idReclamacion +
                ", numeroReclamacion='" + numeroReclamacion + '\'' +
                ", estado=" + estado +
                ", montoReclamado=" + montoReclamado +
                ", montoAprobado=" + montoAprobado +
                ", fechaReclamacion=" + fechaReclamacion +
                '}';
    }
}
