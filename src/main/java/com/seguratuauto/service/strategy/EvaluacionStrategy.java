package com.seguratuauto.service.strategy;

import com.seguratuauto.model.Reclamacion;

import java.math.BigDecimal;

/**
 * Estrategia base para la evaluación de reclamaciones
 * Implementa el patrón Strategy para diferentes tipos de evaluación
 */
public interface EvaluacionStrategy {
    
    /**
     * Evalúa una reclamación y determina el monto a aprobar
     * @param reclamacion la reclamación a evaluar
     * @return el monto aprobado o null si debe ser rechazada
     */
    BigDecimal evaluar(Reclamacion reclamacion);
    
    /**
     * Determina si esta estrategia puede evaluar el tipo de reclamación
     * @param reclamacion la reclamación a verificar
     * @return true si puede evaluar, false en caso contrario
     */
    boolean puedeEvaluar(Reclamacion reclamacion);
    
    /**
     * Obtiene el nombre de la estrategia
     * @return nombre descriptivo de la estrategia
     */
    String getNombreEstrategia();
}
