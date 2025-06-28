package com.seguratuauto.api.handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

/**
 * Handler HTTP para operaciones de pricing
 * TODO: Implementar lógica real de pricing
 */
public class PricingHandler implements HttpHandler {
    
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        // Configurar CORS
        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type, Authorization");
        
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();
        
        // TODO: Implementar endpoints de pricing:
        // POST /api/pricing/calcular - Calcular prima para una cotización
        // GET /api/pricing/tabla/{tipoSeguro} - Obtener tabla de precios
        // PUT /api/pricing/descuentos/{clienteId} - Aplicar descuentos
        // POST /api/pricing/recargos - Calcular recargos por riesgo
        
        String response = """
            {
                "success": false,
                "message": "Servicio de pricing en desarrollo - TODO implementar",
                "data": null,
                "timestamp": "%s"
            }
            """.formatted(java.time.LocalDateTime.now().toString());
        
        sendResponse(exchange, 501, response);
    }
    
    private void sendResponse(HttpExchange exchange, int statusCode, String response) throws IOException {
        byte[] responseBytes = response.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");
        exchange.sendResponseHeaders(statusCode, responseBytes.length);
        
        try (OutputStream outputStream = exchange.getResponseBody()) {
            outputStream.write(responseBytes);
        }
    }
}
