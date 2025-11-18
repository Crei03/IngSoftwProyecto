package com.seguratuauto.api.mapper;

import org.springframework.stereotype.Component;
import com.seguratuauto.api.dto.AgenteRequest;
import com.seguratuauto.model.Agente;

@Component
public class AgenteMapper {
    
    public Agente toEntity(AgenteRequest request) {
        if (request == null) {
            return null;
        }
        Agente agente = new Agente();
        agente.setNombre(request.getNombre());
        agente.setCorreo(request.getCorreo());
        agente.setTelefono(request.getTelefono());
        agente.setCodigo(request.getCodigo());
        agente.setActivo(request.isActivo());
        return agente;
    }

    public Agente updateEntity(AgenteRequest request, Agente agente) {
        if (request == null) {
            return agente;
        }
        agente.setNombre(request.getNombre());
        agente.setCorreo(request.getCorreo());
        agente.setTelefono(request.getTelefono());
        agente.setCodigo(request.getCodigo());
        agente.setActivo(request.isActivo());
        return agente;
    }
}
