package com.seguratuauto.service.strategy;

import com.seguratuauto.model.Reclamacion;

import java.math.BigDecimal;

/**
 * Estrategia de evaluación automática para montos menores
 * Aprueba automáticamente reclamaciones de montos pequeños
 */
public class EvaluacionAutomaticaStrategy implements EvaluacionStrategy {
    
    private static final BigDecimal MONTO_MAXIMO_AUTOMATICO = new BigDecimal("5000.00");
    
    @Override
    public BigDecimal evaluar(Reclamacion reclamacion) {
        if (!puedeEvaluar(reclamacion)) {
            return null;
        }
        
        // Para montos pequeños, aprobar el 100%
        BigDecimal montoReclamado = reclamacion.getMontoReclamado();
        
        System.out.println("Evaluación automática - Monto: $" + montoReclamado);
        System.out.println("Monto aprobado automáticamente: 100%");
        
        return montoReclamado;
    }
    
    @Override
    public boolean puedeEvaluar(Reclamacion reclamacion) {
        if (reclamacion == null || reclamacion.getMontoReclamado() == null) {
            return false;
        }
        
        // Solo para montos menores al límite automático
        return reclamacion.getMontoReclamado().compareTo(MONTO_MAXIMO_AUTOMATICO) <= 0;
    }
    
    @Override
    public String getNombreEstrategia() {
        return "Evaluación Automática";
    }
}
