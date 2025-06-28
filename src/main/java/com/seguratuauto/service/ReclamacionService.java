package com.seguratuauto.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Servicio para el manejo de reclamaciones de seguros
 * TODO: Implementar lógica real de reclamaciones
 */
public interface ReclamacionService {
    
    /**
     * Registra una nueva reclamación
     * @param polizaId ID de la póliza relacionada
     * @param descripcion descripción de la reclamación
     * @param montoReclamado monto reclamado
     * @return ID de la reclamación creada
     */
    UUID registrarReclamacion(UUID polizaId, String descripcion, java.math.BigDecimal montoReclamado);
    
    /**
     * Evalúa una reclamación
     * @param reclamacionId ID de la reclamación
     * @param montoAprobado monto aprobado para pago
     * @param observaciones observaciones de la evaluación
     * @return true si se evaluó correctamente
     */
    boolean evaluarReclamacion(UUID reclamacionId, java.math.BigDecimal montoAprobado, String observaciones);
    
    /**
     * Aprueba una reclamación para pago
     * @param reclamacionId ID de la reclamación
     * @return true si se aprobó correctamente
     */
    boolean aprobarReclamacion(UUID reclamacionId);
    
    /**
     * Rechaza una reclamación
     * @param reclamacionId ID de la reclamación
     * @param motivo motivo del rechazo
     * @return true si se rechazó correctamente
     */
    boolean rechazarReclamacion(UUID reclamacionId, String motivo);
    
    /**
     * Obtiene reclamaciones por póliza
     * @param polizaId ID de la póliza
     * @return lista de reclamaciones
     */
    List<Object> obtenerReclamacionesPorPoliza(UUID polizaId);
    
    /**
     * Obtiene reclamaciones en un rango de fechas
     * @param fechaInicio fecha de inicio
     * @param fechaFin fecha de fin
     * @return lista de reclamaciones
     */
    List<Object> obtenerReclamacionesPorFecha(LocalDateTime fechaInicio, LocalDateTime fechaFin);
}
