package com.seguratuauto.model;

import jakarta.persistence.*;

@Entity
@Table(name = "agentes")
public class Agente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_agente")
    private Long idAgente;

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Column(name = "correo", unique = true, nullable = false, length = 150)
    private String correo;

    @Column(name = "telefono", length = 20)
    private String telefono;

    @Column(name = "codigo", unique = true, length = 20)
    private String codigo;

    @Column(name = "activo", nullable = false)
    private boolean activo = true;

    @Column(name = "fecha_ingreso", nullable = false, updatable = false)
    private java.time.LocalDateTime fechaIngreso = java.time.LocalDateTime.now();

    public Agente() {
    }

    public Agente(String nombre, String correo, String telefono, String codigo) {
        this.nombre = nombre;
        this.correo = correo;
        this.telefono = telefono;
        this.codigo = codigo;
    }

    public Long getIdAgente() {
        return idAgente;
    }

    public void setIdAgente(Long idAgente) {
        this.idAgente = idAgente;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public java.time.LocalDateTime getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(java.time.LocalDateTime fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }
}
