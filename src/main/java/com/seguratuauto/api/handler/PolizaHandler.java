package com.seguratuauto.api.handler;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.seguratuauto.api.dto.ApiResponse;
import com.seguratuauto.api.dto.PolizaDto;
import com.seguratuauto.api.mapper.EntityMapper;
import com.seguratuauto.dao.ClienteDao;
import com.seguratuauto.dao.impl.JdbcClienteDao;
import com.seguratuauto.model.Cliente;
import com.seguratuauto.model.EstadoPoliza;
import com.seguratuauto.model.Poliza;
import com.seguratuauto.service.PolizaService;
import com.seguratuauto.service.impl.PolizaServiceImpl;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Handler HTTP para operaciones con pólizas
 */
public class PolizaHandler implements HttpHandler {
    
    private final PolizaService polizaService;
    private final ClienteDao clienteDao;
    private final Gson gson;
    
    public PolizaHandler() {
        this.polizaService = new PolizaServiceImpl();
        this.clienteDao = new JdbcClienteDao();
        this.gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();
    }
    
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        // Configurar CORS
        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type, Authorization");
        
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();
        
        try {
            switch (method) {
                case "OPTIONS":
                    handleOptions(exchange);
                    break;
                case "POST":
                    if (path.equals("/api/polizas")) {
                        handleCreatePoliza(exchange);
                    } else {
                        sendErrorResponse(exchange, 404, "Endpoint no encontrado");
                    }
                    break;
                case "GET":
                    if (path.equals("/api/polizas")) {
                        handleGetAllPolizas(exchange);
                    } else if (path.matches("/api/polizas/[0-9a-f-]+")) {
                        handleGetPolizaById(exchange);
                    } else {
                        sendErrorResponse(exchange, 404, "Endpoint no encontrado");
                    }
                    break;
                case "PUT":
                    if (path.matches("/api/polizas/[0-9a-f-]+/aprobar")) {
                        handleAprobarPoliza(exchange);
                    } else if (path.matches("/api/polizas/[0-9a-f-]+/rechazar")) {
                        handleRechazarPoliza(exchange);
                    } else {
                        sendErrorResponse(exchange, 404, "Endpoint no encontrado");
                    }
                    break;
                default:
                    sendErrorResponse(exchange, 405, "Método no permitido");
            }
        } catch (Exception e) {
            e.printStackTrace();
            sendErrorResponse(exchange, 500, "Error interno del servidor: " + e.getMessage());
        }
    }
    
    private void handleOptions(HttpExchange exchange) throws IOException {
        exchange.sendResponseHeaders(200, 0);
        exchange.close();
    }
    
    private void handleCreatePoliza(HttpExchange exchange) throws IOException {
        try {
            // Leer el cuerpo de la petición
            String requestBody = readRequestBody(exchange);
            
            // Parsear JSON a DTO
            PolizaDto polizaDto = gson.fromJson(requestBody, PolizaDto.class);
            
            // Validar datos básicos
            if (polizaDto.getClienteId() == null || polizaDto.getClienteId().isEmpty()) {
                sendErrorResponse(exchange, 400, "ID del cliente es requerido");
                return;
            }
            
            if (polizaDto.getAgenteId() == null || polizaDto.getAgenteId().isEmpty()) {
                sendErrorResponse(exchange, 400, "ID del agente es requerido");
                return;
            }
            
            // Buscar el cliente
            UUID clienteId = UUID.fromString(polizaDto.getClienteId());
            Cliente cliente = clienteDao.findById(clienteId).orElse(null);
            
            if (cliente == null) {
                sendErrorResponse(exchange, 404, "Cliente no encontrado");
                return;
            }
            
            // Convertir DTO a entidad
            Poliza poliza = EntityMapper.toEntity(polizaDto);
            
            // Establecer valores por defecto si no están presentes
            if (poliza.getFechaEmision() == null) {
                poliza.setFechaEmision(LocalDateTime.now());
            }
            
            if (poliza.getEstado() == null) {
                poliza.setEstado(EstadoPoliza.PENDIENTE);
            }
            
            // Crear la póliza usando el servicio
            Poliza polizaCreada = polizaService.crearPoliza(cliente, poliza);
            
            // Convertir a DTO y enviar respuesta
            PolizaDto responseDto = EntityMapper.toDto(polizaCreada);
            ApiResponse<PolizaDto> response = ApiResponse.success(responseDto, "Póliza creada exitosamente");
            
            sendJsonResponse(exchange, 201, response);
            
        } catch (IllegalArgumentException e) {
            sendErrorResponse(exchange, 400, "Datos inválidos: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            sendErrorResponse(exchange, 500, "Error al crear la póliza: " + e.getMessage());
        }
    }
    
    private void handleGetAllPolizas(HttpExchange exchange) throws IOException {
        try {
            // TODO: Implementar paginación y filtros
            List<Poliza> polizas = polizaService.obtenerTodasLasPolizas();
            
            List<PolizaDto> polizasDto = polizas.stream()
                    .map(EntityMapper::toDto)
                    .toList();
            
            ApiResponse<List<PolizaDto>> response = ApiResponse.success(polizasDto, 
                    "Pólizas obtenidas exitosamente (" + polizasDto.size() + " registros)");
            
            sendJsonResponse(exchange, 200, response);
            
        } catch (Exception e) {
            e.printStackTrace();
            sendErrorResponse(exchange, 500, "Error al obtener las pólizas: " + e.getMessage());
        }
    }
    
    private void handleGetPolizaById(HttpExchange exchange) throws IOException {
        try {
            String path = exchange.getRequestURI().getPath();
            String idStr = path.substring(path.lastIndexOf('/') + 1);
            UUID polizaId = UUID.fromString(idStr);
            
            Poliza poliza = polizaService.buscarPolizaPorId(polizaId);
            
            if (poliza == null) {
                sendErrorResponse(exchange, 404, "Póliza no encontrada");
                return;
            }
            
            PolizaDto polizaDto = EntityMapper.toDto(poliza);
            ApiResponse<PolizaDto> response = ApiResponse.success(polizaDto, "Póliza encontrada");
            
            sendJsonResponse(exchange, 200, response);
            
        } catch (IllegalArgumentException e) {
            sendErrorResponse(exchange, 400, "ID de póliza inválido");
        } catch (Exception e) {
            e.printStackTrace();
            sendErrorResponse(exchange, 500, "Error al obtener la póliza: " + e.getMessage());
        }
    }
    
    private void handleAprobarPoliza(HttpExchange exchange) throws IOException {
        try {
            String path = exchange.getRequestURI().getPath();
            String idStr = path.split("/")[3]; // /api/polizas/{id}/aprobar
            UUID polizaId = UUID.fromString(idStr);
            
            Poliza polizaAprobada = polizaService.aprobarPoliza(polizaId);
            
            PolizaDto polizaDto = EntityMapper.toDto(polizaAprobada);
            ApiResponse<PolizaDto> response = ApiResponse.success(polizaDto, "Póliza aprobada exitosamente");
            
            sendJsonResponse(exchange, 200, response);
            
        } catch (IllegalArgumentException e) {
            sendErrorResponse(exchange, 400, "Error en los datos: " + e.getMessage());
        } catch (IllegalStateException e) {
            sendErrorResponse(exchange, 409, "Estado inválido: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            sendErrorResponse(exchange, 500, "Error al aprobar la póliza: " + e.getMessage());
        }
    }
    
    private void handleRechazarPoliza(HttpExchange exchange) throws IOException {
        try {
            String path = exchange.getRequestURI().getPath();
            String idStr = path.split("/")[3]; // /api/polizas/{id}/rechazar
            UUID polizaId = UUID.fromString(idStr);
            
            // Leer el motivo del cuerpo de la petición
            String requestBody = readRequestBody(exchange);
            @SuppressWarnings("unchecked")
            Map<String, String> requestMap = gson.fromJson(requestBody, Map.class);
            String motivo = requestMap.getOrDefault("motivo", "Sin motivo especificado");
            
            Poliza polizaRechazada = polizaService.rechazarPoliza(polizaId, motivo);
            
            PolizaDto polizaDto = EntityMapper.toDto(polizaRechazada);
            ApiResponse<PolizaDto> response = ApiResponse.success(polizaDto, "Póliza rechazada exitosamente");
            
            sendJsonResponse(exchange, 200, response);
            
        } catch (IllegalArgumentException e) {
            sendErrorResponse(exchange, 400, "Error en los datos: " + e.getMessage());
        } catch (IllegalStateException e) {
            sendErrorResponse(exchange, 409, "Estado inválido: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            sendErrorResponse(exchange, 500, "Error al rechazar la póliza: " + e.getMessage());
        }
    }
    
    private String readRequestBody(HttpExchange exchange) throws IOException {
        try (InputStream inputStream = exchange.getRequestBody()) {
            return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        }
    }
    
    private void sendJsonResponse(HttpExchange exchange, int statusCode, Object response) throws IOException {
        String jsonResponse = gson.toJson(response);
        byte[] responseBytes = jsonResponse.getBytes(StandardCharsets.UTF_8);
        
        exchange.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");
        exchange.sendResponseHeaders(statusCode, responseBytes.length);
        
        try (OutputStream outputStream = exchange.getResponseBody()) {
            outputStream.write(responseBytes);
        }
    }
    
    private void sendErrorResponse(HttpExchange exchange, int statusCode, String message) throws IOException {
        ApiResponse<Object> errorResponse = ApiResponse.error(message);
        sendJsonResponse(exchange, statusCode, errorResponse);
    }
}
