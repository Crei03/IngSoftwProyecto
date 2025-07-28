package com.seguratuauto.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Entidad Evaluador que representa un evaluador de reclamaciones en el sistema
 */
@Entity
@Table(name = "evaluadores")
public class Evaluador {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_evaluador")
    private Long idEvaluador;
    
    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;
    
    @Column(name = "codigo", unique = true, length = 20)
    private String codigo;
    
    @Column(name = "email", length = 150)
    private String email;
    
    @Column(name = "telefono", length = 20)
    private String telefono;
    
    @Column(name = "especialidad", length = 100)
    private String especialidad;
    
    @Column(name = "activo", nullable = false)
    private Boolean activo = true;
    
    @Column(name = "fecha_ingreso")
    private LocalDateTime fechaIngreso;
    
    // Constructor por defecto
    public Evaluador() {}
    
    // Constructor con parámetros principales
    public Evaluador(Long idEvaluador, String nombre) {
        this.idEvaluador = idEvaluador;
        this.nombre = nombre;
        this.fechaIngreso = LocalDateTime.now();
        this.activo = true;
    }
    
    // Constructor completo
    public Evaluador(Long idEvaluador, String nombre, String codigo, String email, 
                    String telefono, String especialidad) {
        this.idEvaluador = idEvaluador;
        this.nombre = nombre;
        this.codigo = codigo;
        this.email = email;
        this.telefono = telefono;
        this.especialidad = especialidad;
        this.activo = true;
        this.fechaIngreso = LocalDateTime.now();
    }
    
    // Getters y Setters
    public Long getIdEvaluador() {
        return idEvaluador;
    }
    
    public void setIdEvaluador(Long idEvaluador) {
        this.idEvaluador = idEvaluador;
    }
    
    public String getNombre() {
        return nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public String getCodigo() {
        return codigo;
    }
    
    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getTelefono() {
        return telefono;
    }
    
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
    
    public String getEspecialidad() {
        return especialidad;
    }
    
    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }
    
    public Boolean getActivo() {
        return activo;
    }
    
    public void setActivo(Boolean activo) {
        this.activo = activo;
    }
    
    public LocalDateTime getFechaIngreso() {
        return fechaIngreso;
    }
    
    public void setFechaIngreso(LocalDateTime fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }
    
    // Métodos equals, hashCode y toString
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Evaluador evaluador = (Evaluador) o;
        return Objects.equals(idEvaluador, evaluador.idEvaluador);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(idEvaluador);
    }
    
    @Override
    public String toString() {
        return "Evaluador{" +
                "idEvaluador=" + idEvaluador +
                ", nombre='" + nombre + '\'' +
                ", codigo='" + codigo + '\'' +
                ", email='" + email + '\'' +
                ", telefono='" + telefono + '\'' +
                ", especialidad='" + especialidad + '\'' +
                ", activo=" + activo +
                ", fechaIngreso=" + fechaIngreso +
                '}';
    }
}
