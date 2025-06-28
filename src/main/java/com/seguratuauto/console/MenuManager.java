package com.seguratuauto.console;

import com.seguratuauto.console.service.ConsolePolizaService;
import com.seguratuauto.util.JDBCUtil;

import java.util.Scanner;

/**
 * Gestor de menús para la aplicación de consola
 * Maneja la navegación y opciones de los diferentes menús
 */
public class MenuManager {
    
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_BLUE = "\u001B[34m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_YELLOW = "\u001B[33m";
    private static final String ANSI_CYAN = "\u001B[36m";
    private static final String ANSI_BOLD = "\u001B[1m";
    
    private final Scanner scanner;
    private final ConsolePolizaService polizaService;
    
    public MenuManager(Scanner scanner) {
        this.scanner = scanner;
        this.polizaService = new ConsolePolizaService(scanner);
    }
    
    /**
     * Muestra el menú de gestión de pólizas
     */
    public void mostrarMenuPolizas() {
        boolean volverMenuPrincipal = false;
        
        while (!volverMenuPrincipal) {
            limpiarPantalla();
            mostrarCabeceraPolizas();
            
            System.out.print(ANSI_CYAN + "👤 Selecciona una opción [0-6]: " + ANSI_RESET);
            String opcion = scanner.nextLine().trim();
            
            switch (opcion) {
                case "1":
                    polizaService.crearNuevaPoliza();
                    break;
                case "2":
                    polizaService.consultarTodasLasPolizas();
                    break;
                case "3":
                    polizaService.consultarPolizaPorId();
                    break;
                case "4":
                    polizaService.aprobarPoliza();
                    break;
                case "5":
                    polizaService.consultarPolizasPorEstado();
                    break;
                case "0":
                    volverMenuPrincipal = true;
                    break;
                default:
                    System.out.println(ANSI_RED + "❌ Opción no válida. Por favor selecciona un número del 0 al 6." + ANSI_RESET);
                    pausa("Presiona Enter para continuar...");
            }
        }
    }
    
    private void mostrarCabeceraPolizas() {
        System.out.println(ANSI_BOLD + ANSI_BLUE + "════════════════════════════════════════════════════════════════" + ANSI_RESET);
        System.out.println(ANSI_BOLD + ANSI_BLUE + "              📄 AUTOMATIZACIÓN DE REGISTRO DE PÓLIZAS          " + ANSI_RESET);
        System.out.println(ANSI_BOLD + ANSI_BLUE + "════════════════════════════════════════════════════════════════" + ANSI_RESET);
        System.out.println();
        
        System.out.println(ANSI_GREEN + "[1] ➕ Crear Nueva Póliza" + ANSI_RESET);
        System.out.println("    Registrar una nueva póliza en el sistema");
        System.out.println();
        
        System.out.println(ANSI_GREEN + "[2] 📋 Consultar Todas las Pólizas" + ANSI_RESET);
        System.out.println("    Ver listado completo de pólizas registradas");
        System.out.println();
        
        System.out.println(ANSI_GREEN + "[3] 🔍 Buscar Póliza por ID" + ANSI_RESET);
        System.out.println("    Consultar información detallada de una póliza específica");
        System.out.println();
        
        System.out.println(ANSI_GREEN + "[4] ✅ Aprobar Póliza" + ANSI_RESET);
        System.out.println("    Cambiar estado de póliza a APROBADA");
        System.out.println();
        
        System.out.println(ANSI_GREEN + "[5] ❌ Rechazar Póliza" + ANSI_RESET);
        System.out.println("    Cambiar estado de póliza a RECHAZADA");
        System.out.println();
        
        System.out.println(ANSI_GREEN + "[6] 📊 Consultar por Estado" + ANSI_RESET);
        System.out.println("    Filtrar pólizas por su estado actual");
        System.out.println();
        
        System.out.println(ANSI_YELLOW + "[0] ⬅️ Volver al Menú Principal" + ANSI_RESET);
        System.out.println();
        System.out.println(ANSI_BOLD + ANSI_BLUE + "════════════════════════════════════════════════════════════════" + ANSI_RESET);
    }
    
    /**
     * Muestra el menú de configuración del sistema
     */
    public void mostrarMenuConfiguracion() {
        boolean volverMenuPrincipal = false;
        
        while (!volverMenuPrincipal) {
            limpiarPantalla();
            mostrarCabeceraConfiguracion();
            
            System.out.print(ANSI_CYAN + "👤 Selecciona una opción [0-4]: " + ANSI_RESET);
            String opcion = scanner.nextLine().trim();
            
            switch (opcion) {
                case "1":
                    mostrarEstadoBaseDatos();
                    break;
                case "2":
                    mostrarInformacionSistema();
                    break;
                case "3":
                    probarConexionBaseDatos();
                    break;
                case "0":
                    volverMenuPrincipal = true;
                    break;
                default:
                    System.out.println(ANSI_RED + "❌ Opción no válida. Por favor selecciona un número del 0 al 3." + ANSI_RESET);
                    pausa("Presiona Enter para continuar...");
            }
        }
    }
    
    private void mostrarCabeceraConfiguracion() {
        System.out.println(ANSI_BOLD + ANSI_BLUE + "════════════════════════════════════════════════════════════════" + ANSI_RESET);
        System.out.println(ANSI_BOLD + ANSI_BLUE + "                    ⚙️ CONFIGURACIÓN DEL SISTEMA                " + ANSI_RESET);
        System.out.println(ANSI_BOLD + ANSI_BLUE + "════════════════════════════════════════════════════════════════" + ANSI_RESET);
        System.out.println();
        
        System.out.println(ANSI_GREEN + "[1] 🗄️ Estado de la Base de Datos" + ANSI_RESET);
        System.out.println("    Verificar conexión y estado de MySQL");
        System.out.println();
        
        System.out.println(ANSI_GREEN + "[2] 📋 Información del Sistema" + ANSI_RESET);
        System.out.println("    Detalles de versión, configuración y patrones");
        System.out.println();
        
        System.out.println(ANSI_GREEN + "[3] 🔧 Probar Conexión a BD" + ANSI_RESET);
        System.out.println("    Ejecutar test de conectividad a MySQL");
        System.out.println();
        
        System.out.println(ANSI_YELLOW + "[0] ⬅️ Volver al Menú Principal" + ANSI_RESET);
        System.out.println();
        System.out.println(ANSI_BOLD + ANSI_BLUE + "════════════════════════════════════════════════════════════════" + ANSI_RESET);
    }
    
    private void mostrarEstadoBaseDatos() {
        limpiarPantalla();
        System.out.println(ANSI_BOLD + ANSI_CYAN + "🗄️ ESTADO DE LA BASE DE DATOS" + ANSI_RESET);
        System.out.println(ANSI_CYAN + "═══════════════════════════════════════" + ANSI_RESET);
        System.out.println();
        
        try {
            boolean conexionDisponible = JDBCUtil.isConnectionAvailable();
            
            if (conexionDisponible) {
                System.out.println(ANSI_GREEN + "✅ Estado: CONECTADO" + ANSI_RESET);
                System.out.println("📊 " + JDBCUtil.getInstanceInfo());
                System.out.println("🗄️ Motor: " + JDBCUtil.getProperty("app.database"));
                System.out.println("🔗 URL: " + JDBCUtil.getProperty("db.mysql.url"));
                System.out.println("👤 Usuario: " + JDBCUtil.getProperty("db.mysql.username"));
                System.out.println("🏗️ Patrón: Singleton Pattern");
            } else {
                System.out.println(ANSI_RED + "❌ Estado: DESCONECTADO" + ANSI_RESET);
                System.out.println("⚠️ No se pudo establecer conexión con MySQL");
            }
            
        } catch (Exception e) {
            System.out.println(ANSI_RED + "❌ Error al verificar estado: " + e.getMessage() + ANSI_RESET);
        }
        
        System.out.println();
        pausa("Presiona Enter para continuar...");
    }
    
    private void mostrarInformacionSistema() {
        limpiarPantalla();
        System.out.println(ANSI_BOLD + ANSI_CYAN + "📋 INFORMACIÓN DEL SISTEMA" + ANSI_RESET);
        System.out.println(ANSI_CYAN + "═══════════════════════════════════════" + ANSI_RESET);
        System.out.println();
        
        System.out.println(ANSI_YELLOW + "📱 Aplicación:" + ANSI_RESET);
        System.out.println("   Nombre: " + JDBCUtil.getProperty("app.name"));
        System.out.println("   Versión: " + JDBCUtil.getProperty("app.version"));
        System.out.println("   Entorno: " + JDBCUtil.getProperty("app.environment"));
        System.out.println();
        
        System.out.println(ANSI_YELLOW + "☕ Java:" + ANSI_RESET);
        System.out.println("   Versión: " + System.getProperty("java.version"));
        System.out.println("   Vendor: " + System.getProperty("java.vendor"));
        System.out.println("   Runtime: " + System.getProperty("java.runtime.version"));
        System.out.println();
        
        System.out.println(ANSI_YELLOW + "🎯 Patrones de Diseño:" + ANSI_RESET);
        System.out.println("   • Singleton Pattern - Gestión de conexiones BD");
        System.out.println("   • Repository Pattern - Acceso a datos");
        System.out.println("   • Service Layer Pattern - Lógica de negocio");
        System.out.println("   • DTO Pattern - Transferencia de datos");
        System.out.println("   • Front Controller - Punto de entrada HTTP");
        System.out.println("   • Facade Pattern - Simplificación de operaciones");
        System.out.println();
        
        System.out.println(ANSI_YELLOW + "🏗️ Arquitectura:" + ANSI_RESET);
        System.out.println("   Tipo: Monolítica");
        System.out.println("   Capas: Model → DAO → Service → API");
        System.out.println("   Base de datos: MySQL");
        System.out.println("   Servidor HTTP: Embebido (puerto " + JDBCUtil.getProperty("server.port") + ")");
        System.out.println();
        
        pausa("Presiona Enter para continuar...");
    }
    
    private void probarConexionBaseDatos() {
        limpiarPantalla();
        System.out.println(ANSI_BOLD + ANSI_CYAN + "🔧 PRUEBA DE CONEXIÓN A BASE DE DATOS" + ANSI_RESET);
        System.out.println(ANSI_CYAN + "═══════════════════════════════════════════════" + ANSI_RESET);
        System.out.println();
        
        System.out.println("🔄 Ejecutando test de conectividad...");
        
        try {
            long startTime = System.currentTimeMillis();
            boolean conexionDisponible = JDBCUtil.isConnectionAvailable();
            long endTime = System.currentTimeMillis();
            long tiempoRespuesta = endTime - startTime;
            
            System.out.println();
            if (conexionDisponible) {
                System.out.println(ANSI_GREEN + "✅ Conexión exitosa" + ANSI_RESET);
                System.out.println("⏱️ Tiempo de respuesta: " + tiempoRespuesta + " ms");
                System.out.println("🏗️ Patrón utilizado: Singleton");
                System.out.println("🔗 Instancia: " + JDBCUtil.getInstanceInfo());
            } else {
                System.out.println(ANSI_RED + "❌ Conexión fallida" + ANSI_RESET);
                System.out.println("⏱️ Tiempo transcurrido: " + tiempoRespuesta + " ms");
                System.out.println("💡 Verifica que MySQL esté ejecutándose");
            }
            
        } catch (Exception e) {
            System.out.println(ANSI_RED + "❌ Error durante la prueba: " + e.getMessage() + ANSI_RESET);
        }
        
        System.out.println();
        pausa("Presiona Enter para continuar...");
    }
    
    private void limpiarPantalla() {
        try {
            if (System.getProperty("os.name").toLowerCase().contains("windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                System.out.print("\033[2J\033[H");
            }
        } catch (Exception e) {
            for (int i = 0; i < 50; i++) {
                System.out.println();
            }
        }
    }
    
    private void pausa(String mensaje) {
        System.out.print(ANSI_YELLOW + mensaje + ANSI_RESET);
        scanner.nextLine();
    }
}
