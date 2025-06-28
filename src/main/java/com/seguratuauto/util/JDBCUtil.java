package com.seguratuauto.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Clase utilitaria para gestionar conexiones JDBC usando patrón Singleton
 * Configurada específicamente para MySQL
 * 
 * PATRÓN SINGLETON IMPLEMENTADO:
 * - Constructor privado para evitar instanciación directa
 * - Instancia única estática (instance)
 * - Método getInstance() con double-checked locking para thread safety
 * - Sincronización para prevenir condiciones de carrera
 * 
 * BENEFICIOS:
 * - Una sola instancia de configuración de BD en toda la aplicación
 * - Inicialización lazy (solo cuando se necesita)
 * - Thread-safe para aplicaciones concurrentes
 * - Control centralizado de la configuración de MySQL
 * 
 * USO:
 * JDBCUtil.getInstance(); // Inicializar singleton
 * Connection conn = JDBCUtil.getConnection(); // Obtener conexión MySQL
 */
public class JDBCUtil {
    
    private static final String CONFIG_FILE = "application.properties";
    private static JDBCUtil instance;
    private static Properties properties;
    private static final Object lock = new Object();
    private static boolean databaseInitialized = false;
    private static boolean loggingEnabled = true;
    private static int connectionCount = 0;
    
    // Constructor privado para implementar Singleton
    private JDBCUtil() {
        loadProperties();
    }
    
    /**
     * Obtiene la instancia única de JDBCUtil (Singleton)
     * @return instancia única de JDBCUtil
     */
    public static JDBCUtil getInstance() {
        if (instance == null) {
            synchronized (lock) {
                if (instance == null) {
                    instance = new JDBCUtil();
                }
            }
        }
        return instance;
    }
    
    /**
     * Carga las propiedades de configuración
     */
    private void loadProperties() {
        properties = new Properties();
        try (InputStream input = JDBCUtil.class.getClassLoader().getResourceAsStream(CONFIG_FILE)) {
            if (input == null) {
                throw new RuntimeException("No se pudo encontrar el archivo " + CONFIG_FILE);
            }
            properties.load(input);
            
            // Configurar logging basado en las propiedades
            String loggingConnectionStr = properties.getProperty("logging.connection.enable", "true");
            loggingEnabled = Boolean.parseBoolean(loggingConnectionStr);
            
        } catch (IOException e) {
            throw new RuntimeException("Error al cargar las propiedades de configuración", e);
        }
    }
    
    /**
     * Inicializa la base de datos MySQL (crear tablas si es necesario)
     * Solo se ejecuta una vez usando inicialización perezosa
     */
    private static void ensureDatabaseInitialized() {
        if (!databaseInitialized) {
            synchronized (lock) {
                if (!databaseInitialized) {
                    try {
                        String initLoggingStr = properties.getProperty("logging.initialization.enable", "true");
                        boolean initLoggingEnabled = Boolean.parseBoolean(initLoggingStr);
                        
                        // Solo marcamos como inicializada, sin crear conexiones adicionales
                        DatabaseInitializer.inicializarBaseDatos(initLoggingEnabled);
                        databaseInitialized = true;
                        
                    } catch (SQLException e) {
                        System.err.println("Error al inicializar la base de datos MySQL: " + e.getMessage());
                        // No lanzamos la excepción para permitir que la aplicación continúe
                    }
                }
            }
        }
    }
    
    /**
     * Inicializa la base de datos MySQL (crear tablas si es necesario)
     * @deprecated Use ensureDatabaseInitialized() en su lugar
     */
    private void initializeDatabase() {
        try {
            DatabaseInitializer.inicializarBaseDatos();
            System.out.println("Base de datos MySQL inicializada correctamente");
        } catch (SQLException e) {
            System.err.println("Error al inicializar la base de datos MySQL: " + e.getMessage());
        }
    }
    
    /**
     * Obtiene una conexión a la base de datos MySQL
     * @return Connection a MySQL
     * @throws SQLException si hay error en la conexión
     */
    public static Connection getConnection() throws SQLException {
        // Asegurar que la instancia singleton esté inicializada
        getInstance();
        
        String url = properties.getProperty("db.mysql.url");
        String username = properties.getProperty("db.mysql.username");
        String password = properties.getProperty("db.mysql.password");
        String driver = properties.getProperty("db.mysql.driver");
        
        if (url == null || username == null || driver == null) {
            throw new SQLException("Configuración de MySQL incompleta en application.properties");
        }

        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            throw new SQLException("Driver de MySQL no encontrado: " + driver, e);
        }

        // Configuraciones adicionales para optimizar la conexión
        String connectionUrl = url;
        if (!url.contains("autoReconnect")) {
            connectionUrl += "&autoReconnect=true&failOverReadOnly=false&maxReconnects=3";
        }

        Connection connection = DriverManager.getConnection(connectionUrl, username, password);
        
        // Configurar propiedades de la conexión para optimizar el pool
        connection.setAutoCommit(true);
        
        synchronized (lock) {
            connectionCount++;
            if (loggingEnabled && connectionCount <= 3) { // Solo mostrar las primeras 3 conexiones
                System.out.println("Conexión a la base de datos establecida correctamente (#" + connectionCount + ")");
            }
        }
        
        // Inicializar la base de datos de forma perezosa solo en la primera conexión exitosa
        ensureDatabaseInitialized();
        
        return connection;
    }
    
    /**
     * Cierra una conexión de forma segura
     * @param connection la conexión a cerrar
     */
    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                System.err.println("Error al cerrar la conexión: " + e.getMessage());
            }
        }
    }
    
    /**
     * Obtiene una propiedad de configuración
     * @param key la clave de la propiedad
     * @return el valor de la propiedad
     */
    public static String getProperty(String key) {
        // Asegurar que la instancia singleton esté inicializada
        getInstance();
        return properties.getProperty(key);
    }
    
    /**
     * Verifica si la conexión a MySQL está disponible
     * @return true si la conexión está disponible
     */
    public static boolean isConnectionAvailable() {
        try (Connection conn = getConnection()) {
            return conn != null && !conn.isClosed();
        } catch (SQLException e) {
            if (loggingEnabled) {
                System.err.println("Error al verificar conexión MySQL: " + e.getMessage());
            }
            return false;
        }
    }
    
    /**
     * Obtiene información de la instancia singleton para debugging
     * @return información de la instancia
     */
    public static String getInstanceInfo() {
        return "JDBCUtil Singleton - MySQL Connection Manager - Instance: " + 
               (instance != null ? instance.hashCode() : "Not initialized");
    }
    
    /**
     * Obtiene el perfil actual de la aplicación basado en la configuración
     * @return string que representa el perfil actual (ej: "mysql-development")
     */
    public static String getCurrentProfile() {
        // Asegurar que la instancia singleton esté inicializada
        getInstance();
        
        String database = properties.getProperty("app.database", "mysql");
        String environment = properties.getProperty("app.environment", "development");
        
        return database.toLowerCase() + "-" + environment.toLowerCase();
    }
}
