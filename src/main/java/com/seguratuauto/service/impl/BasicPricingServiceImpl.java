package com.seguratuauto.service.impl;

import com.seguratuauto.model.TarifaBase;
import com.seguratuauto.model.FactorRiesgo;
import com.seguratuauto.model.Descuento;
import com.seguratuauto.service.PricingService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Implementación base del servicio de pricing
 * Esta clase proporciona la funcionalidad básica de cálculo de primas
 */
public class BasicPricingServiceImpl implements PricingService {
    
    // Simulación de datos (en producción vendría de DAO)
    private static final Map<String, TarifaBase> TARIFAS_BASE = new HashMap<>();
    private static final List<FactorRiesgo> FACTORES_RIESGO = new ArrayList<>();
    private static final List<Descuento> DESCUENTOS = new ArrayList<>();
    
    static {
        // Inicializar datos de ejemplo
        inicializarTarifasBase();
        inicializarFactoresRiesgo();
        inicializarDescuentos();
    }
    
    @Override
    public BigDecimal calcularPrima(UUID clienteId, String tipoSeguro, Map<String, Object> parametros) {
        TarifaBase tarifa = obtenerTarifaBase(tipoSeguro);
        if (tarifa == null) {
            throw new IllegalArgumentException("Tipo de seguro no válido: " + tipoSeguro);
        }
        
        return tarifa.getPrimaBase();
    }
    
    @Override
    public TarifaBase obtenerTarifaBase(String tipoSeguro) {
        return TARIFAS_BASE.get(tipoSeguro);
    }
    
    @Override
    public List<FactorRiesgo> obtenerFactoresRiesgoActivos() {
        return FACTORES_RIESGO.stream()
                .filter(FactorRiesgo::isActivo)
                .toList();
    }
    
    @Override
    public List<Descuento> obtenerDescuentosVigentes() {
        return DESCUENTOS.stream()
                .filter(Descuento::esVigente)
                .toList();
    }
    
    @Override
    public Map<String, BigDecimal> obtenerTablaPreciosVigente(String tipoSeguro) {
        Map<String, BigDecimal> tabla = new HashMap<>();
        TarifaBase tarifa = obtenerTarifaBase(tipoSeguro);
        if (tarifa != null) {
            tabla.put("prima_base", tarifa.getPrimaBase());
        }
        return tabla;
    }
    
    // Implementación de métodos adicionales para el patrón Decorator
    
    @Override
    public BigDecimal calcularPrecioBase(String tipoSeguro) {
        // Mapeo de tipos de seguro a precios base
        Map<String, BigDecimal> preciosBase = new HashMap<>();
        preciosBase.put("AUTO_BASICO", new BigDecimal("800.00"));
        preciosBase.put("AUTO_INTERMEDIO", new BigDecimal("1200.00"));
        preciosBase.put("AUTO_FULL", new BigDecimal("1500.00"));
        preciosBase.put("MOTO", new BigDecimal("400.00"));
        
        BigDecimal precio = preciosBase.get(tipoSeguro);
        if (precio == null) {
            throw new IllegalArgumentException("Tipo de seguro no válido: " + tipoSeguro);
        }
        
        return precio;
    }
    
    @Override
    public BigDecimal aplicarDescuentos(BigDecimal precio, UUID clienteId) {
        // Simulación de descuentos por cliente
        Map<UUID, BigDecimal> descuentosCliente = new HashMap<>();
        descuentosCliente.put(UUID.fromString("550e8400-e29b-41d4-a716-446655440000"), new BigDecimal("0.10")); // 10%
        descuentosCliente.put(UUID.fromString("550e8400-e29b-41d4-a716-446655440001"), new BigDecimal("0.15")); // 15%
        
        BigDecimal porcentajeDescuento = descuentosCliente.getOrDefault(clienteId, BigDecimal.ZERO);
        BigDecimal descuento = precio.multiply(porcentajeDescuento);
        
        return precio.subtract(descuento);
    }
    
