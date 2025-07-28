package com.seguratuauto.service.impl;

import com.seguratuauto.model.Reclamacion;
import com.seguratuauto.model.EstadoReclamacion;
import com.seguratuauto.dao.ReclamacionRepository;
import com.seguratuauto.service.ReclamacionService;
import com.seguratuauto.service.strategy.EvaluacionStrategy;
import com.seguratuauto.service.strategy.EvaluacionAutomaticaStrategy;
import com.seguratuauto.service.strategy.EvaluacionManualStrategy;
import com.seguratuauto.service.strategy.EvaluacionEspecializadaStrategy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * Implementación del servicio de reclamaciones usando el patrón Strategy
 * Utiliza diferentes estrategias de evaluación según el tipo y monto de la reclamación
 */
@Service
@Transactional
public class ReclamacionServiceImpl implements ReclamacionService {
    
    private static final Random random = new Random();
    
    private final ReclamacionRepository reclamacionRepository;
    
    // Estrategias de evaluación disponibles
    private final List<EvaluacionStrategy> estrategias;
    
    @Autowired
    public ReclamacionServiceImpl(ReclamacionRepository reclamacionRepository) {
        this.reclamacionRepository = reclamacionRepository;
        this.estrategias = new ArrayList<>();
        this.estrategias.add(new EvaluacionAutomaticaStrategy());
        this.estrategias.add(new EvaluacionManualStrategy());
        this.estrategias.add(new EvaluacionEspecializadaStrategy());
    }
    
    @Override
    public Reclamacion registrarReclamacion(Long polizaId, String descripcion, BigDecimal montoReclamado) {
        if (polizaId == null) {
            throw new IllegalArgumentException("ID de póliza es requerido");
        }
        if (descripcion == null || descripcion.trim().isEmpty()) {
            throw new IllegalArgumentException("Descripción es requerida");
        }
        if (montoReclamado == null || montoReclamado.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Monto reclamado debe ser mayor a cero");
        }
        
        Reclamacion reclamacion = new Reclamacion(polizaId, descripcion, montoReclamado);
        reclamacion.setNumeroReclamacion(generarNumeroReclamacion());
        reclamacion.setFechaReclamacion(LocalDateTime.now());
        reclamacion.setEstado(EstadoReclamacion.REGISTRADA);
        
        // Guardar en la base de datos (JPA manejará el ID automáticamente)
        reclamacion = reclamacionRepository.save(reclamacion);
        
        System.out.println("Reclamación registrada: " + reclamacion.getNumeroReclamacion());
        System.out.println("Monto reclamado: $" + montoReclamado);
        
        return reclamacion;
    }
    
    @Override
    public boolean evaluarReclamacion(Long reclamacionId, BigDecimal montoAprobado, String observaciones, String evaluador) {
        Reclamacion reclamacion = buscarReclamacionPorId(reclamacionId);
        if (reclamacion == null) {
            throw new IllegalArgumentException("Reclamación no encontrada");
        }
        
        if (!reclamacion.puedeTransicionarA(EstadoReclamacion.EN_EVALUACION)) {
            throw new IllegalStateException("La reclamación no puede ser evaluada en su estado actual: " + reclamacion.getEstado());
        }
        
        // Seleccionar la estrategia apropiada
        EvaluacionStrategy estrategia = seleccionarEstrategia(reclamacion);
        if (estrategia == null) {
            throw new IllegalStateException("No se pudo determinar una estrategia de evaluación apropiada");
        }
        
        System.out.println("Usando estrategia: " + estrategia.getNombreEstrategia());
        
        // Evaluar usando la estrategia seleccionada
        BigDecimal montoCalculado = estrategia.evaluar(reclamacion);
        
        // Usar el monto proporcionado o el calculado por la estrategia
        BigDecimal montoFinal = montoAprobado != null ? montoAprobado : montoCalculado;
        
        reclamacion.setEstado(EstadoReclamacion.EN_EVALUACION);
        reclamacion.setMontoAprobado(montoFinal);
        reclamacion.setFechaEvaluacion(LocalDateTime.now());
        reclamacion.setEvaluador(evaluador);
        reclamacion.setObservaciones(observaciones);
        
        return true;
    }
    
    @Override
    public boolean aprobarReclamacion(Long reclamacionId, String evaluador) {
        Reclamacion reclamacion = buscarReclamacionPorId(reclamacionId);
        if (reclamacion == null) {
            throw new IllegalArgumentException("Reclamación no encontrada");
        }
        
        if (!reclamacion.puedeTransicionarA(EstadoReclamacion.APROBADA)) {
            throw new IllegalStateException("La reclamación no puede ser aprobada en su estado actual: " + reclamacion.getEstado());
        }
        
        reclamacion.setEstado(EstadoReclamacion.APROBADA);
        reclamacion.setEvaluador(evaluador);
        reclamacion.setFechaEvaluacion(LocalDateTime.now());
        
        String observaciones = reclamacion.getObservaciones() != null ? reclamacion.getObservaciones() : "";
        observaciones += "\n[APROBADA - " + LocalDateTime.now() + "] Aprobada por: " + evaluador;
        reclamacion.setObservaciones(observaciones);
        
        System.out.println("Reclamación aprobada: " + reclamacion.getNumeroReclamacion());
        System.out.println("Monto aprobado: $" + reclamacion.getMontoAprobado());
        
        return true;
    }
    
