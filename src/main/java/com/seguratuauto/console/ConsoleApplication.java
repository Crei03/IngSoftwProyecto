package com.seguratuauto.console;

import com.seguratuauto.util.JDBCUtil;

import java.util.Scanner;

/**
 * Aplicación de consola principal para Segura Tu Auto
 * Punto de entrada alternativo para interactuar con el sistema vía terminal
 */
public class ConsoleApplication {
    
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_BLUE = "\u001B[34m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_YELLOW = "\u001B[33m";
    private static final String ANSI_CYAN = "\u001B[36m";
    private static final String ANSI_BOLD = "\u001B[1m";
    
    private final MenuManager menuManager;
    private final Scanner scanner;
    private boolean running;
    
    public ConsoleApplication() {
        this.scanner = new Scanner(System.in);
        this.menuManager = new MenuManager(scanner);
        this.running = true;
    }
    
    public static void main(String[] args) {
        ConsoleApplication app = new ConsoleApplication();
        app.start();
    }
    
    public void start() {
        try {
            mostrarBienvenida();
            verificarConexionBaseDatos();
            
            while (running) {
                mostrarMenuPrincipal();
                procesarOpcionPrincipal();
            }
            
        } catch (Exception e) {
            System.err.println(ANSI_RED + "❌ Error crítico en la aplicación: " + e.getMessage() + ANSI_RESET);
            e.printStackTrace();
        } finally {
            cerrarAplicacion();
        }
    }
    
    private void mostrarBienvenida() {
        limpiarPantalla();
        System.out.println(ANSI_CYAN + "╔══════════════════════════════════════════════════════════════╗" + ANSI_RESET);
        System.out.println(ANSI_CYAN + "║" + ANSI_BOLD + "                    🚗 SEGURA TU AUTO 🚗                      " + ANSI_RESET + ANSI_CYAN + "║" + ANSI_RESET);
        System.out.println(ANSI_CYAN + "╠══════════════════════════════════════════════════════════════╣" + ANSI_RESET);
        System.out.println(ANSI_CYAN + "║" + ANSI_RESET + "              Sistema de Gestión de Pólizas de Seguros            " + ANSI_CYAN + "║" + ANSI_RESET);
        System.out.println(ANSI_CYAN + "║" + ANSI_RESET + "                     Versión 1.0.0 - 2025                        " + ANSI_CYAN + "║" + ANSI_RESET);
        System.out.println(ANSI_CYAN + "╚══════════════════════════════════════════════════════════════╝" + ANSI_RESET);
        System.out.println();
        System.out.println(ANSI_YELLOW + "💡 Características principales:" + ANSI_RESET);
        System.out.println("   • Automatización del registro de pólizas");
        System.out.println("   • Gestión de clientes y agentes");
        System.out.println("   • Base de datos MySQL con patrón Singleton");
        //System.out.println("   • API REST embebida");
        System.out.println();
        System.out.println(ANSI_GREEN + "🎯 Patrones de diseño implementados:" + ANSI_RESET);
        System.out.println("   • Singleton (por el momento, para conexión a BD)");
        System.out.println();
        pausa("Presiona Enter para continuar...");
    }
    
    private void verificarConexionBaseDatos() {
        System.out.println(ANSI_YELLOW + "🔄 Inicializando conexión a base de datos..." + ANSI_RESET);
        
        try {
            JDBCUtil.getInstance();
            
            if (JDBCUtil.isConnectionAvailable()) {
                System.out.println(ANSI_GREEN + "✅ Conexión MySQL establecida correctamente" + ANSI_RESET);
                System.out.println(ANSI_BLUE + "📊 " + JDBCUtil.getInstanceInfo() + ANSI_RESET);
            } else {
                System.out.println(ANSI_RED + "❌ Error: No se pudo conectar a la base de datos MySQL" + ANSI_RESET);
                System.out.println("   Verifica que MySQL esté ejecutándose y la configuración en application.properties");
                System.exit(1);
            }
        } catch (Exception e) {
            System.out.println(ANSI_RED + "❌ Error al inicializar la base de datos: " + e.getMessage() + ANSI_RESET);
            System.exit(1);
        }
        
        System.out.println();
        pausa("Presiona Enter para acceder al menú principal...");
    }
    
    private void mostrarMenuPrincipal() {
        limpiarPantalla();
        System.out.println(ANSI_BOLD + ANSI_BLUE + "════════════════════════════════════════════════════════════════" + ANSI_RESET);
        System.out.println(ANSI_BOLD + ANSI_BLUE + "                        MENÚ PRINCIPAL                          " + ANSI_RESET);
        System.out.println(ANSI_BOLD + ANSI_BLUE + "════════════════════════════════════════════════════════════════" + ANSI_RESET);
        System.out.println();
        
        System.out.println(ANSI_CYAN + "📋 REQUISITOS FUNCIONALES DISPONIBLES:" + ANSI_RESET);
        System.out.println();
        System.out.println(ANSI_GREEN + "[1] 📄 Automatización del Registro de Pólizas" + ANSI_RESET);
        System.out.println("    • Crear nueva póliza");
        System.out.println("    • Consultar pólizas existentes");
        System.out.println("    • Aprobar/Rechazar pólizas");
        System.out.println("    • Gestionar estados de pólizas");
        System.out.println();
        
        System.out.println(ANSI_YELLOW + "[2] 💰 Servicios de Pricing" + ANSI_RESET + ANSI_RED + " (En desarrollo)" + ANSI_RESET);
        System.out.println("    • Cálculo automático de primas");
        System.out.println("    • Aplicación de descuentos");
        System.out.println("    • Gestión de factores de riesgo");
        System.out.println();
        
        System.out.println(ANSI_YELLOW + "[3] 🛠️ Gestión de Reclamaciones" + ANSI_RESET + ANSI_RED + " (En desarrollo)" + ANSI_RESET);
        System.out.println("    • Registro de reclamaciones");
        System.out.println("    • Workflow de aprobación");
        System.out.println("    • Gestión de pagos");
        System.out.println();
        
        System.out.println(ANSI_BLUE + "[4] ⚙️ Configuración del Sistema" + ANSI_RESET);
        System.out.println("    • Estado de la base de datos");
        System.out.println("    • Información del sistema");
        System.out.println("    • Estadísticas generales");
        System.out.println();
        
        System.out.println(ANSI_RED + "[0] 🚪 Salir del Sistema" + ANSI_RESET);
        System.out.println();
        System.out.println(ANSI_BOLD + ANSI_BLUE + "════════════════════════════════════════════════════════════════" + ANSI_RESET);
    }
    
    private void procesarOpcionPrincipal() {
        System.out.print(ANSI_CYAN + "👤 Selecciona una opción [0-4]: " + ANSI_RESET);
        
        try {
            String input = scanner.nextLine().trim();
            
            switch (input) {
                case "1":
                    menuManager.mostrarMenuPolizas();
                    break;
                case "2":
                    mostrarFuncionalidadEnDesarrollo("Servicios de Pricing");
                    break;
                case "3":
                    mostrarFuncionalidadEnDesarrollo("Gestión de Reclamaciones");
                    break;
                case "4":
                    menuManager.mostrarMenuConfiguracion();
                    break;
                case "0":
                    confirmarSalida();
                    break;
                default:
                    System.out.println(ANSI_RED + "❌ Opción no válida. Por favor selecciona un número del 0 al 4." + ANSI_RESET);
                    pausa("Presiona Enter para continuar...");
            }
        } catch (Exception e) {
            System.out.println(ANSI_RED + "❌ Error al procesar la opción: " + e.getMessage() + ANSI_RESET);
            pausa("Presiona Enter para continuar...");
        }
    }
    
    private void mostrarFuncionalidadEnDesarrollo(String funcionalidad) {
        limpiarPantalla();
        System.out.println(ANSI_YELLOW + "🚧 FUNCIONALIDAD EN DESARROLLO 🚧" + ANSI_RESET);
        System.out.println();
        System.out.println(ANSI_BOLD + funcionalidad + ANSI_RESET);
        System.out.println();
        System.out.println("Esta funcionalidad estará disponible en una próxima versión.");
        System.out.println("Actualmente nos enfocamos en la automatización del registro de pólizas.");
        System.out.println();
        System.out.println(ANSI_CYAN + "📋 TODO: Implementar " + funcionalidad + ANSI_RESET);
        System.out.println("• Diseñar interfaces de servicio");
        System.out.println("• Implementar lógica de negocio");
        System.out.println("• Crear handlers para API REST");
        System.out.println("• Añadir opciones al menú de consola");
        System.out.println();
        pausa("Presiona Enter para volver al menú principal...");
    }
    
    private void confirmarSalida() {
        System.out.println();
        System.out.print(ANSI_YELLOW + "❓ ¿Estás seguro que deseas salir? [s/N]: " + ANSI_RESET);
        String respuesta = scanner.nextLine().trim().toLowerCase();
        
        if (respuesta.equals("s") || respuesta.equals("si") || respuesta.equals("sí")) {
            running = false;
            System.out.println();
            System.out.println(ANSI_GREEN + "👋 ¡Gracias por usar Segura Tu Auto!" + ANSI_RESET);
            System.out.println("🔒 Cerrando conexiones de base de datos...");
            System.out.println("✅ Sistema cerrado correctamente.");
        }
    }
    
    private void cerrarAplicacion() {
        if (scanner != null) {
            scanner.close();
        }
        System.out.println(ANSI_CYAN + "🏁 Aplicación finalizada." + ANSI_RESET);
    }
    
    private void limpiarPantalla() {
        // Intentar limpiar pantalla según el SO
        try {
            if (System.getProperty("os.name").toLowerCase().contains("windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                System.out.print("\033[2J\033[H");
            }
        } catch (Exception e) {
            // Si no se puede limpiar, simplemente añadir líneas en blanco
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
