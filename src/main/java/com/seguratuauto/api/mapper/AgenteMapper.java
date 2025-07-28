package com.seguratuauto.api.mapper;

import com.seguratuauto.api.dto.AgenteRequest;
import com.seguratuauto.api.dto.AgenteResponse;
import com.seguratuauto.model.Agente;
import com.seguratuauto.service.AgenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Random;


/**
 * Mapper para convertir entre entidades Agente y DTOs
 */
@Component
public class AgenteMapper {
    
    private static final Random random = new Random();
    private final AgenteService agenteService;
    
    @Autowired
    public AgenteMapper(AgenteService agenteService) {
        this.agenteService = agenteService;
    }
    
    /**
     * Convierte una entidad Agente a AgenteResponse
     * @param agente La entidad agente
     * @return AgenteResponse con los datos del agente
     */
    public AgenteResponse toResponse(Agente agente) {
        if (agente == null) {
            return null;
        }
        
        // Obtener la cantidad de pólizas del agente
        long cantidadPolizas = agenteService.contarPolizasPorAgente(agente.getIdAgente());
        
        return new AgenteResponse(
            agente.getIdAgente() != null ? agente.getIdAgente().toString() : null,
            agente.getNombre(),
            agente.getCodigo(),
            agente.getEmail(),
            agente.getTelefono(),
            String.valueOf(cantidadPolizas)
        );
    }
    
    /**
     * Convierte un AgenteRequest a entidad Agente
     * @param request El DTO de request
     * @return Nueva entidad Agente
     */
    public Agente toEntity(AgenteRequest request) {
        if (request == null) {
            return null;
        }
        
        Agente agente = new Agente();
        // Removed manual ID assignment - let JPA handle it
        agente.setNombre(request.getNombre());
        agente.setCodigo(request.getCodigo());
        agente.setEmail(request.getEmail());
        agente.setTelefono(request.getTelefono());
        
        return agente;
    }
    
    /**
     * Actualiza una entidad Agente existente con datos de AgenteRequest
     * @param agente La entidad existente a actualizar
     * @param request El DTO con los nuevos datos
     */
    public void updateEntity(Agente agente, AgenteRequest request) {
        if (agente == null || request == null) {
            return;
        }
        
        agente.setNombre(request.getNombre());
        
        // Solo actualizar código si se proporciona
        if (request.getCodigo() != null && !request.getCodigo().trim().isEmpty()) {
            agente.setCodigo(request.getCodigo());
        }
        
        agente.setEmail(request.getEmail());
        agente.setTelefono(request.getTelefono());
    }
    
    /**
     * Convierte una entidad Agente a AgenteResponse sin consultar pólizas
     * (versión optimizada para listados)
     * @param agente La entidad agente
     * @return AgenteResponse con los datos básicos del agente
     */
    public AgenteResponse toResponseBasic(Agente agente) {
        if (agente == null) {
            return null;
        }
        
        return new AgenteResponse(
            agente.getIdAgente() != null ? agente.getIdAgente().toString() : null,
            agente.getNombre(),
            agente.getCodigo(),
            agente.getEmail(),
            agente.getTelefono(),
            "0" // Sin consultar pólizas para optimizar listados
        );
    }
}