    @Override
    public boolean rechazarReclamacion(Long reclamacionId, String motivo, String evaluador) {
        Reclamacion reclamacion = buscarReclamacionPorId(reclamacionId);
        if (reclamacion == null) {
            throw new IllegalArgumentException("Reclamación no encontrada");
        }
        
        if (!reclamacion.puedeTransicionarA(EstadoReclamacion.RECHAZADA)) {
            throw new IllegalStateException("La reclamación no puede ser rechazada en su estado actual: " + reclamacion.getEstado());
        }
        
        reclamacion.setEstado(EstadoReclamacion.RECHAZADA);
        reclamacion.setEvaluador(evaluador);
        reclamacion.setFechaEvaluacion(LocalDateTime.now());
        reclamacion.setMontoAprobado(BigDecimal.ZERO);
        
        String observaciones = reclamacion.getObservaciones() != null ? reclamacion.getObservaciones() : "";
        observaciones += "\n[RECHAZADA - " + LocalDateTime.now() + "] Motivo: " + motivo + " | Evaluador: " + evaluador;
        reclamacion.setObservaciones(observaciones);
        
        System.out.println("Reclamación rechazada: " + reclamacion.getNumeroReclamacion());
        System.out.println("Motivo: " + motivo);
        
        return true;
    }
    
    @Override
    public boolean procesarPago(Long reclamacionId) {
        Reclamacion reclamacion = buscarReclamacionPorId(reclamacionId);
        if (reclamacion == null) {
            throw new IllegalArgumentException("Reclamación no encontrada");
        }
        
        if (!reclamacion.puedeTransicionarA(EstadoReclamacion.PAGADA)) {
            throw new IllegalStateException("La reclamación no puede ser pagada en su estado actual: " + reclamacion.getEstado());
        }
        
        reclamacion.setEstado(EstadoReclamacion.PAGADA);
        
        String observaciones = reclamacion.getObservaciones() != null ? reclamacion.getObservaciones() : "";
        observaciones += "\n[PAGADA - " + LocalDateTime.now() + "] Pago procesado por $" + reclamacion.getMontoAprobado();
        reclamacion.setObservaciones(observaciones);
        
        System.out.println("Pago procesado para reclamación: " + reclamacion.getNumeroReclamacion());
        System.out.println("Monto pagado: $" + reclamacion.getMontoAprobado());
        
        return true;
    }
    
    @Override
    @Transactional(readOnly = true)
    public Reclamacion buscarReclamacionPorId(Long reclamacionId) {
        return reclamacionRepository.findById(reclamacionId).orElse(null);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Reclamacion> obtenerReclamacionesPorPoliza(Long polizaId) {
        return reclamacionRepository.findByPolizaId(polizaId);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Reclamacion> obtenerReclamacionesPorEstado(EstadoReclamacion estado) {
        return reclamacionRepository.findByEstado(estado);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Reclamacion> obtenerReclamacionesPorFecha(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        return reclamacionRepository.findAll().stream()
                .filter(r -> r.getFechaReclamacion() != null &&
                           !r.getFechaReclamacion().isBefore(fechaInicio) &&
                           !r.getFechaReclamacion().isAfter(fechaFin))
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Reclamacion> obtenerTodasLasReclamaciones() {
        return reclamacionRepository.findAll();
    }
    
    /**
     * Selecciona la estrategia de evaluación apropiada según la reclamación
     */
    private EvaluacionStrategy seleccionarEstrategia(Reclamacion reclamacion) {
        return estrategias.stream()
                .filter(estrategia -> estrategia.puedeEvaluar(reclamacion))
                .findFirst()
                .orElse(null);
    }
    
    /**
     * Genera un número de reclamación secuencial basado en las reclamaciones existentes
     */
    private String generarNumeroReclamacion() {
        // Obtener la última reclamación para generar el próximo número secuencial
        List<Reclamacion> todas = reclamacionRepository.findAll();
        int nextNumber = todas.size() + 1;
        
        // Asegurar que el número generado no exista ya
        String numeroGenerado;
        do {
            numeroGenerado = String.format("REC%06d", nextNumber);
            nextNumber++;
        } while (existeNumeroReclamacion(numeroGenerado));
        
        return numeroGenerado;
    }
    
    /**
     * Verifica si ya existe una reclamación con el número dado
     */
    private boolean existeNumeroReclamacion(String numeroReclamacion) {
        return reclamacionRepository.findAll().stream()
                .anyMatch(r -> r.getNumeroReclamacion().equals(numeroReclamacion));
    }
}
