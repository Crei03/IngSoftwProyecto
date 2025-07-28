package com.seguratuauto.service;

import com.seguratuauto.model.Reclamacion;
import com.seguratuauto.model.EstadoReclamacion;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Servicio para el manejo de reclamaciones de seguros
 * Implementa el patrón Strategy para diferentes estrategias de evaluación
 */
public interface ReclamacionService {
    
    /**
     * Registra una nueva reclamación
     * @param polizaId ID de la póliza relacionada
     * @param descripcion descripción de la reclamación
     * @param montoReclamado monto reclamado
     * @return la reclamación creada
     */
    Reclamacion registrarReclamacion(Long polizaId, String descripcion, BigDecimal montoReclamado);
    
    /**
     * Evalúa una reclamación usando la estrategia configurada
     * @param reclamacionId ID de la reclamación
     * @param montoAprobado monto aprobado para pago
     * @param observaciones observaciones de la evaluación
     * @param evaluador nombre del evaluador
     * @return true si se evaluó correctamente
     */
    boolean evaluarReclamacion(Long reclamacionId, BigDecimal montoAprobado, String observaciones, String evaluador);
    
    /**
     * Aprueba una reclamación para pago
     * @param reclamacionId ID de la reclamación
     * @param evaluador nombre del evaluador
     * @return true si se aprobó correctamente
     */
    boolean aprobarReclamacion(Long reclamacionId, String evaluador);
    
    /**
     * Rechaza una reclamación
     * @param reclamacionId ID de la reclamación
     * @param motivo motivo del rechazo
     * @param evaluador nombre del evaluador
     * @return true si se rechazó correctamente
     */
    boolean rechazarReclamacion(Long reclamacionId, String motivo, String evaluador);
    
    /**
     * Procesa el pago de una reclamación aprobada
     * @param reclamacionId ID de la reclamación
     * @return true si se procesó el pago correctamente
     */
    boolean procesarPago(Long reclamacionId);
    
    /**
     * Busca una reclamación por ID
     * @param reclamacionId ID de la reclamación
     * @return la reclamación encontrada o null
     */
    Reclamacion buscarReclamacionPorId(Long reclamacionId);
    
    /**
     * Obtiene reclamaciones por póliza
     * @param polizaId ID de la póliza
     * @return lista de reclamaciones
     */
    List<Reclamacion> obtenerReclamacionesPorPoliza(Long polizaId);
    
    /**
     * Obtiene reclamaciones por estado
     * @param estado estado de las reclamaciones
     * @return lista de reclamaciones
     */
    List<Reclamacion> obtenerReclamacionesPorEstado(EstadoReclamacion estado);
    
    /**
     * Obtiene reclamaciones en un rango de fechas
     * @param fechaInicio fecha de inicio
     * @param fechaFin fecha de fin
     * @return lista de reclamaciones
     */
    List<Reclamacion> obtenerReclamacionesPorFecha(LocalDateTime fechaInicio, LocalDateTime fechaFin);
    
    /**
     * Obtiene todas las reclamaciones
     * @return lista de todas las reclamaciones
     */
    List<Reclamacion> obtenerTodasLasReclamaciones();
}
