package com.seguratuauto.console.service;

import com.seguratuauto.dao.AgenteDao;
import com.seguratuauto.dao.ClienteDao;
import com.seguratuauto.dao.PolizaDao;
import com.seguratuauto.dao.impl.JdbcAgenteDao;
import com.seguratuauto.dao.impl.JdbcClienteDao;
import com.seguratuauto.dao.impl.JdbcPolizaDao;
import com.seguratuauto.model.Agente;
import com.seguratuauto.model.Cliente;
import com.seguratuauto.model.EstadoPoliza;
import com.seguratuauto.model.Poliza;
import com.seguratuauto.service.PolizaService;
import com.seguratuauto.service.impl.PolizaServiceImpl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

/**
 * Servicio de consola para gestión de pólizas
 * Conecta la interfaz de consola con la lógica de negocio
 */
public class ConsolePolizaService {
    
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_BLUE = "\u001B[34m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_YELLOW = "\u001B[33m";
    private static final String ANSI_CYAN = "\u001B[36m";
    private static final String ANSI_BOLD = "\u001B[1m";
    
    private final Scanner scanner;
    private final PolizaService polizaService;
    private final ClienteDao clienteDao;
    private final AgenteDao agenteDao;
    private final PolizaDao polizaDao;
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    
    public ConsolePolizaService(Scanner scanner) {
        this.scanner = scanner;
        this.clienteDao = new JdbcClienteDao();
        this.agenteDao = new JdbcAgenteDao();
        this.polizaDao = new JdbcPolizaDao();
        this.polizaService = new PolizaServiceImpl(polizaDao, clienteDao, agenteDao);
    }
    
    /**
     * Crear nueva póliza
     */
    public void crearNuevaPoliza() {
        try {
            limpiarPantalla();
            mostrarCabecera("➕ CREAR NUEVA PÓLIZA");
            
            // Recolectar datos del cliente
            System.out.println(ANSI_BOLD + "📋 DATOS DEL CLIENTE:" + ANSI_RESET);
            Cliente cliente = crearCliente();
            if (cliente == null) return;
            
            // Recolectar datos del agente
            System.out.println(ANSI_BOLD + "\n👤 DATOS DEL AGENTE:" + ANSI_RESET);
            Agente agente = seleccionarAgente();
            if (agente == null) return;
            
            // Recolectar datos de la póliza
            System.out.println(ANSI_BOLD + "\n📄 DATOS DE LA PÓLIZA:" + ANSI_RESET);
            Poliza poliza = crearPoliza(cliente, agente);
            if (poliza == null) return;
            
            // Crear póliza
            Poliza polizaCreada = polizaService.crearPoliza(cliente, poliza);
            
            System.out.println(ANSI_GREEN + "\n✅ Póliza creada exitosamente!" + ANSI_RESET);
            System.out.println(ANSI_CYAN + "ID de la póliza: " + polizaCreada.getIdPoliza() + ANSI_RESET);
            System.out.println(ANSI_CYAN + "Estado inicial: " + polizaCreada.getEstado() + ANSI_RESET);
            
        } catch (Exception e) {
            System.out.println(ANSI_RED + "❌ Error al crear la póliza: " + e.getMessage() + ANSI_RESET);
        } finally {
            pausa("\nPresiona Enter para continuar...");
        }
    }
    
    /**
     * Consultar todas las pólizas
     */
    public void consultarTodasLasPolizas() {
        try {
            limpiarPantalla();
            mostrarCabecera("📋 TODAS LAS PÓLIZAS");
            
            List<Poliza> polizas = polizaService.obtenerTodasLasPolizas();
            
            if (polizas.isEmpty()) {
                System.out.println(ANSI_YELLOW + "⚠️ No hay pólizas registradas en el sistema." + ANSI_RESET);
            } else {
                mostrarListaPolizas(polizas);
            }
            
        } catch (Exception e) {
            System.out.println(ANSI_RED + "❌ Error al consultar las pólizas: " + e.getMessage() + ANSI_RESET);
        } finally {
            pausa("\nPresiona Enter para continuar...");
        }
    }
    
    /**
     * Consultar póliza por ID
     */
    public void consultarPolizaPorId() {
        try {
            limpiarPantalla();
            mostrarCabecera("🔍 BUSCAR PÓLIZA POR ID");
            
            System.out.print(ANSI_CYAN + "Ingresa el ID de la póliza (UUID): " + ANSI_RESET);
            String input = scanner.nextLine().trim();
            
            if (input.isEmpty()) {
                System.out.println(ANSI_YELLOW + "⚠️ Operación cancelada." + ANSI_RESET);
                return;
            }
            
            UUID id = UUID.fromString(input);
            Poliza poliza = polizaService.buscarPolizaPorId(id);
            
            if (poliza == null) {
                System.out.println(ANSI_YELLOW + "⚠️ No se encontró una póliza con el ID: " + id + ANSI_RESET);
            } else {
                mostrarDetallePoliza(poliza);
            }
            
        } catch (IllegalArgumentException e) {
            System.out.println(ANSI_RED + "❌ ID inválido. Debe ser un UUID válido." + ANSI_RESET);
        } catch (Exception e) {
            System.out.println(ANSI_RED + "❌ Error al consultar la póliza: " + e.getMessage() + ANSI_RESET);
        } finally {
            pausa("\nPresiona Enter para continuar...");
        }
    }
    
    /**
     * Aprobar póliza
     */
    public void aprobarPoliza() {
        try {
            limpiarPantalla();
            mostrarCabecera("✅ APROBAR PÓLIZA");
            
            System.out.print(ANSI_CYAN + "Ingresa el ID de la póliza a aprobar: " + ANSI_RESET);
            String input = scanner.nextLine().trim();
            
            if (input.isEmpty()) {
                System.out.println(ANSI_YELLOW + "⚠️ Operación cancelada." + ANSI_RESET);
                return;
            }
            
            UUID id = UUID.fromString(input);
            Poliza polizaActualizada = polizaService.aprobarPoliza(id);
            
            System.out.println(ANSI_GREEN + "✅ Póliza aprobada exitosamente!" + ANSI_RESET);
            mostrarDetallePoliza(polizaActualizada);
            
        } catch (NumberFormatException e) {
            System.out.println(ANSI_RED + "❌ ID inválido. Debe ser un número." + ANSI_RESET);
        } catch (Exception e) {
            System.out.println(ANSI_RED + "❌ Error al aprobar la póliza: " + e.getMessage() + ANSI_RESET);
        } finally {
            pausa("\nPresiona Enter para continuar...");
        }
    }
    
    /**
     * Consultar pólizas por estado
     */
    public void consultarPolizasPorEstado() {
        try {
            limpiarPantalla();
            mostrarCabecera("📊 CONSULTAR POR ESTADO");
            
            EstadoPoliza estado = seleccionarEstado();
            if (estado == null) return;
            
            List<Poliza> polizas = polizaService.obtenerPolizasPorEstado(estado);
            
            if (polizas.isEmpty()) {
                System.out.println(ANSI_YELLOW + "⚠️ No hay pólizas con estado: " + estado + ANSI_RESET);
            } else {
                System.out.println(ANSI_GREEN + "\n📊 Pólizas con estado " + estado + ":" + ANSI_RESET);
                mostrarListaPolizas(polizas);
            }
            
        } catch (Exception e) {
            System.out.println(ANSI_RED + "❌ Error al consultar pólizas por estado: " + e.getMessage() + ANSI_RESET);
        } finally {
            pausa("\nPresiona Enter para continuar...");
        }
    }
    
    // =============== MÉTODOS AUXILIARES ===============
    
    private Cliente crearCliente() {
        try {
            System.out.print("Nombre: ");
            String nombre = scanner.nextLine().trim();
            if (nombre.isEmpty()) return null;
            
            System.out.print("Apellido: ");
            String apellido = scanner.nextLine().trim();
            if (apellido.isEmpty()) return null;
            
            System.out.print("Email: ");
            String email = scanner.nextLine().trim();
            if (email.isEmpty()) return null;
            
            System.out.print("Teléfono: ");
            String telefono = scanner.nextLine().trim();
            if (telefono.isEmpty()) return null;
            
            Cliente cliente = new Cliente();
            cliente.setNombre(nombre);
            cliente.setEmail(email);
            cliente.setTelefono(telefono);
            
            // Crear cliente en la base de datos
            return clienteDao.create(cliente);
            
        } catch (Exception e) {
            System.out.println(ANSI_RED + "❌ Error al crear cliente: " + e.getMessage() + ANSI_RESET);
            return null;
        }
    }
    
    private Agente seleccionarAgente() {
        try {
            List<Agente> agentes = agenteDao.findAll();
            
            if (agentes.isEmpty()) {
                System.out.println(ANSI_YELLOW + "⚠️ No hay agentes registrados. Creando agente por defecto..." + ANSI_RESET);
                return crearAgenteDefecto();
            }
            
            System.out.println("Agentes disponibles:");
            for (int i = 0; i < agentes.size(); i++) {
                Agente agente = agentes.get(i);
                System.out.println((i + 1) + ". " + agente.getNombre() + 
                                 " (Código: " + agente.getCodigo() + ")");
            }
            
            System.out.print("Selecciona un agente [1-" + agentes.size() + "]: ");
            String input = scanner.nextLine().trim();
            
            if (input.isEmpty()) return null;
            
            int indice = Integer.parseInt(input) - 1;
            if (indice >= 0 && indice < agentes.size()) {
                return agentes.get(indice);
            } else {
                System.out.println(ANSI_RED + "❌ Selección inválida." + ANSI_RESET);
                return null;
            }
            
        } catch (Exception e) {
            System.out.println(ANSI_RED + "❌ Error al seleccionar agente: " + e.getMessage() + ANSI_RESET);
            return null;
        }
    }
    
    private Agente crearAgenteDefecto() {
        try {
            Agente agente = new Agente();
            agente.setNombre("Juan Pérez");
            agente.setEmail("juan.perez@seguratuauto.com");
            agente.setTelefono("555-0123");
            agente.setCodigo("AG001");
            
            return agenteDao.create(agente);
        } catch (Exception e) {
            System.out.println(ANSI_RED + "❌ Error al crear agente por defecto: " + e.getMessage() + ANSI_RESET);
            return null;
        }
    }
    
    private Poliza crearPoliza(Cliente cliente, Agente agente) {
        try {
            System.out.print("Número de póliza: ");
            String numeroPoliza = scanner.nextLine().trim();
            if (numeroPoliza.isEmpty()) return null;
            
            System.out.print("Tipo de póliza: ");
            String tipoPoliza = scanner.nextLine().trim();
            if (tipoPoliza.isEmpty()) return null;
            
            System.out.print("Prima mensual: ");
            String primaStr = scanner.nextLine().trim();
            if (primaStr.isEmpty()) return null;
            BigDecimal primaMensual = new BigDecimal(primaStr);
            
            System.out.print("Cobertura (descripción): ");
            String cobertura = scanner.nextLine().trim();
            if (cobertura.isEmpty()) return null;
            
            System.out.print("Fecha de inicio (dd/MM/yyyy): ");
            String fechaInicioStr = scanner.nextLine().trim();
            if (fechaInicioStr.isEmpty()) return null;
            LocalDate fechaInicio = LocalDate.parse(fechaInicioStr, dateFormatter);
            
            System.out.print("Fecha de fin (dd/MM/yyyy): ");
            String fechaFinStr = scanner.nextLine().trim();
            if (fechaFinStr.isEmpty()) return null;
            LocalDate fechaFin = LocalDate.parse(fechaFinStr, dateFormatter);
            
            Poliza poliza = new Poliza();
            poliza.setNumeroPoliza(numeroPoliza);
            poliza.setTipoSeguro(tipoPoliza);
            poliza.setPrima(primaMensual);
            poliza.setObservaciones(cobertura);
            poliza.setFechaEmision(fechaInicio.atStartOfDay());
            poliza.setFechaVencimiento(fechaFin.atStartOfDay());
            poliza.setClienteId(cliente.getIdCliente());
            poliza.setAgenteId(agente.getIdAgente());
            poliza.setEstado(EstadoPoliza.PENDIENTE);
            
            return poliza;
            
        } catch (DateTimeParseException e) {
            System.out.println(ANSI_RED + "❌ Formato de fecha inválido. Use dd/MM/yyyy" + ANSI_RESET);
            return null;
        } catch (NumberFormatException e) {
            System.out.println(ANSI_RED + "❌ Prima mensual inválida. Debe ser un número." + ANSI_RESET);
            return null;
        } catch (Exception e) {
            System.out.println(ANSI_RED + "❌ Error al crear póliza: " + e.getMessage() + ANSI_RESET);
            return null;
        }
    }
    
    private EstadoPoliza seleccionarEstado() {
        System.out.println("Estados disponibles:");
        EstadoPoliza[] estados = EstadoPoliza.values();
        
        for (int i = 0; i < estados.length; i++) {
            System.out.println((i + 1) + ". " + estados[i]);
        }
        
        System.out.print("Selecciona un estado [1-" + estados.length + "]: ");
        String input = scanner.nextLine().trim();
        
        if (input.isEmpty()) return null;
        
        try {
            int indice = Integer.parseInt(input) - 1;
            if (indice >= 0 && indice < estados.length) {
                return estados[indice];
            } else {
                System.out.println(ANSI_RED + "❌ Selección inválida." + ANSI_RESET);
                return null;
            }
        } catch (NumberFormatException e) {
            System.out.println(ANSI_RED + "❌ Entrada inválida." + ANSI_RESET);
            return null;
        }
    }
    
    private void mostrarListaPolizas(List<Poliza> polizas) {
        System.out.println("\n" + ANSI_BOLD + "ID\tNúmero\t\tTipo\t\tEstado\t\tPrima\t\tFecha Creación" + ANSI_RESET);
        System.out.println("════════════════════════════════════════════════════════════════════════════");
        
        for (Poliza poliza : polizas) {
            System.out.printf("%s\t%s\t%s\t%s\t$%.2f\t%s%n",
                poliza.getIdPoliza(),
                poliza.getNumeroPoliza(),
                poliza.getTipoSeguro(),
                poliza.getEstado(),
                poliza.getPrima(),
                poliza.getFechaEmision().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
            );
        }
    }
    
    private void mostrarDetallePoliza(Poliza poliza) {
        try {
            Cliente cliente = clienteDao.findById(poliza.getClienteId()).orElse(null);
            Agente agente = agenteDao.findById(poliza.getAgenteId()).orElse(null);
            
            System.out.println("\n" + ANSI_BOLD + "═══ DETALLE DE PÓLIZA ===" + ANSI_RESET);
            System.out.println("ID: " + poliza.getIdPoliza());
            System.out.println("Número: " + poliza.getNumeroPoliza());
            System.out.println("Tipo: " + poliza.getTipoSeguro());
            System.out.println("Estado: " + poliza.getEstado());
            System.out.println("Prima: $" + poliza.getPrima());
            System.out.println("Observaciones: " + poliza.getObservaciones());
            System.out.println("Fecha Emisión: " + poliza.getFechaEmision().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
            System.out.println("Fecha Vencimiento: " + (poliza.getFechaVencimiento() != null ? 
                poliza.getFechaVencimiento().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")) : "N/A"));
            
            if (cliente != null) {
                System.out.println("\n" + ANSI_BOLD + "═══ CLIENTE ===" + ANSI_RESET);
                System.out.println("Nombre: " + cliente.getNombre());
                System.out.println("Email: " + cliente.getEmail());
                System.out.println("Teléfono: " + cliente.getTelefono());
            }
            
            if (agente != null) {
                System.out.println("\n" + ANSI_BOLD + "═══ AGENTE ===" + ANSI_RESET);
                System.out.println("Nombre: " + agente.getNombre());
                System.out.println("Código: " + agente.getCodigo());
                System.out.println("Email: " + agente.getEmail());
            }
            
        } catch (Exception e) {
            System.out.println(ANSI_RED + "❌ Error al mostrar detalles: " + e.getMessage() + ANSI_RESET);
        }
    }
    
    private void mostrarCabecera(String titulo) {
        System.out.println(ANSI_BOLD + ANSI_BLUE + "════════════════════════════════════════════════════════════════" + ANSI_RESET);
        System.out.println(ANSI_BOLD + ANSI_BLUE + "              " + titulo + "              " + ANSI_RESET);
        System.out.println(ANSI_BOLD + ANSI_BLUE + "════════════════════════════════════════════════════════════════" + ANSI_RESET);
        System.out.println();
    }
    
    private void limpiarPantalla() {
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
    
    private void pausa(String mensaje) {
        System.out.print(ANSI_CYAN + mensaje + ANSI_RESET);
        scanner.nextLine();
    }
}