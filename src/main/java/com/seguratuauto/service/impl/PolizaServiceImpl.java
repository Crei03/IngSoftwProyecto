package com.seguratuauto.service.impl;

import com.seguratuauto.dao.AgenteDao;
import com.seguratuauto.dao.ClienteDao;
import com.seguratuauto.dao.PolizaDao;
import com.seguratuauto.dao.impl.JdbcAgenteDao;
import com.seguratuauto.dao.impl.JdbcClienteDao;
import com.seguratuauto.dao.impl.JdbcPolizaDao;
import com.seguratuauto.model.Cliente;
import com.seguratuauto.model.EstadoPoliza;
import com.seguratuauto.model.Poliza;
import com.seguratuauto.service.PolizaService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Implementación del servicio de pólizas
 */
public class PolizaServiceImpl implements PolizaService {
    
    private final PolizaDao polizaDao;
    private final ClienteDao clienteDao;
    private final AgenteDao agenteDao;
    
    // Constructor con inyección de dependencias manual
    public PolizaServiceImpl() {
        this.polizaDao = new JdbcPolizaDao();
        this.clienteDao = new JdbcClienteDao();
        this.agenteDao = new JdbcAgenteDao();
    }
    
    // Constructor para testing con DAOs específicos
    public PolizaServiceImpl(PolizaDao polizaDao, ClienteDao clienteDao, AgenteDao agenteDao) {
        this.polizaDao = polizaDao;
        this.clienteDao = clienteDao;
        this.agenteDao = agenteDao;
    }
    
    @Override
    public Poliza crearPoliza(Cliente cliente, Poliza polizaDatos) {
        // Validar que el cliente existe
        if (cliente == null || cliente.getIdCliente() == null) {
            throw new IllegalArgumentException("Cliente no válido");
        }
        
        // Verificar que el cliente existe en la base de datos
        if (!clienteDao.existsById(cliente.getIdCliente())) {
            throw new IllegalArgumentException("El cliente especificado no existe");
        }
        
        // Verificar que el agente existe
        if (polizaDatos.getAgenteId() == null || !agenteDao.existsById(polizaDatos.getAgenteId())) {
            throw new IllegalArgumentException("El agente especificado no existe");
        }
        
        // Validar los datos de la póliza
        if (!validarPoliza(polizaDatos)) {
            throw new IllegalArgumentException("Los datos de la póliza no son válidos");
        }
        
        // Establecer valores por defecto
        if (polizaDatos.getIdPoliza() == null) {
            polizaDatos.setIdPoliza(UUID.randomUUID());
        }
        
        if (polizaDatos.getFechaEmision() == null) {
            polizaDatos.setFechaEmision(LocalDateTime.now());
        }
        
        if (polizaDatos.getEstado() == null) {
            polizaDatos.setEstado(EstadoPoliza.PENDIENTE);
        }
        
        // Establecer el cliente
        polizaDatos.setClienteId(cliente.getIdCliente());
        
        // TODO: Aplicar reglas de negocio adicionales
        // - Calcular prima automáticamente
        // - Aplicar descuentos o recargos
        // - Validar cobertura según tipo de seguro
        
        // Crear la póliza en la base de datos
        return polizaDao.create(polizaDatos);
    }
    
    @Override
    public Poliza buscarPolizaPorId(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("ID no puede ser nulo");
        }
        
