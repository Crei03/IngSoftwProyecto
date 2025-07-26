package com.seguratuauto.service.impl;

import com.seguratuauto.dao.AgenteRepository;
import com.seguratuauto.dao.ClienteRepository;
import com.seguratuauto.dao.PolizaRepository;
import com.seguratuauto.model.Cliente;
import com.seguratuauto.model.EstadoPoliza;
import com.seguratuauto.model.Poliza;
import com.seguratuauto.service.PolizaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Implementación del servicio de pólizas usando Spring Data JPA
 */
@Service
@Transactional
public class PolizaServiceImpl implements PolizaService {
    
    private final PolizaRepository polizaRepository;
    private final ClienteRepository clienteRepository;
    private final AgenteRepository agenteRepository;
    
    @Autowired
    public PolizaServiceImpl(PolizaRepository polizaRepository, 
                           ClienteRepository clienteRepository, 
                           AgenteRepository agenteRepository) {
        this.polizaRepository = polizaRepository;
        this.clienteRepository = clienteRepository;
        this.agenteRepository = agenteRepository;
    }
    
    @Override
    public Poliza crearPoliza(Cliente cliente, Poliza polizaDatos) {
        // Validar que el cliente existe
        if (cliente == null || cliente.getIdCliente() == null) {
            throw new IllegalArgumentException("Cliente no válido");
        }
        
        // Verificar que el cliente existe en la base de datos
        if (!clienteRepository.existsById(cliente.getIdCliente())) {
            throw new IllegalArgumentException("El cliente especificado no existe");
        }
        
        // Verificar que el agente existe
        if (polizaDatos.getAgenteId() == null || !agenteRepository.existsById(polizaDatos.getAgenteId())) {
            throw new IllegalArgumentException("El agente especificado no existe");
        }
        
        // Validar los datos de la póliza
        if (!validarPoliza(polizaDatos)) {
            throw new IllegalArgumentException("Los datos de la póliza no son válidos");
        }
        
        // Asignar ID si no tiene
        if (polizaDatos.getIdPoliza() == null) {
            polizaDatos.setIdPoliza(UUID.randomUUID());
        }
        
        // Asignar cliente ID
        polizaDatos.setClienteId(cliente.getIdCliente());
        
        // Generar número de póliza si no tiene
        if (polizaDatos.getNumeroPoliza() == null || polizaDatos.getNumeroPoliza().isEmpty()) {
            polizaDatos.setNumeroPoliza(generarSiguienteNumeroPoliza());
        }
        
        // Establecer fecha de emisión actual si no tiene
        if (polizaDatos.getFechaEmision() == null) {
            polizaDatos.setFechaEmision(LocalDateTime.now());
        }
        
        // Establecer estado inicial si no tiene
        if (polizaDatos.getEstado() == null) {
            polizaDatos.setEstado(EstadoPoliza.PENDIENTE);
        }
        
        return polizaRepository.save(polizaDatos);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Poliza buscarPolizaPorId(UUID id) {
        return polizaRepository.findById(id).orElse(null);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Poliza buscarPolizaPorNumero(String numeroPoliza) {
        return polizaRepository.findByNumeroPoliza(numeroPoliza).orElse(null);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Poliza> obtenerPolizasPorCliente(UUID clienteId) {
        return polizaRepository.findByClienteId(clienteId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Poliza> obtenerPolizasPorAgente(UUID agenteId) {
        return polizaRepository.findByAgenteId(agenteId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Poliza> obtenerPolizasPorEstado(EstadoPoliza estado) {
        return polizaRepository.findByEstado(estado);
    }
    
    @Override
    public Poliza aprobarPoliza(UUID polizaId) {
        Poliza poliza = polizaRepository.findById(polizaId)
                .orElseThrow(() -> new IllegalArgumentException("Póliza no encontrada"));
        
        if (poliza.getEstado() != EstadoPoliza.PENDIENTE) {
            throw new IllegalStateException("Solo se pueden aprobar pólizas en estado PENDIENTE");
        }
        
        poliza.setEstado(EstadoPoliza.APROBADA);
        return polizaRepository.save(poliza);
    }
    
    @Override
    public Poliza rechazarPoliza(UUID polizaId, String motivo) {
        Poliza poliza = polizaRepository.findById(polizaId)
                .orElseThrow(() -> new IllegalArgumentException("Póliza no encontrada"));
        
        if (poliza.getEstado() != EstadoPoliza.PENDIENTE) {
            throw new IllegalStateException("Solo se pueden rechazar pólizas en estado PENDIENTE");
        }
        
        poliza.setEstado(EstadoPoliza.RECHAZADA);
        if (motivo != null && !motivo.trim().isEmpty()) {
            poliza.setObservaciones(motivo);
        }
        
        return polizaRepository.save(poliza);
    }
    
    @Override
    public Poliza cancelarPoliza(UUID polizaId, String motivo) {
        Poliza poliza = polizaRepository.findById(polizaId)
                .orElseThrow(() -> new IllegalArgumentException("Póliza no encontrada"));
        
        if (poliza.getEstado() == EstadoPoliza.CANCELADA) {
            throw new IllegalStateException("La póliza ya está cancelada");
        }
        
        poliza.setEstado(EstadoPoliza.CANCELADA);
        if (motivo != null && !motivo.trim().isEmpty()) {
            poliza.setObservaciones(motivo);
        }
        
        return polizaRepository.save(poliza);
    }
    
    @Override
    public Poliza actualizarPoliza(Poliza poliza) {
        if (poliza.getIdPoliza() == null) {
            throw new IllegalArgumentException("La póliza debe tener un ID para ser actualizada");
        }
        
        if (!polizaRepository.existsById(poliza.getIdPoliza())) {
            throw new IllegalArgumentException("La póliza especificada no existe");
        }
        
        if (!validarPoliza(poliza)) {
            throw new IllegalArgumentException("Los datos de la póliza no son válidos");
        }
        
        return polizaRepository.save(poliza);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Poliza> obtenerPolizasPorRangoFechas(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        return polizaRepository.findByFechaEmisionBetween(fechaInicio, fechaFin);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Poliza> obtenerTodasLasPolizas() {
        return polizaRepository.findAll();
    }
    
    @Override
    public boolean validarPoliza(Poliza poliza) {
        if (poliza == null) {
            return false;
        }
        
        // Validar prima
        if (poliza.getPrima() != null && poliza.getPrima().compareTo(BigDecimal.ZERO) <= 0) {
            return false;
        }
        
        // Validar fechas
        if (poliza.getFechaVencimiento() != null && poliza.getFechaEmision() != null) {
            if (poliza.getFechaVencimiento().isBefore(poliza.getFechaEmision())) {
                return false;
            }
        }
        
        // Validar que tenga cliente y agente
        if (poliza.getClienteId() == null || poliza.getAgenteId() == null) {
            return false;
        }
        
        return true;
    }
    
    @Override
    public String generarSiguienteNumeroPoliza() {
        // Buscar el último número de póliza
        String ultimoNumero = polizaRepository.findLastNumeroPoliza().orElse("POL-000000");
        
        // Extraer el número secuencial
        String prefijo = "POL-";
        int siguienteNumero = 1;
        
        if (ultimoNumero.startsWith(prefijo)) {
            try {
                String numeroStr = ultimoNumero.substring(prefijo.length());
                int numeroActual = Integer.parseInt(numeroStr);
                siguienteNumero = numeroActual + 1;
            } catch (NumberFormatException e) {
                // Si hay error, comenzar desde 1
                siguienteNumero = 1;
            }
        }
        
        // Formatear con ceros a la izquierda
        return String.format("%s%06d", prefijo, siguienteNumero);
    }
}
