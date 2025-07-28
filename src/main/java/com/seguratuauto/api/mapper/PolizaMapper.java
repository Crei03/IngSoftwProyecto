package com.seguratuauto.api.mapper;

import com.seguratuauto.api.dto.PolizaRequest;
import com.seguratuauto.api.dto.PolizaResponse;
import com.seguratuauto.model.EstadoPoliza;
import com.seguratuauto.model.Poliza;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Mapper para convertir entre entidades Poliza y DTOs
 */
@Component
public class PolizaMapper {
    
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    
    /**
     * Convierte una entidad Poliza a PolizaResponse
     */
    public PolizaResponse toResponse(Poliza poliza) {
        if (poliza == null) {
            return null;
        }
        
        PolizaResponse response = new PolizaResponse();
        response.setIdPoliza(poliza.getIdPoliza() != null ? poliza.getIdPoliza().toString() : null);
        response.setNumeroPoliza(poliza.getNumeroPoliza());
        response.setFechaEmision(poliza.getFechaEmision() != null ? 
                poliza.getFechaEmision().format(DATE_FORMATTER) : null);
        response.setFechaVencimiento(poliza.getFechaVencimiento() != null ? 
                poliza.getFechaVencimiento().format(DATE_FORMATTER) : null);
        response.setEstado(poliza.getEstado() != null ? poliza.getEstado().name() : null);
        response.setClienteId(poliza.getClienteId() != null ? poliza.getClienteId().toString() : null);
        response.setAgenteId(poliza.getAgenteId() != null ? poliza.getAgenteId().toString() : null);
        response.setPrima(poliza.getPrima());
        response.setTipoSeguro(poliza.getTipoSeguro());
        response.setObservaciones(poliza.getObservaciones());
        
        return response;
    }
    
    /**
     * Convierte un PolizaRequest a entidad Poliza
     */
    public Poliza toEntity(PolizaRequest request) {
        if (request == null) {
            return null;
        }
        
        Poliza poliza = new Poliza();
        
        // No asignar ID aquí, se genera en el service
        poliza.setNumeroPoliza(request.getNumeroPoliza());
        
        // Convertir fechas
        if (request.getFechaEmision() != null && !request.getFechaEmision().trim().isEmpty()) {
            poliza.setFechaEmision(LocalDateTime.parse(request.getFechaEmision(), DATE_FORMATTER));
        }
        
        if (request.getFechaVencimiento() != null && !request.getFechaVencimiento().trim().isEmpty()) {
            poliza.setFechaVencimiento(LocalDateTime.parse(request.getFechaVencimiento(), DATE_FORMATTER));
        }
        
        // Convertir estado
        if (request.getEstado() != null && !request.getEstado().trim().isEmpty()) {
            try {
                poliza.setEstado(EstadoPoliza.valueOf(request.getEstado().toUpperCase()));
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Estado de póliza no válido: " + request.getEstado());
            }
        }
        
        // Convertir Longs
        if (request.getClienteId() != null && !request.getClienteId().trim().isEmpty()) {
            try {
                poliza.setClienteId(Long.parseLong(request.getClienteId()));
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("ID de cliente no válido: " + request.getClienteId());
            }
        }
        
        if (request.getAgenteId() != null && !request.getAgenteId().trim().isEmpty()) {
            try {
                poliza.setAgenteId(Long.parseLong(request.getAgenteId()));
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("ID de agente no válido: " + request.getAgenteId());
            }
        }
        
        poliza.setPrima(request.getPrima());
        poliza.setTipoSeguro(request.getTipoSeguro());
        poliza.setObservaciones(request.getObservaciones());
        
        return poliza;
    }
    
    /**
     * Actualiza una entidad Poliza existente con datos de PolizaRequest
     */
    public void updateEntity(Poliza poliza, PolizaRequest request) {
        if (poliza == null || request == null) {
            return;
        }
        
        if (request.getNumeroPoliza() != null) {
            poliza.setNumeroPoliza(request.getNumeroPoliza());
        }
        
        if (request.getFechaEmision() != null && !request.getFechaEmision().trim().isEmpty()) {
            poliza.setFechaEmision(LocalDateTime.parse(request.getFechaEmision(), DATE_FORMATTER));
        }
        
        if (request.getFechaVencimiento() != null && !request.getFechaVencimiento().trim().isEmpty()) {
            poliza.setFechaVencimiento(LocalDateTime.parse(request.getFechaVencimiento(), DATE_FORMATTER));
        }
        
        if (request.getEstado() != null && !request.getEstado().trim().isEmpty()) {
            poliza.setEstado(EstadoPoliza.valueOf(request.getEstado().toUpperCase()));
        }
        
        if (request.getClienteId() != null && !request.getClienteId().trim().isEmpty()) {
            poliza.setClienteId(Long.parseLong(request.getClienteId()));
        }
        
        if (request.getAgenteId() != null && !request.getAgenteId().trim().isEmpty()) {
            poliza.setAgenteId(Long.parseLong(request.getAgenteId()));
        }
        
        if (request.getPrima() != null) {
            poliza.setPrima(request.getPrima());
        }
        
        if (request.getTipoSeguro() != null) {
            poliza.setTipoSeguro(request.getTipoSeguro());
        }
        
        if (request.getObservaciones() != null) {
            poliza.setObservaciones(request.getObservaciones());
        }
    }
}
