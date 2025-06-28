package com.seguratuauto.api.handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

/**
 * Handler HTTP para operaciones de reclamaciones
 * TODO: Implementar lógica real de reclamaciones
 */
public class ReclamacionHandler implements HttpHandler {
    
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        // Configurar CORS
        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type, Authorization");
        
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();
        
        // TODO: Implementar endpoints de reclamaciones:
        // POST /api/reclamaciones - Registrar nueva reclamación
        // GET /api/reclamaciones/{id} - Obtener reclamación por ID
        // GET /api/reclamaciones/poliza/{polizaId} - Obtener reclamaciones por póliza
        // PUT /api/reclamaciones/{id}/evaluar - Evaluar reclamación
        // PUT /api/reclamaciones/{id}/aprobar - Aprobar reclamación
        // PUT /api/reclamaciones/{id}/rechazar - Rechazar reclamación
        
        String response = """
            {
                "success": false,
                "message": "Servicio de reclamaciones en desarrollo - TODO implementar",
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
