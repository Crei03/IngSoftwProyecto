package com.seguratuauto.service;

import com.seguratuauto.model.TarifaBase;
import com.seguratuauto.model.FactorRiesgo;
import com.seguratuauto.model.Descuento;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Servicio base para el cálculo de precios y primas de seguros
 * Implementa el patrón Decorator para permitir la aplicación de diferentes
 * componentes de cálculo de forma flexible
 */
public interface PricingService {
    
    /**
     * Calcula la prima base para un seguro de auto
     * @param clienteId ID del cliente
     * @param tipoSeguro tipo de seguro solicitado
     * @param parametros parámetros adicionales para el cálculo
     * @return prima calculada
     */
    BigDecimal calcularPrima(UUID clienteId, String tipoSeguro, Map<String, Object> parametros);
    
    /**
     * Obtiene la tarifa base para un tipo de seguro
     * @param tipoSeguro tipo de seguro
     * @return tarifa base
     */
    TarifaBase obtenerTarifaBase(String tipoSeguro);
    
    /**
     * Obtiene todos los factores de riesgo activos
     * @return lista de factores de riesgo
     */
    List<FactorRiesgo> obtenerFactoresRiesgoActivos();
    
    /**
     * Obtiene todos los descuentos vigentes
     * @return lista de descuentos
     */
    List<Descuento> obtenerDescuentosVigentes();
    
    /**
     * Obtiene la tabla de precios vigente
     * @param tipoSeguro tipo de seguro
     * @return tabla de precios
     */
    Map<String, BigDecimal> obtenerTablaPreciosVigente(String tipoSeguro);
    
    // Métodos adicionales para el patrón Decorator
    
    /**
     * Calcula el precio base para un tipo de seguro específico
     * @param tipoSeguro tipo de seguro
     * @return precio base
     */
    BigDecimal calcularPrecioBase(String tipoSeguro);
    
    /**
     * Aplica descuentos disponibles para un cliente específico
     * @param precio precio base
     * @param clienteId ID del cliente
     * @return precio con descuentos aplicados
     */
    BigDecimal aplicarDescuentos(BigDecimal precio, UUID clienteId);
    
    /**
     * Aplica factores de riesgo según los parámetros del cliente
     * @param precio precio actual
     * @param factoresRiesgo factores de riesgo del cliente
     * @return precio con factores de riesgo aplicados
     */
    BigDecimal aplicarFactoresRiesgo(BigDecimal precio, Map<String, Object> factoresRiesgo);
    
    /**
     * Calcula el precio total aplicando todos los modificadores
     * @param tipoSeguro tipo de seguro
     * @param clienteId ID del cliente
     * @param factoresRiesgo factores de riesgo
     * @return precio total calculado
     */
    BigDecimal calcularPrecioTotal(String tipoSeguro, UUID clienteId, Map<String, Object> factoresRiesgo);
}
