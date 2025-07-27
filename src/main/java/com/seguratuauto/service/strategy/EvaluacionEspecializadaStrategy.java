package com.seguratuauto.service.strategy;

import com.seguratuauto.model.Reclamacion;

import java.math.BigDecimal;

/**
 * Estrategia de evaluación especializada para montos altos
 * Requiere evaluación detallada y documentación exhaustiva
 */
public class EvaluacionEspecializadaStrategy implements EvaluacionStrategy {
    
    private static final BigDecimal MONTO_MINIMO_ESPECIALIZADO = new BigDecimal("20000.01");
    
    @Override
    public BigDecimal evaluar(Reclamacion reclamacion) {
        if (!puedeEvaluar(reclamacion)) {
            return null;
        }
        
        BigDecimal montoReclamado = reclamacion.getMontoReclamado();
        
        System.out.println("Evaluación especializada - Monto: $" + montoReclamado);
        System.out.println("Requiere documentación exhaustiva y evaluación detallada");
        
        // Lógica de evaluación especializada (más conservadora)
        BigDecimal porcentajeAprobacion = determinarPorcentajeEspecializado(reclamacion);
        BigDecimal montoAprobado = montoReclamado.multiply(porcentajeAprobacion)
                                                .divide(new BigDecimal("100"));
        
        System.out.println("Porcentaje aprobado tras evaluación especializada: " + porcentajeAprobacion + "%");
        System.out.println("Monto aprobado: $" + montoAprobado);
        
        return montoAprobado;
    }
    
    @Override
    public boolean puedeEvaluar(Reclamacion reclamacion) {
        if (reclamacion == null || reclamacion.getMontoReclamado() == null) {
            return false;
        }
        
        // Para montos altos que requieren evaluación especializada
        return reclamacion.getMontoReclamado().compareTo(MONTO_MINIMO_ESPECIALIZADO) >= 0;
    }
    
    @Override
    public String getNombreEstrategia() {
        return "Evaluación Especializada";
    }
    
    /**
     * Determina el porcentaje de aprobación con criterios más estrictos
     */
    private BigDecimal determinarPorcentajeEspecializado(Reclamacion reclamacion) {
        // Lógica más conservadora para montos altos
        String descripcion = reclamacion.getDescripcion().toLowerCase();
        BigDecimal monto = reclamacion.getMontoReclamado();
        
        // Reducir porcentajes para montos muy altos
        BigDecimal factorReduccion = BigDecimal.ONE;
        if (monto.compareTo(new BigDecimal("50000")) > 0) {
            factorReduccion = new BigDecimal("0.9"); // Reducir 10% para montos > 50k
        }
        if (monto.compareTo(new BigDecimal("100000")) > 0) {
            factorReduccion = new BigDecimal("0.8"); // Reducir 20% para montos > 100k
        }
        
        BigDecimal porcentajeBase;
        if (descripcion.contains("accidente") || descripcion.contains("choque")) {
            porcentajeBase = new BigDecimal("70"); // Más conservador para accidentes
        } else if (descripcion.contains("robo") || descripcion.contains("hurto")) {
            porcentajeBase = new BigDecimal("75"); // Más conservador para robos
        } else if (descripcion.contains("daño") || descripcion.contains("daños")) {
            porcentajeBase = new BigDecimal("65"); // Más conservador para daños
        } else if (descripcion.contains("vandalismo")) {
            porcentajeBase = new BigDecimal("60"); // Más conservador para vandalismo
        } else {
            porcentajeBase = new BigDecimal("70"); // Más conservador por defecto
        }
        
        return porcentajeBase.multiply(factorReduccion);
    }
}
