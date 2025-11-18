package com.seguratuauto.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Entidad Cliente que representa un cliente del sistema de seguros
 */
@Entity
@Table(name = "clientes")
public class Cliente {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cliente")
    private Long idCliente;
    
    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;
    
    @Column(name = "email", length = 150)
    private String email;
    
    @Column(name = "telefono", length = 20)
    private String telefono;
    
    @Column(name = "password", nullable = false, length = 255)
    private String password;
    
    @Column(name = "verificado", nullable = false)
    private boolean verificado = false;
    
    @Column(name = "token_verificacion", length = 64, unique = true)
    private String tokenVerificacion;
    
    @Column(name = "token_expira")
    private LocalDateTime tokenExpira;
    
    @Column(name = "fecha_verificacion")
    private LocalDateTime fechaVerificacion;
    
    @Column(name = "reset_password_token", length = 64, unique = true)
    private String resetPasswordToken;
    
    @Column(name = "reset_token_expira")
    private LocalDateTime resetTokenExpira;
    
    // Constructor por defecto
    public Cliente() {}
    
    // Constructor con parámetros
    public Cliente(Long idCliente, String nombre) {
        this.idCliente = idCliente;
        this.nombre = nombre;
    }
    
    // Constructor completo
    public Cliente(Long idCliente, String nombre, String email, String telefono) {
        this.idCliente = idCliente;
        this.nombre = nombre;
        this.email = email;
        this.telefono = telefono;
    }
    
    // Getters y Setters
    public Long getIdCliente() {
        return idCliente;
    }
    
    public void setIdCliente(Long idCliente) {
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
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public boolean isVerificado() {
        return verificado;
    }
    
    public void setVerificado(boolean verificado) {
        this.verificado = verificado;
    }
    
    public String getTokenVerificacion() {
        return tokenVerificacion;
    }
    
    public void setTokenVerificacion(String tokenVerificacion) {
        this.tokenVerificacion = tokenVerificacion;
    }
    
    public LocalDateTime getTokenExpira() {
        return tokenExpira;
    }
    
    public void setTokenExpira(LocalDateTime tokenExpira) {
        this.tokenExpira = tokenExpira;
    }
    
    public LocalDateTime getFechaVerificacion() {
        return fechaVerificacion;
    }
    
    public void setFechaVerificacion(LocalDateTime fechaVerificacion) {
        this.fechaVerificacion = fechaVerificacion;
    }
    
    public String getResetPasswordToken() {
        return resetPasswordToken;
    }
    
    public void setResetPasswordToken(String resetPasswordToken) {
        this.resetPasswordToken = resetPasswordToken;
    }
    
    public LocalDateTime getResetTokenExpira() {
        return resetTokenExpira;
    }
    
    public void setResetTokenExpira(LocalDateTime resetTokenExpira) {
        this.resetTokenExpira = resetTokenExpira;
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
