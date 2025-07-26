package com.seguratuauto.model;

import jakarta.persistence.*;
import java.util.Objects;
import java.util.UUID;

/**
 * Entidad Cliente que representa un cliente del sistema de seguros
 */
@Entity
@Table(name = "clientes")
public class Cliente {
    
    @Id
    @Column(name = "id_cliente", columnDefinition = "BINARY(16)")
    private UUID idCliente;
    
    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;
    
    @Column(name = "email", length = 150)
    private String email;
    
    @Column(name = "telefono", length = 20)
    private String telefono;
    
    // Constructor por defecto
    public Cliente() {}
    
    // Constructor con parámetros
    public Cliente(UUID idCliente, String nombre) {
        this.idCliente = idCliente;
        this.nombre = nombre;
    }
    
    // Constructor completo
    public Cliente(UUID idCliente, String nombre, String email, String telefono) {
        this.idCliente = idCliente;
        this.nombre = nombre;
        this.email = email;
        this.telefono = telefono;
    }
    
    // Getters y Setters
    public UUID getIdCliente() {
        return idCliente;
    }
    
    public void setIdCliente(UUID idCliente) {
        this.idCliente = idCliente;
    }
    
    public String getNombre() {
        return nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
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
        Cliente cliente = (Cliente) o;
        return Objects.equals(idCliente, cliente.idCliente);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(idCliente);
    }
}