        return polizaDao.findById(id).orElse(null);
    }
    
    @Override
    public Poliza buscarPolizaPorNumero(String numeroPoliza) {
        if (numeroPoliza == null || numeroPoliza.trim().isEmpty()) {
            throw new IllegalArgumentException("Número de póliza no puede ser vacío");
        }
        
        return polizaDao.findByNumeroPoliza(numeroPoliza).orElse(null);
    }
    
    @Override
    public List<Poliza> obtenerPolizasPorCliente(UUID clienteId) {
        if (clienteId == null) {
            throw new IllegalArgumentException("ID del cliente no puede ser nulo");
        }
        
        return polizaDao.findByClienteId(clienteId);
    }
    
    @Override
    public List<Poliza> obtenerPolizasPorAgente(UUID agenteId) {
        if (agenteId == null) {
            throw new IllegalArgumentException("ID del agente no puede ser nulo");
        }
        
        return polizaDao.findByAgenteId(agenteId);
    }
    
    @Override
    public List<Poliza> obtenerPolizasPorEstado(EstadoPoliza estado) {
        if (estado == null) {
            throw new IllegalArgumentException("Estado no puede ser nulo");
        }
        
        return polizaDao.findByEstado(estado);
    }
    
    @Override
    public Poliza aprobarPoliza(UUID polizaId) {
        Poliza poliza = buscarPolizaPorId(polizaId);
        if (poliza == null) {
            throw new IllegalArgumentException("Póliza no encontrada");
        }
        
        if (poliza.getEstado() != EstadoPoliza.PENDIENTE) {
            throw new IllegalStateException("Solo se pueden aprobar pólizas en estado PENDIENTE");
        }
        
        // TODO: Implementar validaciones adicionales para aprobación
        // - Verificar documentación completa
        // - Validar límites de cobertura
        // - Confirmar capacidad financiera del cliente
        
        poliza.setEstado(EstadoPoliza.APROBADA);
        
        return polizaDao.update(poliza);
    }
    
    @Override
    public Poliza rechazarPoliza(UUID polizaId, String motivo) {
        Poliza poliza = buscarPolizaPorId(polizaId);
        if (poliza == null) {
            throw new IllegalArgumentException("Póliza no encontrada");
        }
        
        if (poliza.getEstado() != EstadoPoliza.PENDIENTE) {
            throw new IllegalStateException("Solo se pueden rechazar pólizas en estado PENDIENTE");
        }
        
        poliza.setEstado(EstadoPoliza.RECHAZADA);
        
        // Agregar el motivo a las observaciones
        String observacionesActuales = poliza.getObservaciones() != null ? poliza.getObservaciones() : "";
        String motivoRechazo = String.format("[RECHAZADA - %s] %s", 
                                           LocalDateTime.now().toString(), motivo);
        poliza.setObservaciones(observacionesActuales + "\n" + motivoRechazo);
        
        return polizaDao.update(poliza);
    }
    
    @Override
    public Poliza cancelarPoliza(UUID polizaId, String motivo) {
        Poliza poliza = buscarPolizaPorId(polizaId);
        if (poliza == null) {
            throw new IllegalArgumentException("Póliza no encontrada");
        }
        
        if (poliza.getEstado() == EstadoPoliza.CANCELADA) {
            throw new IllegalStateException("La póliza ya está cancelada");
        }
        
        poliza.setEstado(EstadoPoliza.CANCELADA);
        
        // Agregar el motivo a las observaciones
        String observacionesActuales = poliza.getObservaciones() != null ? poliza.getObservaciones() : "";
        String motivoCancelacion = String.format("[CANCELADA - %s] %s", 
                                               LocalDateTime.now().toString(), motivo);
        poliza.setObservaciones(observacionesActuales + "\n" + motivoCancelacion);
        
        return polizaDao.update(poliza);
    }
    
    @Override
    public Poliza actualizarPoliza(Poliza poliza) {
        if (poliza == null || poliza.getIdPoliza() == null) {
            throw new IllegalArgumentException("Póliza no válida para actualización");
        }
        
        if (!polizaDao.existsById(poliza.getIdPoliza())) {
            throw new IllegalArgumentException("La póliza especificada no existe");
        }
        
        if (!validarPoliza(poliza)) {
            throw new IllegalArgumentException("Los datos de la póliza no son válidos");
        }
        
        return polizaDao.update(poliza);
    }
    
    @Override
    public List<Poliza> obtenerPolizasPorRangoFechas(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        if (fechaInicio == null || fechaFin == null) {
            throw new IllegalArgumentException("Las fechas de inicio y fin no pueden ser nulas");
        }
        
        if (fechaInicio.isAfter(fechaFin)) {
            throw new IllegalArgumentException("La fecha de inicio no puede ser posterior a la fecha de fin");
        }
        
        return polizaDao.findByFechaEmisionBetween(fechaInicio, fechaFin);
    }
    
    @Override
    public List<Poliza> obtenerTodasLasPolizas() {
        return polizaDao.findAll();
    }
    
    @Override
    public boolean validarPoliza(Poliza poliza) {
        if (poliza == null) {
            return false;
        }
        
        // Validaciones básicas
        if (poliza.getClienteId() == null) {
            return false;
        }
        
        if (poliza.getAgenteId() == null) {
            return false;
        }
        
        if (poliza.getFechaEmision() == null) {
            return false;
        }
        
        // Validar que la fecha de emisión no sea futura
        if (poliza.getFechaEmision().isAfter(LocalDateTime.now().plusDays(1))) {
            return false;
        }
        
        // Validar que la fecha de vencimiento sea posterior a la emisión
        if (poliza.getFechaVencimiento() != null && 
            poliza.getFechaVencimiento().isBefore(poliza.getFechaEmision())) {
            return false;
        }
        
        // Validar prima si está especificada
        if (poliza.getPrima() != null && poliza.getPrima().signum() <= 0) {
            return false;
        }
        
        // TODO: Implementar validaciones adicionales según reglas de negocio
        // - Validar tipo de seguro permitido
        // - Verificar límites de prima según perfil del cliente
        // - Validar combinaciones de cobertura
        
        return true;
    }
}
