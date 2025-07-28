package com.seguratuauto.service.decorator;

import com.seguratuauto.model.Descuento;
import com.seguratuauto.service.PricingService;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

/**
 * Decorator que aplica descuentos al cálculo de prima
 * Implementa la lógica para aplicar descuentos basados en el perfil del cliente
 */
public class DescuentoPricingDecorator extends PricingServiceDecorator {
    
    public DescuentoPricingDecorator(PricingService pricingService) {
        super(pricingService);
    }
    
    @Override
    public BigDecimal calcularPrima(Long clienteId, String tipoSeguro, Map<String, Object> parametros) {
        // Obtener la prima base del servicio decorado
        BigDecimal primaBase = super.calcularPrima(clienteId, tipoSeguro, parametros);
        
        // Aplicar descuentos
        BigDecimal primaConDescuentos = aplicarDescuentos(clienteId, primaBase, parametros);
        
        return primaConDescuentos;
    }
    
    /**
     * Aplica descuentos según el perfil del cliente
     */
    private BigDecimal aplicarDescuentos(Long clienteId, BigDecimal primaBase, Map<String, Object> parametros) {
        BigDecimal primaFinal = primaBase;
        BigDecimal descuentoTotal = BigDecimal.ZERO;
        
        // Obtener descuentos vigentes
        var descuentos = obtenerDescuentosVigentes();
        
        for (Descuento descuento : descuentos) {
            if (aplicaDescuento(descuento, clienteId, parametros)) {
                BigDecimal montoDescuento = calcularMontoDescuento(primaBase, descuento);
                descuentoTotal = descuentoTotal.add(montoDescuento);
                
                System.out.println("Aplicando descuento: " + descuento.getNombre() + 
                                 " (" + descuento.getPorcentaje() + "%) = $" + montoDescuento);
            }
        }
        
        // Limitar descuento total al 30% máximo
        BigDecimal descuentoMaximo = primaBase.multiply(new BigDecimal("0.30"));
        if (descuentoTotal.compareTo(descuentoMaximo) > 0) {
            descuentoTotal = descuentoMaximo;
            System.out.println("Descuento limitado al máximo del 30%");
        }
        
        primaFinal = primaBase.subtract(descuentoTotal);
        
        if (descuentoTotal.compareTo(BigDecimal.ZERO) > 0) {
            System.out.println("Descuento total aplicado: $" + descuentoTotal);
            System.out.println("Prima después de descuentos: $" + primaFinal);
        }
        
        return primaFinal.setScale(2, RoundingMode.HALF_UP);
    }
    
    /**
     * Determina si un descuento aplica para el cliente y parámetros dados
     */
    private boolean aplicaDescuento(Descuento descuento, Long clienteId, Map<String, Object> parametros) {
        if (!descuento.esVigente()) {
            return false;
        }
        
        // Lógica específica para cada tipo de descuento
        switch (descuento.getTipo()) {
            case "HISTORIAL":
                // Simular verificación de historial de conducción
                return parametros.containsKey("buen_historial") && 
                       (Boolean) parametros.get("buen_historial");
                       
            case "FIDELIDAD":
                // Simular verificación de fidelidad del cliente
                return parametros.containsKey("cliente_frecuente") && 
                       (Boolean) parametros.get("cliente_frecuente");
                       
            case "SEGURIDAD":
                // Simular verificación de características de seguridad
                return parametros.containsKey("vehiculo_seguro") && 
                       (Boolean) parametros.get("vehiculo_seguro");
                       
            default:
                return false;
        }
    }
    
    /**
     * Calcula el monto del descuento
     */
    private BigDecimal calcularMontoDescuento(BigDecimal primaBase, Descuento descuento) {
        return primaBase.multiply(descuento.getPorcentaje())
                       .divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
    }
}
