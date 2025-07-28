package com.seguratuauto.service.decorator;

import com.seguratuauto.model.FactorRiesgo;
import com.seguratuauto.service.PricingService;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

/**
 * Decorator que aplica recargos por factores de riesgo al cálculo de prima
 * Implementa la lógica para aplicar recargos basados en factores de riesgo
 */
public class FactorRiesgoPricingDecorator extends PricingServiceDecorator {
    
    public FactorRiesgoPricingDecorator(PricingService pricingService) {
        super(pricingService);
    }
    
    @Override
    public BigDecimal calcularPrima(Long clienteId, String tipoSeguro, Map<String, Object> parametros) {
        // Obtener la prima base del servicio decorado
        BigDecimal primaBase = super.calcularPrima(clienteId, tipoSeguro, parametros);
        
        // Aplicar recargos por factores de riesgo
        BigDecimal primaConRecargos = aplicarRecargos(clienteId, primaBase, parametros);
        
        return primaConRecargos;
    }
    
    /**
     * Aplica recargos según los factores de riesgo identificados
     */
    private BigDecimal aplicarRecargos(Long clienteId, BigDecimal primaBase, Map<String, Object> parametros) {
        BigDecimal primaFinal = primaBase;
        BigDecimal recargoTotal = BigDecimal.ZERO;
        
        // Obtener factores de riesgo activos
        var factoresRiesgo = obtenerFactoresRiesgoActivos();
        
        for (FactorRiesgo factor : factoresRiesgo) {
            if (aplicaFactorRiesgo(factor, clienteId, parametros)) {
                BigDecimal montoRecargo = calcularMontoRecargo(primaBase, factor);
                recargoTotal = recargoTotal.add(montoRecargo);
                
                System.out.println("Aplicando factor de riesgo: " + factor.getNombre() + 
                                 " (" + factor.getPorcentajeRecargo() + "%) = $" + montoRecargo);
            }
        }
        
        // Limitar recargo total al 50% máximo
        BigDecimal recargoMaximo = primaBase.multiply(new BigDecimal("0.50"));
        if (recargoTotal.compareTo(recargoMaximo) > 0) {
            recargoTotal = recargoMaximo;
            System.out.println("Recargo limitado al máximo del 50%");
        }
        
        primaFinal = primaBase.add(recargoTotal);
        
        if (recargoTotal.compareTo(BigDecimal.ZERO) > 0) {
            System.out.println("Recargo total aplicado: $" + recargoTotal);
            System.out.println("Prima después de recargos: $" + primaFinal);
        }
        
        return primaFinal.setScale(2, RoundingMode.HALF_UP);
    }
    
    /**
     * Determina si un factor de riesgo aplica para el cliente y parámetros dados
     */
    private boolean aplicaFactorRiesgo(FactorRiesgo factor, Long clienteId, Map<String, Object> parametros) {
        if (!factor.isActivo()) {
            return false;
        }
        
        // Lógica específica para cada tipo de factor de riesgo
        switch (factor.getTipo()) {
            case "EDAD":
                // Simular verificación de edad del conductor
                if (parametros.containsKey("edad_conductor")) {
                    Integer edad = (Integer) parametros.get("edad_conductor");
                    return edad != null && edad < 25; // Conductor joven
                }
                return false;
                
            case "UBICACION":
                // Simular verificación de zona de riesgo
                return parametros.containsKey("zona_riesgo") && 
                       "ALTO".equals(parametros.get("zona_riesgo"));
                       
            case "EXPERIENCIA":
                // Simular verificación de experiencia del conductor
                if (parametros.containsKey("anos_experiencia")) {
                    Integer experiencia = (Integer) parametros.get("anos_experiencia");
                    return experiencia != null && experiencia < 2; // Poca experiencia
                }
                return false;
                
            case "VEHICULO":
                // Simular verificación de características del vehículo
                return parametros.containsKey("vehiculo_riesgo") && 
                       (Boolean) parametros.get("vehiculo_riesgo");
                       
            default:
                return false;
        }
    }
    
    /**
     * Calcula el monto del recargo
     */
    private BigDecimal calcularMontoRecargo(BigDecimal primaBase, FactorRiesgo factor) {
        return primaBase.multiply(factor.getPorcentajeRecargo())
                       .divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
    }
}
