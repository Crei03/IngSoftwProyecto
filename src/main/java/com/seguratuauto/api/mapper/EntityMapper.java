package com.seguratuauto.api.mapper;

import com.seguratuauto.api.dto.ClienteDto;
import com.seguratuauto.api.dto.PolizaDto;
import com.seguratuauto.model.Cliente;
import com.seguratuauto.model.EstadoPoliza;
import com.seguratuauto.model.Poliza;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

/**
 * Mapper para convertir entre entidades y DTOs
 */
public class EntityMapper {
    
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    
    // Mapeo de Cliente a ClienteDto
    public static ClienteDto toDto(Cliente cliente) {
        if (cliente == null) {
            return null;
        }
        
        ClienteDto dto = new ClienteDto();
        dto.setIdCliente(cliente.getIdCliente() != null ? cliente.getIdCliente().toString() : null);
        dto.setNombre(cliente.getNombre());
        dto.setEmail(cliente.getEmail());
        dto.setTelefono(cliente.getTelefono());
        
        return dto;
    }
    
    // Mapeo de ClienteDto a Cliente
    public static Cliente toEntity(ClienteDto dto) {
        if (dto == null) {
            return null;
        }
        
        Cliente cliente = new Cliente();
        if (dto.getIdCliente() != null && !dto.getIdCliente().isEmpty()) {
            cliente.setIdCliente(UUID.fromString(dto.getIdCliente()));
        }
        cliente.setNombre(dto.getNombre());
        cliente.setEmail(dto.getEmail());
        cliente.setTelefono(dto.getTelefono());
        
        return cliente;
    }
    
    // Mapeo de Poliza a PolizaDto
    public static PolizaDto toDto(Poliza poliza) {
        if (poliza == null) {
            return null;
        }
        
        PolizaDto dto = new PolizaDto();
        dto.setIdPoliza(poliza.getIdPoliza() != null ? poliza.getIdPoliza().toString() : null);
        dto.setNumeroPoliza(poliza.getNumeroPoliza());
        dto.setFechaEmision(poliza.getFechaEmision() != null ? 
                          poliza.getFechaEmision().format(DATE_FORMATTER) : null);
        dto.setFechaVencimiento(poliza.getFechaVencimiento() != null ? 
                              poliza.getFechaVencimiento().format(DATE_FORMATTER) : null);
        dto.setEstado(poliza.getEstado() != null ? poliza.getEstado().name() : null);
        dto.setClienteId(poliza.getClienteId() != null ? poliza.getClienteId().toString() : null);
        dto.setAgenteId(poliza.getAgenteId() != null ? poliza.getAgenteId().toString() : null);
        dto.setPrima(poliza.getPrima());
        dto.setTipoSeguro(poliza.getTipoSeguro());
        dto.setObservaciones(poliza.getObservaciones());
        
        return dto;
    }
    
    // Mapeo de PolizaDto a Poliza
    public static Poliza toEntity(PolizaDto dto) {
        if (dto == null) {
            return null;
        }
        
        Poliza poliza = new Poliza();
        
        if (dto.getIdPoliza() != null && !dto.getIdPoliza().isEmpty()) {
            poliza.setIdPoliza(UUID.fromString(dto.getIdPoliza()));
        }
        
        poliza.setNumeroPoliza(dto.getNumeroPoliza());
        
        if (dto.getFechaEmision() != null && !dto.getFechaEmision().isEmpty()) {
            poliza.setFechaEmision(LocalDateTime.parse(dto.getFechaEmision(), DATE_FORMATTER));
        }
        
        if (dto.getFechaVencimiento() != null && !dto.getFechaVencimiento().isEmpty()) {
            poliza.setFechaVencimiento(LocalDateTime.parse(dto.getFechaVencimiento(), DATE_FORMATTER));
        }
        
        if (dto.getEstado() != null && !dto.getEstado().isEmpty()) {
            poliza.setEstado(EstadoPoliza.valueOf(dto.getEstado()));
        }
        
        if (dto.getClienteId() != null && !dto.getClienteId().isEmpty()) {
            poliza.setClienteId(UUID.fromString(dto.getClienteId()));
        }
        
        if (dto.getAgenteId() != null && !dto.getAgenteId().isEmpty()) {
            poliza.setAgenteId(UUID.fromString(dto.getAgenteId()));
        }
        
        poliza.setPrima(dto.getPrima());
        poliza.setTipoSeguro(dto.getTipoSeguro());
        poliza.setObservaciones(dto.getObservaciones());
        
        return poliza;
    }
}
