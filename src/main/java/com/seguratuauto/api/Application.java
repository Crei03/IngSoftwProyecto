package com.seguratuauto.api;

import com.seguratuauto.api.handler.PolizaHandler;
import com.seguratuauto.api.handler.PricingHandler;
import com.seguratuauto.api.handler.ReclamacionHandler;
import com.seguratuauto.util.JDBCUtil;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

/**
 * Clase principal de la aplicación Segura Tu Auto
 * Configura y arranca el servidor HTTP embebido
 */
public class Application {
    
    private static final int DEFAULT_PORT = 8080;
    private static final String DEFAULT_HOST = "localhost";
    
    private HttpServer server;
    private final int port;
    
    public Application() {
        this.port = getPortFromConfig();
    }
    
    public Application(int port) {
        this.port = port;
    }
    
    /**
     * Inicia la aplicación
     */
    public void start() throws IOException {
        // Verificar conectividad de base de datos
        initializeDatabase();
        
        // Crear servidor HTTP
        server = HttpServer.create(new InetSocketAddress(DEFAULT_HOST, port), 0);
        
        // Configurar handlers para endpoints
        configureHandlers();
        
        // Configurar pool de threads
        server.setExecutor(Executors.newFixedThreadPool(10));
        
        // Iniciar servidor
        server.start();
        
        System.out.println("=================================");
        System.out.println("🚗 SEGURA TU AUTO - INICIADO");
        System.out.println("=================================");
        System.out.println("Servidor HTTP iniciado en: http://" + DEFAULT_HOST + ":" + port);
        System.out.println("Base de datos: " + JDBCUtil.getCurrentProfile().toUpperCase());
        System.out.println("Endpoints disponibles:");
        System.out.println("  📋 POST   /api/polizas");
        System.out.println("  📋 GET    /api/polizas");
        System.out.println("  📋 GET    /api/polizas/{id}");
        System.out.println("  📋 PUT    /api/polizas/{id}/aprobar");
        System.out.println("  📋 PUT    /api/polizas/{id}/rechazar");
        System.out.println("  💰 *      /api/pricing/* (TODO)");
        System.out.println("  🛡️  *      /api/reclamaciones/* (TODO)");
        System.out.println("=================================");
        System.out.println("Presiona Ctrl+C para detener el servidor");
        
        // Agregar shutdown hook
        Runtime.getRuntime().addShutdownHook(new Thread(this::stop));
    }
    
    /**
     * Configura los handlers para los diferentes endpoints
     */
    private void configureHandlers() {
        // Handler principal para pólizas
        server.createContext("/api/polizas", new PolizaHandler());
        
        // Handlers para funcionalidades futuras
        server.createContext("/api/pricing", new PricingHandler());
        server.createContext("/api/reclamaciones", new ReclamacionHandler());
        
        // Handler para endpoint de salud
        server.createContext("/api/health", exchange -> {
            String response = """
                {
                    "status": "UP",
                    "application": "Segura Tu Auto",
                    "version": "1.0.0",
                    "database": "%s",
                    "timestamp": "%s"
                }
                """.formatted(
                    JDBCUtil.getCurrentProfile().toUpperCase(),
                    java.time.LocalDateTime.now().toString()
                );
            
            exchange.getResponseHeaders().set("Content-Type", "application/json");
            exchange.sendResponseHeaders(200, response.getBytes().length);
            exchange.getResponseBody().write(response.getBytes());
            exchange.close();
        });
        
        // Handler para la raíz
        server.createContext("/", exchange -> {
            String response = """
                <!DOCTYPE html>
                <html>
                <head>
                    <title>Segura Tu Auto API</title>
                    <style>
                        body { font-family: Arial, sans-serif; margin: 40px; }
                        .container { max-width: 600px; }
                        .endpoint { background: #f5f5f5; padding: 10px; margin: 5px 0; border-radius: 5px; }
                        .method { font-weight: bold; color: #0066cc; }
                    </style>
                </head>
                <body>
                    <div class="container">
                        <h1>🚗 Segura Tu Auto API</h1>
                        <p>Sistema monolítico para automatización del registro de pólizas de seguros</p>
                        
                        <h2>Endpoints Disponibles:</h2>
                        
                        <h3>Pólizas</h3>
                        <div class="endpoint">
                            <span class="method">POST</span> /api/polizas - Crear nueva póliza
                        </div>
                        <div class="endpoint">
                            <span class="method">GET</span> /api/polizas - Listar todas las pólizas
                        </div>
                        <div class="endpoint">
                            <span class="method">GET</span> /api/polizas/{id} - Obtener póliza por ID
                        </div>
                        <div class="endpoint">
                            <span class="method">PUT</span> /api/polizas/{id}/aprobar - Aprobar póliza
                        </div>
                        <div class="endpoint">
                            <span class="method">PUT</span> /api/polizas/{id}/rechazar - Rechazar póliza
                        </div>
                        
                        <h3>Otros</h3>
                        <div class="endpoint">
                            <span class="method">GET</span> /api/health - Estado del servicio
                        </div>
                        
                        <h3>En Desarrollo</h3>
                        <div class="endpoint">
                            <span class="method">*</span> /api/pricing/* - Servicios de pricing (TODO)
                        </div>
                        <div class="endpoint">
                            <span class="method">*</span> /api/reclamaciones/* - Servicios de reclamaciones (TODO)
                        </div>
                        
                        <p><strong>Base de datos activa:</strong> %s</p>
                        <p><strong>Versión:</strong> 1.0.0</p>
                    </div>
                </body>
                </html>
                """.formatted(JDBCUtil.getCurrentProfile().toUpperCase());
            
            exchange.getResponseHeaders().set("Content-Type", "text/html; charset=UTF-8");
            exchange.sendResponseHeaders(200, response.getBytes().length);
            exchange.getResponseBody().write(response.getBytes());
            exchange.close();
        });
    }
    
