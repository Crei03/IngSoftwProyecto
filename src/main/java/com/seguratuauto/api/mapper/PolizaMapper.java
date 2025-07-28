package com.seguratuauto.api.mapper;

import com.seguratuauto.api.dto.AgenteResponse;
import com.seguratuauto.api.dto.ClienteResponse;
import com.seguratuauto.api.dto.PolizaRequest;
import com.seguratuauto.api.dto.PolizaResponse;
import com.seguratuauto.dao.AgenteRepository;
import com.seguratuauto.dao.ClienteRepository;
import com.seguratuauto.model.Agente;
import com.seguratuauto.model.Cliente;
import com.seguratuauto.model.EstadoPoliza;
import com.seguratuauto.model.Poliza;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Mapper para convertir entre entidades Poliza y DTOs
 */
@Component
public class PolizaMapper {
    
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    private static final DateTimeFormatter DATE_ONLY_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;
    
    private final ClienteRepository clienteRepository;
    private final AgenteRepository agenteRepository;
    
    @Autowired
    public PolizaMapper(ClienteRepository clienteRepository, AgenteRepository agenteRepository) {
        this.clienteRepository = clienteRepository;
        this.agenteRepository = agenteRepository;
    }
    
    /**
     * Convierte una entidad Cliente a ClienteResponse
     */
    private ClienteResponse toClienteResponse(Cliente cliente) {
        if (cliente == null) {
            return null;
        }
        return new ClienteResponse(
            cliente.getIdCliente() != null ? cliente.getIdCliente().toString() : null,
            cliente.getNombre(),
            cliente.getEmail(),
            cliente.getTelefono()
        );
    }
    
    /**
     * Convierte una entidad Agente a AgenteResponse
     */
    private AgenteResponse toAgenteResponse(Agente agente) {
        if (agente == null) {
            return null;
        }
        return new AgenteResponse(
            agente.getIdAgente() != null ? agente.getIdAgente().toString() : null,
            agente.getNombre(),
            agente.getCodigo(),
            agente.getEmail(),
            agente.getTelefono()
        );
    }
    
    /**
     * Convierte una fecha string a LocalDate soportando múltiples formatos
     */
    private LocalDate parseDate(String dateString) {
        if (dateString == null || dateString.trim().isEmpty()) {
            return null;
        }
        
        try {
            // Intentar con formato ISO_LOCAL_DATE (YYYY-MM-DD)
            return LocalDate.parse(dateString, DATE_ONLY_FORMATTER);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Formato de fecha no válido: " + dateString + 
                ". Use formato YYYY-MM-DD");
        }
    }
    
    /**
     * Convierte una fecha string a LocalDateTime soportando múltiples formatos
     */
    private LocalDateTime parseDateTime(String dateString) {
        if (dateString == null || dateString.trim().isEmpty()) {
            return null;
        }
        
        try {
            // Primero intentar con formato completo ISO_LOCAL_DATE_TIME
            return LocalDateTime.parse(dateString, DATE_FORMATTER);
        } catch (DateTimeParseException e1) {
            try {
                // Si falla, intentar con formato de solo fecha ISO_LOCAL_DATE
                LocalDate date = LocalDate.parse(dateString, DATE_ONLY_FORMATTER);
                // Convertir a LocalDateTime agregando hora 00:00:00
                return date.atStartOfDay();
            } catch (DateTimeParseException e2) {
                throw new IllegalArgumentException("Formato de fecha no válido: " + dateString + 
                    ". Use formato YYYY-MM-DD o YYYY-MM-DDTHH:MM:SS");
            }
        }
    }
    
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
        
        // Cargar datos de Cliente
        if (poliza.getClienteId() != null) {
            Cliente cliente = clienteRepository.findById(poliza.getClienteId()).orElse(null);
            response.setCliente(toClienteResponse(cliente));
        }
        
        // Cargar datos de Agente
        if (poliza.getAgenteId() != null) {
            Agente agente = agenteRepository.findById(poliza.getAgenteId()).orElse(null);
            response.setAgente(toAgenteResponse(agente));
        }
        
        response.setPrima(poliza.getPrima());
        response.setTipoSeguro(poliza.getTipoSeguro());
        response.setObservaciones(poliza.getObservaciones());
        response.setMarca(poliza.getMarca());
        response.setModelo(poliza.getModelo());
        response.setAnioVehiculo(poliza.getAnioVehiculo() != null ? 
                poliza.getAnioVehiculo().format(DATE_ONLY_FORMATTER) : null);
        
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
            poliza.setFechaEmision(parseDateTime(request.getFechaEmision()));
        }
        
        if (request.getFechaVencimiento() != null && !request.getFechaVencimiento().trim().isEmpty()) {
            poliza.setFechaVencimiento(parseDateTime(request.getFechaVencimiento()));
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
        poliza.setMarca(request.getMarca());
        poliza.setModelo(request.getModelo());
        
        // Convertir año del vehículo
        if (request.getAnioVehiculo() != null && !request.getAnioVehiculo().trim().isEmpty()) {
            poliza.setAnioVehiculo(parseDate(request.getAnioVehiculo()));
        }
        
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
        
        if (request.getMarca() != null) {
            poliza.setMarca(request.getMarca());
        }
        
        if (request.getModelo() != null) {
            poliza.setModelo(request.getModelo());
        }
        
        if (request.getAnioVehiculo() != null && !request.getAnioVehiculo().trim().isEmpty()) {
            poliza.setAnioVehiculo(parseDate(request.getAnioVehiculo()));
        }
    }
}
