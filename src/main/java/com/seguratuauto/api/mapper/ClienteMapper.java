package com.seguratuauto.api.mapper;

import org.springframework.stereotype.Component;
import com.seguratuauto.api.dto.ClienteRequest;
import com.seguratuauto.api.dto.ClienteResponse;
import com.seguratuauto.model.Cliente;

@Component
public class ClienteMapper {
    
    public Cliente toEntity(ClienteRequest request) {
        if (request == null) {
            return null;
        }
        Cliente cliente = new Cliente();
        cliente.setNombre(request.getNombre());
        cliente.setCorreo(request.getCorreo());
        cliente.setTelefono(request.getTelefono());
        cliente.setDireccion(request.getDireccion());
        cliente.setCiudad(request.getCiudad());
        cliente.setProvincia(request.getProvincia());
        cliente.setCodigoPostal(request.getCodigoPostal());
        return cliente;
    }

    public ClienteResponse toResponse(Cliente cliente) {
        if (cliente == null) {
            return null;
        }
        ClienteResponse response = new ClienteResponse();
        response.setId(cliente.getIdCliente());
        response.setNombre(cliente.getNombre());
        response.setCorreo(cliente.getCorreo());
        response.setTelefono(cliente.getTelefono());
        response.setDireccion(cliente.getDireccion());
        response.setCiudad(cliente.getCiudad());
        response.setProvincia(cliente.getProvincia());
        response.setCodigoPostal(cliente.getCodigoPostal());
        response.setActivo(cliente.isActivo());
        return response;
    }

    public Cliente updateEntity(ClienteRequest request, Cliente cliente) {
        if (request == null) {
            return cliente;
        }
        cliente.setNombre(request.getNombre());
        cliente.setCorreo(request.getCorreo());
        cliente.setTelefono(request.getTelefono());
        cliente.setDireccion(request.getDireccion());
        cliente.setCiudad(request.getCiudad());
        cliente.setProvincia(request.getProvincia());
        cliente.setCodigoPostal(request.getCodigoPostal());
        return cliente;
    }
}
