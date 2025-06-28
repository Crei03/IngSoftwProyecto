package com.seguratuauto;

import com.seguratuauto.console.ConsoleApplication;
import com.seguratuauto.util.DatabaseInitializer;
import com.seguratuauto.util.JDBCUtil;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Clase principal - Punto de entrada de la aplicación Segura Tu Auto
 * 
 * Esta aplicación de consola permite gestionar pólizas de seguros de auto
 * implementando los siguientes requisitos funcionales:
 * 
 * 📋 REQUISITOS FUNCIONALES IMPLEMENTADOS:
 * 
 * RF-001: Automatización del Registro de Pólizas
 * ✅ Crear nuevas pólizas con datos de cliente, agente y detalles de cobertura
 * ✅ Validación de datos de entrada
 * ✅ Asignación automática de estados (PENDIENTE por defecto)
 * ✅ Generación de ID único automático
 * 
 * RF-002: Gestión de Estados de Pólizas
 * ✅ Consultar pólizas por estado (PENDIENTE, APROBADA, RECHAZADA, CANCELADA)
 * ✅ Aprobar pólizas (cambio de estado PENDIENTE → APROBADA)
 * ✅ Rechazar pólizas (cambio de estado PENDIENTE → RECHAZADA)
 * ✅ Historial de cambios con fechas de creación y modificación
 * 
 * RF-003: Consulta y Búsqueda de Pólizas
 * ✅ Consultar todas las pólizas registradas
 * ✅ Buscar póliza específica por ID
 * ✅ Filtrar pólizas por estado
 * ✅ Mostrar información detallada (cliente, agente, cobertura, fechas)
 * 
 * RF-004: Gestión de Clientes y Agentes
 * ✅ Registro automático de nuevos clientes durante creación de póliza
 * ✅ Selección de agentes existentes o creación de agente por defecto
 * ✅ Vinculación automática cliente-agente-póliza
 * 
 * 🏗️ ARQUITECTURA IMPLEMENTADA:
 * - Patrón DAO para acceso a datos (JDBC puro)
 * - Patrón Service para lógica de negocio
 * - Patrón Singleton para conexión a BD
 * - Separación de capas (Modelo, DAO, Service, Console)
 * - API HTTP embebida (puerto 8080) para futuras integraciones
 * 
 * 🗄️ BASE DE DATOS:
 * - MySQL como motor de base de datos principal
 * - Tablas: clientes, agentes, polizas
 * - Relaciones: polizas → clientes, polizas → agentes
 * - Inicialización automática con datos de ejemplo
 * 
 * 🚀 INSTRUCCIONES DE EJECUCIÓN:
 * 
 * 1. Configurar MySQL:
 *    - Crear base de datos: CREATE DATABASE segura_tu_auto;
 *    - Configurar credenciales en src/main/resources/application.properties
 * 
 * 2. Ejecutar desde IntelliJ IDEA:
 *    - Abrir proyecto en IntelliJ
 *    - Ejecutar Main.main() desde esta clase
 *    - La aplicación verificará y configurará la BD automáticamente
 * 
 * 3. Ejecutar desde terminal:
 *    - mvn clean compile
 *    - mvn exec:java -Dexec.mainClass="com.seguratuauto.Main"
 * 
 * 4. Navegación del menú:
 *    - Menú principal: seleccionar opción de gestión de pólizas
 *    - Submenús intuitivos con validaciones
 *    - Códigos de colores para mejor experiencia de usuario
 *    - Opción de salida en cualquier momento (0 o Enter vacío)
 * 
 * 📚 FUNCIONALIDADES DEL MENÚ:
 * 
 * [1] ➕ Crear Nueva Póliza
 *     - Registrar cliente nuevo (nombre, email, teléfono, dirección)
 *     - Seleccionar agente existente o crear por defecto
 *     - Ingresar detalles de póliza (número, tipo, prima, cobertura, fechas)
 *     - Estado inicial: PENDIENTE
 * 
 * [2] 📋 Consultar Todas las Pólizas
 *     - Lista completa de pólizas en formato tabla
 *     - Información resumida: ID, número, tipo, estado, prima, fecha
 * 
 * [3] 🔍 Buscar Póliza por ID
 *     - Búsqueda específica por ID de póliza
 *     - Información detallada: póliza + cliente + agente
 * 
 * [4] ✅ Aprobar Póliza
 *     - Cambiar estado PENDIENTE → APROBADA
 *     - Validación de estado actual antes del cambio
 * 
 * [5] ❌ Rechazar Póliza
 *     - Cambiar estado PENDIENTE → RECHAZADA
 *     - Validación de estado actual antes del cambio
 * 
 * [6] 📊 Consultar por Estado
 *     - Filtrar pólizas por estado específico
 *     - Estados disponibles: PENDIENTE, APROBADA, RECHAZADA, CANCELADA
 * 
 * [7] ⚙️ Configuración del Sistema
 *     - Verificar conexión a base de datos
 *     - Mostrar información de configuración
 *     - Reinicializar base de datos (desarrollo)
 * 
 * [0] 🚪 Salir
 *     - Cierre seguro de conexiones
 *     - Mensaje de despedida
 * 
 * ⚠️ NOTAS IMPORTANTES:
 * - La aplicación maneja errores de conexión de BD graciosamente
 * - Se validan todos los datos de entrada del usuario
 * - Formatos de fecha: dd/MM/yyyy
 * - Montos en formato decimal (ej: 150.50)
 * - IDs numéricos únicos autogenerados
 * - Colores ANSI para mejor experiencia visual (funciona en terminales compatibles)
 * 
 * 🔧 CONFIGURACIÓN TÉCNICA:
 * - Java 17
 * - Maven como gestor de dependencias
 * - MySQL JDBC Driver
 * - API HTTP embebida (com.sun.net.httpserver)
 * - Sin frameworks externos (Java Vanilla)
 * 
 * @author Sistema Segura Tu Auto
 * @version 1.0
 * @since 2025-01-02
 */
