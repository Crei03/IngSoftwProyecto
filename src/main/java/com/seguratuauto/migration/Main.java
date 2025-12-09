package com.seguratuauto.migration;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.math.BigDecimal;

/**
 * Clase principal para la migración de datos de prueba
 * Inserta 5 registros en todas las tablas del sistema
 */
public class Main {
    
    private static String DB_URL = "jdbc:mysql://localhost:3306/segura_tu_auto";
    private static String DB_USER = "root";
    private static String DB_PASSWORD = "root";
    
    public static void main(String[] args) {
        // Leer credenciales de variables de entorno o argumentos
        if (args.length >= 1) DB_USER = args[0];
        if (args.length >= 2) DB_PASSWORD = args[1];
        if (args.length >= 3) DB_URL = args[2];
        
        // También intentar leer de variables de entorno
        if (System.getenv("DB_USER") != null) DB_USER = System.getenv("DB_USER");
        if (System.getenv("DB_PASSWORD") != null) DB_PASSWORD = System.getenv("DB_PASSWORD");
        if (System.getenv("DB_URL") != null) DB_URL = System.getenv("DB_URL");
        
        System.out.println("=== INICIANDO MIGRACIÓN DE DATOS ===");
        System.out.println("Base de datos: " + DB_URL);
        System.out.println("Usuario: " + DB_USER);
        System.out.println();
        
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            conn.setAutoCommit(false);
            
            try {
                // 1. Insertar Clientes
                insertarClientes(conn);
                
                // 2. Insertar Agentes
                insertarAgentes(conn);
                
                // 3. Insertar Evaluadores
                insertarEvaluadores(conn);
                
                // 4. Insertar Pólizas
                insertarPolizas(conn);
                
                // 5. Insertar Reclamaciones
                insertarReclamaciones(conn);
                
                conn.commit();
                System.out.println("\n✓ MIGRACIÓN COMPLETADA EXITOSAMENTE");
                System.out.println("Se insertaron 5 registros en cada tabla");
                
            } catch (SQLException e) {
                conn.rollback();
                System.err.println("✗ ERROR: La migración falló y se revirtieron los cambios");
                throw e;
            }
            
        } catch (SQLException e) {
            System.err.println("Error de conexión a la base de datos:");
            e.printStackTrace();
            System.exit(1);
        }
    }
    
    private static void insertarClientes(Connection conn) throws SQLException {
        System.out.println("Insertando clientes...");
        String sql = "INSERT INTO clientes (id_cliente, nombre, email, telefono, password, verificado, " +
                    "token_verificacion, token_expira, fecha_verificacion, reset_password_token, reset_token_expira) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            // Cliente 1 - Verificado (ID: 102)
            stmt.setLong(1, 102L);
            stmt.setString(2, "Juan Pérez García");
            stmt.setString(3, "juan.perez@email.com");
            stmt.setString(4, "+502 1234-5678");
            stmt.setString(5, "$2a$10$7QwQwQwQwQwQwQwQwQwQwOQwQwQwQwQwQwQwQwQwQwQwQwQwQwQwQwQwQwQwQw"); // cliente123$
            stmt.setBoolean(6, true);
            stmt.setNull(7, Types.VARCHAR);
            stmt.setNull(8, Types.TIMESTAMP);
            stmt.setTimestamp(9, Timestamp.valueOf(LocalDateTime.now().minusDays(10)));
            stmt.setNull(10, Types.VARCHAR);
            stmt.setNull(11, Types.TIMESTAMP);
            stmt.executeUpdate();
            
            // Cliente 2 - Verificado (ID: 103)
            stmt.setLong(1, 103L);
            stmt.setString(2, "María López Hernández");
            stmt.setString(3, "maria.lopez@email.com");
            stmt.setString(4, "+502 2345-6789");
            stmt.setString(5, "$2a$10$7QwQwQwQwQwQwQwQwQwQwOQwQwQwQwQwQwQwQwQwQwQwQwQwQwQwQwQwQwQwQw");
            stmt.setBoolean(6, true);
            stmt.setNull(7, Types.VARCHAR);
            stmt.setNull(8, Types.TIMESTAMP);
            stmt.setTimestamp(9, Timestamp.valueOf(LocalDateTime.now().minusDays(15)));
            stmt.setNull(10, Types.VARCHAR);
            stmt.setNull(11, Types.TIMESTAMP);
            stmt.executeUpdate();
            
            // Cliente 3 - No verificado (ID: 104)
            stmt.setLong(1, 104L);
            stmt.setString(2, "Carlos Rodríguez Méndez");
            stmt.setString(3, "carlos.rodriguez@email.com");
            stmt.setString(4, "+502 3456-7890");
            stmt.setString(5, "$2a$10$7QwQwQwQwQwQwQwQwQwQwOQwQwQwQwQwQwQwQwQwQwQwQwQwQwQwQwQwQwQwQw");
            stmt.setBoolean(6, false);
            stmt.setString(7, "abc123def456ghi789jkl012mno345pqr678stu901vwx234yz5678901234");
            stmt.setTimestamp(8, Timestamp.valueOf(LocalDateTime.now().plusHours(20)));
            stmt.setNull(9, Types.TIMESTAMP);
            stmt.setNull(10, Types.VARCHAR);
            stmt.setNull(11, Types.TIMESTAMP);
            stmt.executeUpdate();
            
            // Cliente 4 - Verificado (ID: 105)
            stmt.setLong(1, 105L);
            stmt.setString(2, "Ana Martínez Flores");
            stmt.setString(3, "ana.martinez@email.com");
            stmt.setString(4, "+502 4567-8901");
            stmt.setString(5, "$2a$10$7QwQwQwQwQwQwQwQwQwQwOQwQwQwQwQwQwQwQwQwQwQwQwQwQwQwQwQwQwQwQw");
            stmt.setBoolean(6, true);
            stmt.setNull(7, Types.VARCHAR);
            stmt.setNull(8, Types.TIMESTAMP);
            stmt.setTimestamp(9, Timestamp.valueOf(LocalDateTime.now().minusDays(5)));
            stmt.setNull(10, Types.VARCHAR);
            stmt.setNull(11, Types.TIMESTAMP);
            stmt.executeUpdate();
            
            // Cliente 5 - Verificado con token de reset (ID: 106)
            stmt.setLong(1, 106L);
            stmt.setString(2, "Luis Gómez Castillo");
            stmt.setString(3, "luis.gomez@email.com");
            stmt.setString(4, "+502 5678-9012");
            stmt.setString(5, "$2a$10$7QwQwQwQwQwQwQwQwQwQwOQwQwQwQwQwQwQwQwQwQwQwQwQwQwQwQwQwQwQwQw");
            stmt.setBoolean(6, true);
            stmt.setNull(7, Types.VARCHAR);
            stmt.setNull(8, Types.TIMESTAMP);
            stmt.setTimestamp(9, Timestamp.valueOf(LocalDateTime.now().minusDays(20)));
            stmt.setString(10, "reset123token456password789reset012token345password678reset901");
            stmt.setTimestamp(11, Timestamp.valueOf(LocalDateTime.now().plusHours(2)));
            stmt.executeUpdate();
            
            System.out.println("  ✓ 5 clientes insertados");
        }
    }
    
    private static void insertarAgentes(Connection conn) throws SQLException {
        System.out.println("Insertando agentes...");
        String sql = "INSERT INTO agentes (id_agente, nombre, codigo, email, telefono, password) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            // Agente 1 (ID: 22)
            stmt.setLong(1, 22L);
            stmt.setString(2, "Roberto Sánchez Díaz");
            stmt.setString(3, "AG-001");
            stmt.setString(4, "roberto.sanchez@seguratuauto.com");
            stmt.setString(5, "+502 6789-0123");
            stmt.setString(6, "$2a$10$6QwQwQwQwQwQwQwQwQwQwOQwQwQwQwQwQwQwQwQwQwQwQwQwQwQwQwQwQwQwQw"); // agente123$
            stmt.executeUpdate();
            
            // Agente 2 (ID: 23)
            stmt.setLong(1, 23L);
            stmt.setString(2, "Patricia Fernández Ramos");
            stmt.setString(3, "AG-002");
            stmt.setString(4, "patricia.fernandez@seguratuauto.com");
            stmt.setString(5, "+502 7890-1234");
            stmt.setString(6, "$2a$10$6QwQwQwQwQwQwQwQwQwQwOQwQwQwQwQwQwQwQwQwQwQwQwQwQwQwQwQwQwQwQw");
            stmt.executeUpdate();
            
            // Agente 3 (ID: 24)
            stmt.setLong(1, 24L);
            stmt.setString(2, "Diego Morales Torres");
            stmt.setString(3, "AG-003");
            stmt.setString(4, "diego.morales@seguratuauto.com");
            stmt.setString(5, "+502 8901-2345");
            stmt.setString(6, "$2a$10$6QwQwQwQwQwQwQwQwQwQwOQwQwQwQwQwQwQwQwQwQwQwQwQwQwQwQwQwQwQwQw");
            stmt.executeUpdate();
            
            // Agente 4 (ID: 25)
            stmt.setLong(1, 25L);
            stmt.setString(2, "Carmen Jiménez Vega");
            stmt.setString(3, "AG-004");
            stmt.setString(4, "carmen.jimenez@seguratuauto.com");
            stmt.setString(5, "+502 9012-3456");
            stmt.setString(6, "$2a$10$6QwQwQwQwQwQwQwQwQwQwOQwQwQwQwQwQwQwQwQwQwQwQwQwQwQwQwQwQwQwQw");
            stmt.executeUpdate();
            
            // Agente 5 (ID: 26)
            stmt.setLong(1, 26L);
            stmt.setString(2, "Fernando Castro Ruiz");
            stmt.setString(3, "AG-005");
            stmt.setString(4, "fernando.castro@seguratuauto.com");
            stmt.setString(5, "+502 1111-2222");
            stmt.setString(6, "$2a$10$6QwQwQwQwQwQwQwQwQwQwOQwQwQwQwQwQwQwQwQwQwQwQwQwQwQwQwQwQwQwQw");
            stmt.executeUpdate();
            
            System.out.println("  ✓ 5 agentes insertados");
        }
    }
    
    private static void insertarEvaluadores(Connection conn) throws SQLException {
        System.out.println("Insertando evaluadores...");
        String sql = "INSERT INTO evaluadores (id_evaluador, nombre, codigo, email, telefono, especialidad, activo, fecha_ingreso) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            // Evaluador 1 (ID: 17)
            stmt.setLong(1, 17L);
            stmt.setString(2, "Dr. Manuel Ortiz Pérez");
            stmt.setString(3, "EV-001");
            stmt.setString(4, "manuel.ortiz@seguratuauto.com");
            stmt.setString(5, "+502 2222-3333");
            stmt.setString(6, "Daños Corporales");
            stmt.setBoolean(7, true);
            stmt.setTimestamp(8, Timestamp.valueOf(LocalDateTime.now().minusMonths(6)));
            stmt.executeUpdate();
            
            // Evaluador 2 (ID: 18)
            stmt.setLong(1, 18L);
            stmt.setString(2, "Ing. Laura Ramírez Silva");
            stmt.setString(3, "EV-002");
            stmt.setString(4, "laura.ramirez@seguratuauto.com");
            stmt.setString(5, "+502 3333-4444");
            stmt.setString(6, "Daños Vehiculares");
            stmt.setBoolean(7, true);
            stmt.setTimestamp(8, Timestamp.valueOf(LocalDateTime.now().minusMonths(12)));
            stmt.executeUpdate();
            
            // Evaluador 3 (ID: 19)
            stmt.setLong(1, 19L);
            stmt.setString(2, "Lic. Pedro Vargas Núñez");
            stmt.setString(3, "EV-003");
            stmt.setString(4, "pedro.vargas@seguratuauto.com");
            stmt.setString(5, "+502 4444-5555");
            stmt.setString(6, "Fraude y Siniestros");
            stmt.setBoolean(7, true);
            stmt.setTimestamp(8, Timestamp.valueOf(LocalDateTime.now().minusMonths(8)));
            stmt.executeUpdate();
            
            // Evaluador 4 (ID: 20)
            stmt.setLong(1, 20L);
            stmt.setString(2, "Dra. Sofía Campos León");
            stmt.setString(3, "EV-004");
            stmt.setString(4, "sofia.campos@seguratuauto.com");
            stmt.setString(5, "+502 5555-6666");
            stmt.setString(6, "Evaluación Médica");
            stmt.setBoolean(7, true);
            stmt.setTimestamp(8, Timestamp.valueOf(LocalDateTime.now().minusMonths(3)));
            stmt.executeUpdate();
            
            // Evaluador 5 - Inactivo (ID: 21)
            stmt.setLong(1, 21L);
            stmt.setString(2, "Ing. Jorge Mejía Rojas");
            stmt.setString(3, "EV-005");
            stmt.setString(4, "jorge.mejia@seguratuauto.com");
            stmt.setString(5, "+502 6666-7777");
            stmt.setString(6, "Peritaje Técnico");
            stmt.setBoolean(7, false);
            stmt.setTimestamp(8, Timestamp.valueOf(LocalDateTime.now().minusMonths(18)));
            stmt.executeUpdate();
            
            System.out.println("  ✓ 5 evaluadores insertados");
        }
    }
    
    private static void insertarPolizas(Connection conn) throws SQLException {
        System.out.println("Insertando pólizas...");
        String sql = "INSERT INTO polizas (id_poliza, fecha_emision, estado, cliente_id, agente_id, numero_poliza, " +
                    "prima, tipo_seguro, fecha_vencimiento, observaciones, marca, modelo, anio_vehiculo, " +
                    "fecha_creacion, fecha_modificacion) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            LocalDateTime ahora = LocalDateTime.now();
            
            // Póliza 1 (ID: 201)
            stmt.setLong(1, 201L);
            stmt.setTimestamp(2, Timestamp.valueOf(ahora.minusDays(30)));
            stmt.setString(3, "APROBADA");
            stmt.setLong(4, 102L); // Cliente 1
            stmt.setLong(5, 22L); // Agente 1
            stmt.setString(6, "POL-2024-001");
            stmt.setBigDecimal(7, new BigDecimal("1500.00"));
            stmt.setString(8, "Todo Riesgo");
            stmt.setTimestamp(9, Timestamp.valueOf(ahora.plusYears(1).minusDays(30)));
            stmt.setString(10, "Póliza aprobada sin observaciones");
            stmt.setString(11, "Toyota");
            stmt.setString(12, "Corolla");
            stmt.setDate(13, Date.valueOf(LocalDate.of(2020, 1, 1)));
            stmt.setTimestamp(14, Timestamp.valueOf(ahora.minusDays(30)));
            stmt.setTimestamp(15, Timestamp.valueOf(ahora.minusDays(30)));
            stmt.executeUpdate();
            
            // Póliza 2 (ID: 202)
            stmt.setLong(1, 202L);
            stmt.setTimestamp(2, Timestamp.valueOf(ahora.minusDays(25)));
            stmt.setString(3, "APROBADA");
            stmt.setLong(4, 103L); // Cliente 2
            stmt.setLong(5, 23L); // Agente 2
            stmt.setString(6, "POL-2024-002");
            stmt.setBigDecimal(7, new BigDecimal("2100.00"));
            stmt.setString(8, "Todo Riesgo Premium");
            stmt.setTimestamp(9, Timestamp.valueOf(ahora.plusYears(1).minusDays(25)));
            stmt.setString(10, "Cliente premium con descuento del 10%");
            stmt.setString(11, "Honda");
            stmt.setString(12, "Civic");
            stmt.setDate(13, Date.valueOf(LocalDate.of(2021, 6, 15)));
            stmt.setTimestamp(14, Timestamp.valueOf(ahora.minusDays(25)));
            stmt.setTimestamp(15, Timestamp.valueOf(ahora.minusDays(25)));
            stmt.executeUpdate();
            
            // Póliza 3 (ID: 203)
            stmt.setLong(1, 203L);
            stmt.setTimestamp(2, Timestamp.valueOf(ahora.minusDays(20)));
            stmt.setString(3, "PENDIENTE");
            stmt.setLong(4, 104L); // Cliente 3
            stmt.setLong(5, 24L); // Agente 3
            stmt.setString(6, "POL-2024-003");
            stmt.setBigDecimal(7, new BigDecimal("1200.00"));
            stmt.setString(8, "Daños a Terceros");
            stmt.setTimestamp(9, Timestamp.valueOf(ahora.plusYears(1).minusDays(20)));
            stmt.setString(10, "Pendiente de verificación de documentos");
            stmt.setString(11, "Nissan");
            stmt.setString(12, "Sentra");
            stmt.setDate(13, Date.valueOf(LocalDate.of(2019, 3, 10)));
            stmt.setTimestamp(14, Timestamp.valueOf(ahora.minusDays(20)));
            stmt.setTimestamp(15, Timestamp.valueOf(ahora.minusDays(20)));
            stmt.executeUpdate();
            
            // Póliza 4 (ID: 204)
            stmt.setLong(1, 204L);
            stmt.setTimestamp(2, Timestamp.valueOf(ahora.minusDays(15)));
            stmt.setString(3, "APROBADA");
            stmt.setLong(4, 105L); // Cliente 4
            stmt.setLong(5, 25L); // Agente 4
            stmt.setString(6, "POL-2024-004");
            stmt.setBigDecimal(7, new BigDecimal("1800.00"));
            stmt.setString(8, "Todo Riesgo");
            stmt.setTimestamp(9, Timestamp.valueOf(ahora.plusYears(1).minusDays(15)));
            stmt.setString(10, "Incluye asistencia en carretera 24/7");
            stmt.setString(11, "Mazda");
            stmt.setString(12, "3");
            stmt.setDate(13, Date.valueOf(LocalDate.of(2022, 8, 20)));
            stmt.setTimestamp(14, Timestamp.valueOf(ahora.minusDays(15)));
            stmt.setTimestamp(15, Timestamp.valueOf(ahora.minusDays(15)));
            stmt.executeUpdate();
            
            // Póliza 5 (ID: 205)
            stmt.setLong(1, 205L);
            stmt.setTimestamp(2, Timestamp.valueOf(ahora.minusDays(10)));
            stmt.setString(3, "RECHAZADA");
            stmt.setLong(4, 106L); // Cliente 5
            stmt.setLong(5, 26L); // Agente 5
            stmt.setString(6, "POL-2024-005");
            stmt.setBigDecimal(7, new BigDecimal("950.00"));
            stmt.setString(8, "Básico");
            stmt.setTimestamp(9, Timestamp.valueOf(ahora.plusYears(1).minusDays(10)));
            stmt.setString(10, "Rechazada por inconsistencias en documentación");
            stmt.setString(11, "Chevrolet");
            stmt.setString(12, "Spark");
            stmt.setDate(13, Date.valueOf(LocalDate.of(2018, 11, 5)));
            stmt.setTimestamp(14, Timestamp.valueOf(ahora.minusDays(10)));
            stmt.setTimestamp(15, Timestamp.valueOf(ahora.minusDays(10)));
            stmt.executeUpdate();
            
            System.out.println("  ✓ 5 pólizas insertadas");
        }
    }
    
    private static void insertarReclamaciones(Connection conn) throws SQLException {
        System.out.println("Insertando reclamaciones...");
        String sql = "INSERT INTO reclamaciones (poliza_id, numero_reclamacion, descripcion, " +
                    "monto_reclamado, monto_aprobado, estado, fecha_reclamacion, fecha_evaluacion, " +
                    "fecha_resolucion, observaciones, evaluador_id) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            LocalDateTime ahora = LocalDateTime.now();
            
            // Reclamación 1 - Pagada
            stmt.setLong(1, 201L); // Póliza 1
            stmt.setString(2, "REC-2024-001");
            stmt.setString(3, "Colisión frontal en intersección, daños en capó y parachoques");
            stmt.setBigDecimal(4, new BigDecimal("8500.00"));
            stmt.setBigDecimal(5, new BigDecimal("7800.00"));
            stmt.setString(6, "PAGADA");
            stmt.setTimestamp(7, Timestamp.valueOf(ahora.minusDays(20)));
            stmt.setTimestamp(8, Timestamp.valueOf(ahora.minusDays(18)));
            stmt.setTimestamp(9, Timestamp.valueOf(ahora.minusDays(15)));
            stmt.setString(10, "Reclamación aprobada, se aplicó deducible del 10%");
            stmt.setLong(11, 18L); // Evaluador 2
            stmt.executeUpdate();
            
            // Reclamación 2 - Aprobada
            stmt.setLong(1, 202L); // Póliza 2
            stmt.setString(2, "REC-2024-002");
            stmt.setString(3, "Robo de espejos laterales y rines del vehículo");
            stmt.setBigDecimal(4, new BigDecimal("3200.00"));
            stmt.setBigDecimal(5, new BigDecimal("3200.00"));
            stmt.setString(6, "APROBADA");
            stmt.setTimestamp(7, Timestamp.valueOf(ahora.minusDays(12)));
            stmt.setTimestamp(8, Timestamp.valueOf(ahora.minusDays(10)));
            stmt.setTimestamp(9, Timestamp.valueOf(ahora.minusDays(8)));
            stmt.setString(10, "Aprobado 100%, cliente tiene cobertura premium");
            stmt.setLong(11, 19L); // Evaluador 3
            stmt.executeUpdate();
            
            // Reclamación 3 - En evaluación
            stmt.setLong(1, 201L); // Póliza 1
            stmt.setString(2, "REC-2024-003");
            stmt.setString(3, "Daños por granizo en el techo y capó del vehículo");
            stmt.setBigDecimal(4, new BigDecimal("4500.00"));
            stmt.setNull(5, Types.DECIMAL);
            stmt.setString(6, "EN_EVALUACION");
            stmt.setTimestamp(7, Timestamp.valueOf(ahora.minusDays(5)));
            stmt.setTimestamp(8, Timestamp.valueOf(ahora.minusDays(3)));
            stmt.setNull(9, Types.TIMESTAMP);
            stmt.setString(10, "En proceso de evaluación técnica");
            stmt.setLong(11, 18L); // Evaluador 2
            stmt.executeUpdate();
            
            // Reclamación 4 - Rechazada
            stmt.setLong(1, 204L); // Póliza 4
            stmt.setString(2, "REC-2024-004");
            stmt.setString(3, "Daños mecánicos en motor por falta de mantenimiento");
            stmt.setBigDecimal(4, new BigDecimal("15000.00"));
            stmt.setNull(5, Types.DECIMAL);
            stmt.setString(6, "RECHAZADA");
            stmt.setTimestamp(7, Timestamp.valueOf(ahora.minusDays(18)));
            stmt.setTimestamp(8, Timestamp.valueOf(ahora.minusDays(16)));
            stmt.setTimestamp(9, Timestamp.valueOf(ahora.minusDays(14)));
            stmt.setString(10, "Rechazada: los daños por falta de mantenimiento no están cubiertos");
            stmt.setLong(11, 18L); // Evaluador 2
            stmt.executeUpdate();
            
            // Reclamación 5 - Registrada
            stmt.setLong(1, 202L); // Póliza 2
            stmt.setString(2, "REC-2024-005");
            stmt.setString(3, "Rayón profundo en puerta lateral derecha por acto vandálico");
            stmt.setBigDecimal(4, new BigDecimal("2100.00"));
            stmt.setNull(5, Types.DECIMAL);
            stmt.setString(6, "REGISTRADA");
            stmt.setTimestamp(7, Timestamp.valueOf(ahora.minusDays(2)));
            stmt.setNull(8, Types.TIMESTAMP);
            stmt.setNull(9, Types.TIMESTAMP);
            stmt.setString(10, "Reclamación registrada, pendiente de asignación");
            stmt.setNull(11, Types.BIGINT);
            stmt.executeUpdate();
            
            System.out.println("  ✓ 5 reclamaciones insertadas");
        }
    }
}