    /**
     * Inicializa y verifica la conexión a la base de datos MySQL (Singleton)
     */
    private void initializeDatabase() {
        try {
            System.out.println("📊 Verificando conexión MySQL (Singleton)...");
            
            // Asegurar que el singleton esté inicializado
            JDBCUtil.getInstance();
            
            // Verificar conectividad
            if (JDBCUtil.isConnectionAvailable()) {
                System.out.println("✅ Conexión MySQL establecida correctamente");
                System.out.println("   Base de datos: " + JDBCUtil.getProperty("app.database"));
            } else {
                System.err.println("❌ Error: No se pudo conectar a MySQL");
                System.err.println("   Verifique la configuración en application.properties");
                System.exit(1);
            }
            
        } catch (Exception e) {
            System.err.println("❌ Error al inicializar MySQL: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
    
    /**
     * Detiene el servidor
     */
    public void stop() {
        if (server != null) {
            System.out.println("\n🛑 Deteniendo servidor...");
            server.stop(2);
            System.out.println("✅ Servidor detenido correctamente");
        }
    }
    
    /**
     * Obtiene el puerto desde la configuración
     */
    private int getPortFromConfig() {
        try {
            String portStr = JDBCUtil.getProperty("server.port");
            return portStr != null ? Integer.parseInt(portStr) : DEFAULT_PORT;
        } catch (NumberFormatException e) {
            return DEFAULT_PORT;
        }
    }
    
    /**
     * Método main - punto de entrada de la aplicación
     */
    public static void main(String[] args) {
        try {
            // Inicializar el singleton de JDBCUtil y verificar conexión MySQL
            System.out.println("🚀 Iniciando Segura Tu Auto...");
            System.out.println("📊 Inicializando conexión a MySQL (Singleton Pattern)...");
            
            JDBCUtil jdbcUtil = JDBCUtil.getInstance();
            
            if (!JDBCUtil.isConnectionAvailable()) {
                System.err.println("❌ Error: No se pudo conectar a la base de datos MySQL");
                System.err.println("Verifica que MySQL esté ejecutándose y la configuración en application.properties");
                System.exit(1);
            }
            
            System.out.println("✅ " + JDBCUtil.getInstanceInfo());
            
            // Crear y iniciar la aplicación
            Application app = new Application();
            app.start();
            
            // Mantener el hilo principal vivo
            Thread.currentThread().join();
            
        } catch (Exception e) {
            System.err.println("❌ Error al iniciar la aplicación: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}
