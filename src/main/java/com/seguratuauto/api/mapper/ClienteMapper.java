package com.seguratuauto.api.mapper;

import com.seguratuauto.api.dto.ClienteRequest;
import com.seguratuauto.api.dto.ClienteResponse;
import com.seguratuauto.model.Cliente;
import org.springframework.stereotype.Component;


/**
 * Mapper para convertir entre entidades Cliente y DTOs
 */
@Component
public class ClienteMapper {
    
    /**
     * Convierte una entidad Cliente a ClienteResponse
     */
    public ClienteResponse toResponse(Cliente cliente) {
        if (cliente == null) {
            return null;
        }
        
        ClienteResponse response = new ClienteResponse();
        response.setIdCliente(cliente.getIdCliente() != null ? cliente.getIdCliente().toString() : null);
        response.setNombre(cliente.getNombre());
        response.setEmail(cliente.getEmail());
        response.setTelefono(cliente.getTelefono());
        
        return response;
    }
    
    /**
     * Convierte un ClienteRequest a entidad Cliente
     */
    public Cliente toEntity(ClienteRequest request) {
        if (request == null) {
            return null;
        }
        
        Cliente cliente = new Cliente();
        
        // No asignar ID aquí, se genera en el service
        cliente.setNombre(request.getNombre());
        cliente.setEmail(request.getEmail());
        cliente.setTelefono(request.getTelefono());
        
        return cliente;
    }
    
    /**
     * Actualiza una entidad Cliente existente con datos de ClienteRequest
     */
    public void updateEntity(Cliente cliente, ClienteRequest request) {
        if (cliente == null || request == null) {
            return;
        }
        
        if (request.getNombre() != null) {
            cliente.setNombre(request.getNombre());
        }
        
        if (request.getEmail() != null) {
            cliente.setEmail(request.getEmail());
        }
        
        if (request.getTelefono() != null) {
            cliente.setTelefono(request.getTelefono());
        }
    }
}
