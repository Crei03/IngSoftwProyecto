package com.seguratuauto.service.strategy;

import com.seguratuauto.model.Reclamacion;

import java.math.BigDecimal;

/**
 * Estrategia de evaluación manual para montos medianos
 * Requiere evaluación manual con criterios específicos
 */
public class EvaluacionManualStrategy implements EvaluacionStrategy {
    
    private static final BigDecimal MONTO_MINIMO_MANUAL = new BigDecimal("5000.01");
    private static final BigDecimal MONTO_MAXIMO_MANUAL = new BigDecimal("20000.00");
    
    @Override
    public BigDecimal evaluar(Reclamacion reclamacion) {
        if (!puedeEvaluar(reclamacion)) {
            return null;
        }
        
        BigDecimal montoReclamado = reclamacion.getMontoReclamado();
        
        System.out.println("Evaluación manual - Monto: $" + montoReclamado);
        
        // Lógica de evaluación manual (simulada)
        BigDecimal porcentajeAprobacion = determinarPorcentajeAprobacion(reclamacion);
        BigDecimal montoAprobado = montoReclamado.multiply(porcentajeAprobacion)
                                                .divide(new BigDecimal("100"));
        
        System.out.println("Porcentaje aprobado: " + porcentajeAprobacion + "%");
        System.out.println("Monto aprobado: $" + montoAprobado);
        
        return montoAprobado;
    }
    
    @Override
    public boolean puedeEvaluar(Reclamacion reclamacion) {
        if (reclamacion == null || reclamacion.getMontoReclamado() == null) {
            return false;
        }
        
        BigDecimal monto = reclamacion.getMontoReclamado();
        return monto.compareTo(MONTO_MINIMO_MANUAL) >= 0 && 
               monto.compareTo(MONTO_MAXIMO_MANUAL) <= 0;
    }
    
    @Override
    public String getNombreEstrategia() {
        return "Evaluación Manual";
    }
    
    /**
     * Determina el porcentaje de aprobación basado en criterios específicos
     */
    private BigDecimal determinarPorcentajeAprobacion(Reclamacion reclamacion) {
        // Lógica simulada de evaluación manual
        String descripcion = reclamacion.getDescripcion().toLowerCase();
        
        if (descripcion.contains("accidente") || descripcion.contains("choque")) {
            return new BigDecimal("80"); // 80% para accidentes
        } else if (descripcion.contains("robo") || descripcion.contains("hurto")) {
            return new BigDecimal("90"); // 90% para robos
        } else if (descripcion.contains("daño") || descripcion.contains("daños")) {
            return new BigDecimal("75"); // 75% para daños
        } else if (descripcion.contains("vandalismo")) {
            return new BigDecimal("70"); // 70% para vandalismo
        } else {
            return new BigDecimal("85"); // 85% por defecto
        }
    }
}
