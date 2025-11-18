package com.seguratuauto.api.dto;

public class LoginResponse {
    private Long idCliente;
    private String nombre;
    private String email;
    private String token;
    private boolean exitoso;
    private String mensaje;

    public LoginResponse() {
    }

    public LoginResponse(Long idCliente, String nombre, String email, String token, boolean exitoso, String mensaje) {
        this.idCliente = idCliente;
        this.nombre = nombre;
        this.email = email;
        this.token = token;
        this.exitoso = exitoso;
        this.mensaje = mensaje;
    }

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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isExitoso() {
        return exitoso;
    }

    public void setExitoso(boolean exitoso) {
        this.exitoso = exitoso;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
}
