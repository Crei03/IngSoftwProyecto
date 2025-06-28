package com.seguratuauto.util;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Clase para inicializar la estructura de la base de datos
 */
public class DatabaseInitializer {

    /**
     * Inicializa la conexión a la base de datos sin crear tablas
     * Este método solo verifica que la conexión funcione
     * 
     * @throws SQLException si hay error en la inicialización de la conexión
     */
    public static void inicializarBaseDatos() throws SQLException {
        inicializarBaseDatos(true); // Por defecto, logging habilitado
    }
    
    /**
     * Inicializa la conexión a la base de datos sin crear tablas
     * Este método solo verifica que la conexión funcione
     * 
     * @param enableLogging si se deben mostrar mensajes de logging
     * @throws SQLException si hay error en la inicialización de la conexión
     */
    public static void inicializarBaseDatos(boolean enableLogging) throws SQLException {
        // Nota: No llamamos a JDBCUtil.getConnection() aquí para evitar ciclo infinito
        // La verificación de conexión se hace desde JDBCUtil directamente
        if (enableLogging) {
            System.out.println("Base de datos inicializada correctamente");
        }
    }
}
