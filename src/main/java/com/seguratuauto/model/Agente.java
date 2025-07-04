package com.seguratuauto.model;

import java.util.Objects;
import java.util.UUID;

/**
 * Entidad Agente que representa un agente de seguros en el sistema
 */
public class Agente {
    
    private UUID idAgente;
    private String nombre;
    private String codigo;
    private String email;
    private String telefono;
    
    // Constructor por defecto
    public Agente() {}
    
    // Constructor con parámetros principales
    public Agente(UUID idAgente, String nombre) {
        this.idAgente = idAgente;
        this.nombre = nombre;
    }
    
    // Constructor completo
    public Agente(UUID idAgente, String nombre, String codigo, String email, String telefono) {
        this.idAgente = idAgente;
        this.nombre = nombre;
        this.codigo = codigo;
        this.email = email;
        this.telefono = telefono;
    }
    
    // Getters y Setters
    public UUID getIdAgente() {
        return idAgente;
    }
    
    public void setIdAgente(UUID idAgente) {
        this.idAgente = idAgente;
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
    
    // Métodos equals, hashCode y toString
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Agente agente = (Agente) o;
        return Objects.equals(idAgente, agente.idAgente);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(idAgente);
    }
    
    @Override
    public String toString() {
        return "Agente{" +
                "idAgente=" + idAgente +
                ", nombre='" + nombre + '\'' +
                ", codigo='" + codigo + '\'' +
                ", email='" + email + '\'' +
                ", telefono='" + telefono + '\'' +
                '}';
    }
}