    @Override
    public BigDecimal aplicarFactoresRiesgo(BigDecimal precio, Map<String, Object> factoresRiesgo) {
        if (factoresRiesgo == null || factoresRiesgo.isEmpty()) {
            return precio;
        }
        
        BigDecimal precioConRiesgos = precio;
        
        // Factor de riesgo por edad
        Object edad = factoresRiesgo.get("edad");
        if (edad instanceof Integer && (Integer) edad < 25) {
            precioConRiesgos = precioConRiesgos.multiply(new BigDecimal("1.15")); // +15%
        }
        
        // Factor de riesgo por experiencia
        Object experiencia = factoresRiesgo.get("experiencia");
        if (experiencia instanceof Integer && (Integer) experiencia < 2) {
            precioConRiesgos = precioConRiesgos.multiply(new BigDecimal("1.20")); // +20%
        }
        
        // Factor de riesgo por ubicación
        Object ubicacion = factoresRiesgo.get("ubicacion");
        if ("zona_riesgo".equals(ubicacion)) {
            precioConRiesgos = precioConRiesgos.multiply(new BigDecimal("1.10")); // +10%
        }
        
        // Factor de riesgo por vehículo
        Object vehiculo = factoresRiesgo.get("vehiculo");
        if ("deportivo".equals(vehiculo)) {
            precioConRiesgos = precioConRiesgos.multiply(new BigDecimal("1.25")); // +25%
        }
        
        return precioConRiesgos;
    }
    
    @Override
    public BigDecimal calcularPrecioTotal(String tipoSeguro, UUID clienteId, Map<String, Object> factoresRiesgo) {
        // Calcular precio base
        BigDecimal precio = calcularPrecioBase(tipoSeguro);
        
        // Aplicar descuentos
        precio = aplicarDescuentos(precio, clienteId);
        
        // Aplicar factores de riesgo
        precio = aplicarFactoresRiesgo(precio, factoresRiesgo);
        
        return precio;
    }
    
    // Métodos de inicialización de datos de ejemplo
    private static void inicializarTarifasBase() {
        TARIFAS_BASE.put("BASICO", new TarifaBase(
            UUID.randomUUID(), "BASICO", new BigDecimal("150.00"), 
            "Seguro básico con cobertura mínima", true
        ));
        
        TARIFAS_BASE.put("COMPLETO", new TarifaBase(
            UUID.randomUUID(), "COMPLETO", new BigDecimal("350.00"), 
            "Seguro completo con cobertura amplia", true
        ));
        
        TARIFAS_BASE.put("PREMIUM", new TarifaBase(
            UUID.randomUUID(), "PREMIUM", new BigDecimal("500.00"), 
            "Seguro premium con cobertura total", true
        ));
    }
    
    private static void inicializarFactoresRiesgo() {
        FACTORES_RIESGO.add(new FactorRiesgo(
            UUID.randomUUID(), "CONDUCTOR_JOVEN", "EDAD", 
            new BigDecimal("20.00"), "Conductor menor de 25 años", true
        ));
        
        FACTORES_RIESGO.add(new FactorRiesgo(
            UUID.randomUUID(), "ZONA_RIESGO_ALTO", "UBICACION", 
            new BigDecimal("15.00"), "Vehículo en zona de alto riesgo", true
        ));
        
        FACTORES_RIESGO.add(new FactorRiesgo(
            UUID.randomUUID(), "POCA_EXPERIENCIA", "EXPERIENCIA", 
            new BigDecimal("25.00"), "Conductor con menos de 2 años de experiencia", true
        ));
    }
    
    private static void inicializarDescuentos() {
        DESCUENTOS.add(new Descuento(
            UUID.randomUUID(), "BUEN_CONDUCTOR", "HISTORIAL", 
            new BigDecimal("10.00"), null, null, 
            "Descuento por buen historial de conducción", true
        ));
        
        DESCUENTOS.add(new Descuento(
            UUID.randomUUID(), "CLIENTE_FRECUENTE", "FIDELIDAD", 
            new BigDecimal("15.00"), null, null, 
            "Descuento por fidelidad del cliente", true
        ));
        
        DESCUENTOS.add(new Descuento(
            UUID.randomUUID(), "VEHICULO_SEGURO", "SEGURIDAD", 
            new BigDecimal("5.00"), null, null, 
            "Descuento por características de seguridad del vehículo", true
        ));
    }
}