public class Main {
    
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_BLUE = "\u001B[34m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_YELLOW = "\u001B[33m";
    private static final String ANSI_CYAN = "\u001B[36m";
    private static final String ANSI_BOLD = "\u001B[1m";
    
    /**
     * Punto de entrada principal de la aplicación
     * 
     * @param args argumentos de línea de comandos (no utilizados)
     */
    public static void main(String[] args) {
        mostrarBanner();
        
        try {
            // Verificar y configurar base de datos
            verificarBaseDatos();
            
            // Iniciar aplicación de consola
            ConsoleApplication app = new ConsoleApplication();
            app.start();
            
        } catch (Exception e) {
            System.err.println(ANSI_RED + "\n❌ Error crítico al iniciar la aplicación:" + ANSI_RESET);
            System.err.println(ANSI_RED + "   " + e.getMessage() + ANSI_RESET);
            System.err.println(ANSI_YELLOW + "\n💡 Verificar:" + ANSI_RESET);
            System.err.println("   - Configuración de base de datos en application.properties");
            System.err.println("   - Que MySQL esté ejecutándose");
            System.err.println("   - Que la base de datos 'segura_tu_auto' exista");
            System.err.println("   - Credenciales de acceso a MySQL");
            
            System.exit(1);
        }
    }
    
    /**
     * Muestra el banner de bienvenida de la aplicación
     */
    private static void mostrarBanner() {
        limpiarPantalla();
        
        System.out.println(ANSI_BOLD + ANSI_BLUE + 
            "╔══════════════════════════════════════════════════════════════════════════════╗\n" +
            "║                                                                              ║\n" +
            "║    ███████╗███████╗ ██████╗ ██╗   ██╗██████╗  █████╗     ████████╗██╗   ██╗ ║\n" +
            "║    ██╔════╝██╔════╝██╔════╝ ██║   ██║██╔══██╗██╔══██╗    ╚══██╔══╝██║   ██║ ║\n" +
            "║    ███████╗█████╗  ██║  ███╗██║   ██║██████╔╝███████║       ██║   ██║   ██║ ║\n" +
            "║    ╚════██║██╔══╝  ██║   ██║██║   ██║██╔══██╗██╔══██║       ██║   ██║   ██║ ║\n" +
            "║    ███████║███████╗╚██████╔╝╚██████╔╝██║  ██║██║  ██║       ██║   ╚██████╔╝ ║\n" +
            "║    ╚══════╝╚══════╝ ╚═════╝  ╚═════╝ ╚═╝  ╚═╝╚═╝  ╚═╝       ╚═╝    ╚═════╝  ║\n" +
            "║                                                                              ║\n" +
            "║                    █████╗ ██╗   ██╗████████╗ ██████╗                        ║\n" +
            "║                   ██╔══██╗██║   ██║╚══██╔══╝██╔═══██╗                       ║\n" +
            "║                   ███████║██║   ██║   ██║   ██║   ██║                       ║\n" +
            "║                   ██╔══██║██║   ██║   ██║   ██║   ██║                       ║\n" +
            "║                   ██║  ██║╚██████╔╝   ██║   ╚██████╔╝                       ║\n" +
            "║                   ╚═╝  ╚═╝ ╚═════╝    ╚═╝    ╚═════╝                        ║\n" +
            "║                                                                              ║\n" +
            "╚══════════════════════════════════════════════════════════════════════════════╝" + ANSI_RESET);
        
        System.out.println(ANSI_CYAN + "\n                🚗 Sistema de Gestión de Pólizas de Seguros de Auto 🚗" + ANSI_RESET);
        System.out.println(ANSI_YELLOW + "                        📋 Automatización de Registro de Pólizas 📋" + ANSI_RESET);
        System.out.println();
        
        System.out.println(ANSI_BOLD + "🎯 FUNCIONALIDADES PRINCIPALES:" + ANSI_RESET);
        System.out.println("   ✅ Registro automático de pólizas y clientes");
        System.out.println("   ✅ Gestión de estados (Pendiente → Aprobada/Rechazada)");
        System.out.println("   ✅ Consultas y búsquedas avanzadas");
        System.out.println("   ✅ Integración con base de datos MySQL");
        System.out.println("   ✅ Interfaz de consola intuitiva y colorida");
        System.out.println();
        
        System.out.println(ANSI_BOLD + "🔧 CARACTERÍSTICAS TÉCNICAS:" + ANSI_RESET);
        System.out.println("   🏛️  Patrón DAO para acceso a datos");
        System.out.println("   🔄 Patrón Service para lógica de negocio");
        System.out.println("   🎯 Patrón Singleton para conexión BD");
        //System.out.println("   🌐 API HTTP embebida (puerto 8080)");
        //System.out.println("   📊 JDBC puro sin frameworks");
        System.out.println();
        
        pausa("🚀 Presiona Enter para iniciar la aplicación...");
    }
    
    /**
     * Verifica la conexión y configuración de la base de datos
     */
    private static void verificarBaseDatos() {
        System.out.println(ANSI_BOLD + ANSI_BLUE + "🔍 VERIFICANDO CONFIGURACIÓN DE BASE DE DATOS..." + ANSI_RESET);
        System.out.println();
        
        try {
            // Verificar conexión una sola vez
            System.out.print("📡 Verificando conexión a MySQL... ");
            
            // Probar conexión simple sin inicialización redundante
            try (Connection testConnection = JDBCUtil.getConnection()) {
                if (testConnection != null && !testConnection.isClosed()) {
                    System.out.println(ANSI_GREEN + "✅ Conectado e inicializado" + ANSI_RESET);
                } else {
                    throw new SQLException("Conexión inválida");
                }
            }
            
            System.out.println();
            System.out.println(ANSI_GREEN + "🎉 ¡Base de datos configurada correctamente!" + ANSI_RESET);
            System.out.println();
            
        } catch (Exception e) {
            System.out.println(ANSI_RED + "❌ Error" + ANSI_RESET);
            System.err.println("Detalles del error: " + e.getMessage());
            throw new RuntimeException("Error al configurar la base de datos: " + e.getMessage(), e);
        }
        
        pausa("⏳ Presiona Enter para continuar...");
    }
    
    /**
     * Limpia la pantalla de la consola
     */
    private static void limpiarPantalla() {
        try {
            if (System.getProperty("os.name").contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                System.out.print("\033[2J\033[H");
            }
        } catch (Exception e) {
            // Si no se puede limpiar la pantalla, simplemente continuar
            for (int i = 0; i < 50; i++) {
                System.out.println();
            }
        }
    }
    
    /**
     * Pausa la ejecución y espera entrada del usuario
     */
    private static void pausa(String mensaje) {
        System.out.print(ANSI_CYAN + mensaje + ANSI_RESET);
        try {
            System.in.read();
        } catch (Exception e) {
            // Ignorar errores de entrada
        }
    }
}
