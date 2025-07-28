package com.seguratuauto.service.decorator;

import com.seguratuauto.model.TarifaBase;
import com.seguratuauto.model.FactorRiesgo;
import com.seguratuauto.model.Descuento;
import com.seguratuauto.service.PricingService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Decorator base para el servicio de pricing
 * Implementa el patrón Decorator para permitir agregar funcionalidades
 * adicionales al cálculo de primas de forma flexible
 */
public abstract class PricingServiceDecorator implements PricingService {
    
    protected final PricingService pricingService;
    
    public PricingServiceDecorator(PricingService pricingService) {
        this.pricingService = pricingService;
    }
    
    @Override
    public BigDecimal calcularPrima(Long clienteId, String tipoSeguro, Map<String, Object> parametros) {
        return pricingService.calcularPrima(clienteId, tipoSeguro, parametros);
    }
    
    @Override
    public TarifaBase obtenerTarifaBase(String tipoSeguro) {
        return pricingService.obtenerTarifaBase(tipoSeguro);
    }
    
    @Override
    public List<FactorRiesgo> obtenerFactoresRiesgoActivos() {
        return pricingService.obtenerFactoresRiesgoActivos();
    }
    
    @Override
    public List<Descuento> obtenerDescuentosVigentes() {
        return pricingService.obtenerDescuentosVigentes();
    }
    
    @Override
    public Map<String, BigDecimal> obtenerTablaPreciosVigente(String tipoSeguro) {
        return pricingService.obtenerTablaPreciosVigente(tipoSeguro);
    }
    
    // Delegación de métodos adicionales para el patrón Decorator
    
    @Override
    public BigDecimal calcularPrecioBase(String tipoSeguro) {
        return pricingService.calcularPrecioBase(tipoSeguro);
    }
    
    @Override
    public BigDecimal aplicarDescuentos(BigDecimal precio, Long clienteId) {
        return pricingService.aplicarDescuentos(precio, clienteId);
    }
    
    @Override
    public BigDecimal aplicarFactoresRiesgo(BigDecimal precio, Map<String, Object> factoresRiesgo) {
        return pricingService.aplicarFactoresRiesgo(precio, factoresRiesgo);
    }
    
    @Override
    public BigDecimal calcularPrecioTotal(String tipoSeguro, Long clienteId, Map<String, Object> factoresRiesgo) {
        return pricingService.calcularPrecioTotal(tipoSeguro, clienteId, factoresRiesgo);
    }
}
