package com.seguratuauto.service;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

/**
 * Servicio para el cálculo de precios y primas de seguros
 * TODO: Implementar lógica real de pricing
 */
public interface PricingService {
    
    /**
     * Calcula la prima base para un seguro de auto
     * @param clienteId ID del cliente
     * @param tipoSeguro tipo de seguro solicitado
     * @param parametros parámetros adicionales para el cálculo
     * @return prima calculada
     */
    BigDecimal calcularPrimaAuto(UUID clienteId, String tipoSeguro, Map<String, Object> parametros);
    
    /**
     * Aplica descuentos según el perfil del cliente
     * @param clienteId ID del cliente
     * @param primaBase prima base antes de descuentos
     * @return prima con descuentos aplicados
     */
    BigDecimal aplicarDescuentos(UUID clienteId, BigDecimal primaBase);
    
    /**
     * Calcula recargos por factores de riesgo
     * @param clienteId ID del cliente
     * @param factoresRiesgo factores de riesgo identificados
     * @param primaBase prima base antes de recargos
     * @return prima con recargos aplicados
     */
    BigDecimal calcularRecargos(UUID clienteId, Map<String, Object> factoresRiesgo, BigDecimal primaBase);
    
    /**
     * Obtiene la tabla de precios vigente
     * @param tipoSeguro tipo de seguro
     * @return tabla de precios
     */
    Map<String, BigDecimal> obtenerTablaPreciosVigente(String tipoSeguro);
}
