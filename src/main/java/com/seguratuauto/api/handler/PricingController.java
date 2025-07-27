package com.seguratuauto.api.handler;

import com.seguratuauto.api.dto.CotizacionRequest;
import com.seguratuauto.api.dto.CotizacionResponse;
import com.seguratuauto.service.PricingService;
import com.seguratuauto.service.impl.BasicPricingServiceImpl;
import com.seguratuauto.service.decorator.DescuentoPricingDecorator;
import com.seguratuauto.service.decorator.FactorRiesgoPricingDecorator;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * Controlador REST para funcionalidades de pricing
 * Utiliza el patrón Decorator para cálculos flexibles de precios
 */
@RestController
@RequestMapping("/api/pricing")
@Validated
@CrossOrigin(origins = "*")
public class PricingController {
    
    private final PricingService pricingService;
    
    public PricingController() {
        // Configuración del patrón Decorator
        PricingService basicService = new BasicPricingServiceImpl();
        PricingService conDescuentos = new DescuentoPricingDecorator(basicService);
        this.pricingService = new FactorRiesgoPricingDecorator(conDescuentos);
    }
    
    /**
     * Calcula una cotización completa aplicando descuentos y factores de riesgo
     * 
     * @param request Datos de la cotización
     * @return Respuesta con precios calculados y detalles
     */
    @PostMapping("/cotizar")
    public ResponseEntity<CotizacionResponse> cotizar(@Valid @RequestBody CotizacionRequest request) {
        try {
            // Calcular precio base
            BigDecimal precioBase = pricingService.calcularPrecioBase(request.getTipoSeguro());
            
            // Calcular descuentos
            BigDecimal precioConDescuentos = pricingService.aplicarDescuentos(precioBase, request.getClienteId());
            BigDecimal descuentosAplicados = precioBase.subtract(precioConDescuentos);
            
            // Calcular factores de riesgo
            Map<String, Object> factores = request.getFactoresRiesgo() != null ? 
                request.getFactoresRiesgo() : new HashMap<>();
            BigDecimal precioConRiesgos = pricingService.aplicarFactoresRiesgo(precioConDescuentos, factores);
            BigDecimal recargosAplicados = precioConRiesgos.subtract(precioConDescuentos);
            
            // Precio total
            BigDecimal precioTotal = pricingService.calcularPrecioTotal(
                request.getTipoSeguro(), 
                request.getClienteId(), 
                factores
            );
            
            // Detalle de cálculos
            Map<String, BigDecimal> detalleCalculos = new HashMap<>();
            detalleCalculos.put("precioBase", precioBase);
            detalleCalculos.put("descuentos", descuentosAplicados);
            detalleCalculos.put("recargos", recargosAplicados);
            detalleCalculos.put("total", precioTotal);
            
            // Preparar respuesta
            CotizacionResponse response = new CotizacionResponse(
                request.getTipoSeguro(),
                precioBase,
                descuentosAplicados,
                recargosAplicados,
                precioTotal,
                detalleCalculos,
                "Cotización calculada exitosamente"
            );
            
            return ResponseEntity.ok(response);
            
        } catch (IllegalArgumentException e) {
            CotizacionResponse errorResponse = new CotizacionResponse();
            errorResponse.setMensaje("Error en la cotización: " + e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
            
        } catch (Exception e) {
            CotizacionResponse errorResponse = new CotizacionResponse();
            errorResponse.setMensaje("Error interno del servidor");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
    
    /**
     * Obtiene solo el precio base sin modificadores
     * 
     * @param tipoSeguro Tipo de seguro a cotizar
     * @return Precio base del seguro
     */
    @GetMapping("/precio-base/{tipoSeguro}")
    public ResponseEntity<Map<String, Object>> obtenerPrecioBase(@PathVariable String tipoSeguro) {
        try {
            BigDecimal precioBase = pricingService.calcularPrecioBase(tipoSeguro);
            
            Map<String, Object> response = new HashMap<>();
            response.put("tipoSeguro", tipoSeguro);
            response.put("precioBase", precioBase);
            response.put("mensaje", "Precio base obtenido exitosamente");
            
            return ResponseEntity.ok(response);
            
        } catch (IllegalArgumentException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Tipo de seguro no válido: " + tipoSeguro);
            errorResponse.put("mensaje", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
    
    /**
     * Obtiene los tipos de seguro disponibles
     * 
     * @return Lista de tipos de seguro con sus precios base
     */
    @GetMapping("/tipos-seguro")
    public ResponseEntity<Map<String, Object>> obtenerTiposSeguro() {
        Map<String, BigDecimal> tiposSeguro = new HashMap<>();
        tiposSeguro.put("AUTO_BASICO", new BigDecimal("800.00"));
        tiposSeguro.put("AUTO_INTERMEDIO", new BigDecimal("1200.00"));
        tiposSeguro.put("AUTO_FULL", new BigDecimal("1500.00"));
        tiposSeguro.put("MOTO", new BigDecimal("400.00"));
        
        Map<String, Object> response = new HashMap<>();
        response.put("tiposSeguro", tiposSeguro);
        response.put("mensaje", "Tipos de seguro disponibles");
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * Endpoint de prueba para verificar el funcionamiento del controlador
     * 
     * @return Mensaje de estado
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "OK");
        response.put("service", "Pricing Service");
        response.put("message", "Servicio de pricing funcionando correctamente");
        
        return ResponseEntity.ok(response);
    }
}
