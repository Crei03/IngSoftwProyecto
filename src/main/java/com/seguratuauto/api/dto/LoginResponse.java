package com.seguratuauto.api.dto;

/**
 * DTO para devolver los datos del usuario autenticado.
 */
public class LoginResponse {
    
    private String tipoUsuario;
    private ClienteResponse cliente;
    private AgenteResponse agente;
    
    public LoginResponse() {}
    
    public LoginResponse(String tipoUsuario, ClienteResponse cliente, AgenteResponse agente) {
        this.tipoUsuario = tipoUsuario;
        this.cliente = cliente;
        this.agente = agente;
    }
    
    public String getTipoUsuario() {
        return tipoUsuario;
    }
    
    public void setTipoUsuario(String tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }
    
    public ClienteResponse getCliente() {
        return cliente;
    }
    
    public void setCliente(ClienteResponse cliente) {
        this.cliente = cliente;
    }
    
    public AgenteResponse getAgente() {
        return agente;
    }
    
    public void setAgente(AgenteResponse agente) {
        this.agente = agente;
    }
}

