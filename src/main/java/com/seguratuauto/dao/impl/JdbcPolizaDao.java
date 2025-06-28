package com.seguratuauto.dao.impl;

import com.seguratuauto.dao.PolizaDao;
import com.seguratuauto.model.EstadoPoliza;
import com.seguratuauto.model.Poliza;
import com.seguratuauto.util.JDBCUtil;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Implementación JDBC del DAO para Poliza
 */
public class JdbcPolizaDao implements PolizaDao {
    
    private static final String INSERT_SQL = """
        INSERT INTO polizas (id_poliza, numero_poliza, fecha_emision, fecha_vencimiento, 
                           estado, cliente_id, agente_id, prima, tipo_seguro, observaciones) 
        VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;
    
    private static final String SELECT_BY_ID_SQL = """
        SELECT id_poliza, numero_poliza, fecha_emision, fecha_vencimiento, estado, 
               cliente_id, agente_id, prima, tipo_seguro, observaciones 
        FROM polizas WHERE id_poliza = ?
        """;
    
    private static final String SELECT_BY_NUMERO_SQL = """
        SELECT id_poliza, numero_poliza, fecha_emision, fecha_vencimiento, estado, 
               cliente_id, agente_id, prima, tipo_seguro, observaciones 
        FROM polizas WHERE numero_poliza = ?
        """;
    
    private static final String SELECT_BY_CLIENTE_SQL = """
        SELECT id_poliza, numero_poliza, fecha_emision, fecha_vencimiento, estado, 
               cliente_id, agente_id, prima, tipo_seguro, observaciones 
        FROM polizas WHERE cliente_id = ? ORDER BY fecha_emision DESC
        """;
    
    private static final String SELECT_BY_AGENTE_SQL = """
        SELECT id_poliza, numero_poliza, fecha_emision, fecha_vencimiento, estado, 
               cliente_id, agente_id, prima, tipo_seguro, observaciones 
        FROM polizas WHERE agente_id = ? ORDER BY fecha_emision DESC
        """;
    
    private static final String SELECT_BY_ESTADO_SQL = """
        SELECT id_poliza, numero_poliza, fecha_emision, fecha_vencimiento, estado, 
               cliente_id, agente_id, prima, tipo_seguro, observaciones 
        FROM polizas WHERE estado = ? ORDER BY fecha_emision DESC
        """;
    
    private static final String SELECT_BY_FECHA_RANGE_SQL = """
        SELECT id_poliza, numero_poliza, fecha_emision, fecha_vencimiento, estado, 
               cliente_id, agente_id, prima, tipo_seguro, observaciones 
        FROM polizas WHERE fecha_emision BETWEEN ? AND ? ORDER BY fecha_emision DESC
        """;
    
    private static final String SELECT_ALL_SQL = """
        SELECT id_poliza, numero_poliza, fecha_emision, fecha_vencimiento, estado, 
               cliente_id, agente_id, prima, tipo_seguro, observaciones 
        FROM polizas ORDER BY fecha_emision DESC
        """;
    
    private static final String UPDATE_SQL = """
        UPDATE polizas SET numero_poliza = ?, fecha_emision = ?, fecha_vencimiento = ?, 
                          estado = ?, cliente_id = ?, agente_id = ?, prima = ?, 
                          tipo_seguro = ?, observaciones = ? 
        WHERE id_poliza = ?
        """;
    
    private static final String DELETE_SQL = "DELETE FROM polizas WHERE id_poliza = ?";
    
    private static final String EXISTS_SQL = "SELECT 1 FROM polizas WHERE id_poliza = ?";
    
    private static final String NEXT_NUMERO_SQL = """
        SELECT COALESCE(MAX(CAST(SUBSTRING(numero_poliza, 4) AS UNSIGNED)), 0) + 1 as next_num 
        FROM polizas WHERE numero_poliza LIKE 'POL%'
        """;
    
    @Override
    public Poliza create(Poliza poliza) {
        if (poliza.getIdPoliza() == null) {
            poliza.setIdPoliza(UUID.randomUUID());
        }
        
        if (poliza.getNumeroPoliza() == null || poliza.getNumeroPoliza().isEmpty()) {
            poliza.setNumeroPoliza(generateNextNumeroPoliza());
        }
        
        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(INSERT_SQL)) {
            
            stmt.setString(1, poliza.getIdPoliza().toString());
            stmt.setString(2, poliza.getNumeroPoliza());
            stmt.setTimestamp(3, Timestamp.valueOf(poliza.getFechaEmision()));
            stmt.setTimestamp(4, poliza.getFechaVencimiento() != null ? 
                             Timestamp.valueOf(poliza.getFechaVencimiento()) : null);
            stmt.setString(5, poliza.getEstado().name());
            stmt.setString(6, poliza.getClienteId().toString());
            stmt.setString(7, poliza.getAgenteId().toString());
            stmt.setBigDecimal(8, poliza.getPrima());
            stmt.setString(9, poliza.getTipoSeguro());
            stmt.setString(10, poliza.getObservaciones());
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Error al crear la póliza, no se insertaron filas");
            }
            
            return poliza;
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al crear póliza: " + e.getMessage(), e);
        }
    }
    
    @Override
    public Optional<Poliza> findById(UUID id) {
        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_BY_ID_SQL)) {
            
            stmt.setString(1, id.toString());
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRowToPoliza(rs));
                }
                return Optional.empty();
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar póliza por ID: " + e.getMessage(), e);
        }
    }
    
    @Override
    public Optional<Poliza> findByNumeroPoliza(String numeroPoliza) {
        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_BY_NUMERO_SQL)) {
            
            stmt.setString(1, numeroPoliza);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRowToPoliza(rs));
                }
                return Optional.empty();
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar póliza por número: " + e.getMessage(), e);
        }
    }
    
    @Override
    public List<Poliza> findByClienteId(UUID clienteId) {
        List<Poliza> polizas = new ArrayList<>();
        
        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_BY_CLIENTE_SQL)) {
            
            stmt.setString(1, clienteId.toString());
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    polizas.add(mapRowToPoliza(rs));
                }
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar pólizas por cliente: " + e.getMessage(), e);
        }
        
        return polizas;
    }
    
    @Override
    public List<Poliza> findByAgenteId(UUID agenteId) {
        List<Poliza> polizas = new ArrayList<>();
        
        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_BY_AGENTE_SQL)) {
            
            stmt.setString(1, agenteId.toString());
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    polizas.add(mapRowToPoliza(rs));
                }
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar pólizas por agente: " + e.getMessage(), e);
        }
        
        return polizas;
    }
    
    @Override
    public List<Poliza> findByEstado(EstadoPoliza estado) {
        List<Poliza> polizas = new ArrayList<>();
        
        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_BY_ESTADO_SQL)) {
            
            stmt.setString(1, estado.name());
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    polizas.add(mapRowToPoliza(rs));
                }
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar pólizas por estado: " + e.getMessage(), e);
        }
        
        return polizas;
    }
    
    @Override
    public List<Poliza> findByFechaEmisionBetween(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        List<Poliza> polizas = new ArrayList<>();
        
        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_BY_FECHA_RANGE_SQL)) {
            
            stmt.setTimestamp(1, Timestamp.valueOf(fechaInicio));
            stmt.setTimestamp(2, Timestamp.valueOf(fechaFin));
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    polizas.add(mapRowToPoliza(rs));
                }
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar pólizas por rango de fechas: " + e.getMessage(), e);
        }
        
        return polizas;
    }
    
    @Override
    public List<Poliza> findAll() {
        List<Poliza> polizas = new ArrayList<>();
        
        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_ALL_SQL);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                polizas.add(mapRowToPoliza(rs));
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener todas las pólizas: " + e.getMessage(), e);
        }
        
        return polizas;
    }
    
    @Override
    public Poliza update(Poliza poliza) {
        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(UPDATE_SQL)) {
            
            stmt.setString(1, poliza.getNumeroPoliza());
            stmt.setTimestamp(2, Timestamp.valueOf(poliza.getFechaEmision()));
            stmt.setTimestamp(3, poliza.getFechaVencimiento() != null ? 
                             Timestamp.valueOf(poliza.getFechaVencimiento()) : null);
            stmt.setString(4, poliza.getEstado().name());
            stmt.setString(5, poliza.getClienteId().toString());
            stmt.setString(6, poliza.getAgenteId().toString());
            stmt.setBigDecimal(7, poliza.getPrima());
            stmt.setString(8, poliza.getTipoSeguro());
            stmt.setString(9, poliza.getObservaciones());
            stmt.setString(10, poliza.getIdPoliza().toString());
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Error al actualizar la póliza, póliza no encontrada");
            }
            
            return poliza;
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar póliza: " + e.getMessage(), e);
        }
    }
    
    @Override
    public boolean deleteById(UUID id) {
        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(DELETE_SQL)) {
            
            stmt.setString(1, id.toString());
            
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar póliza: " + e.getMessage(), e);
        }
    }
    
    @Override
    public boolean existsById(UUID id) {
        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(EXISTS_SQL)) {
            
            stmt.setString(1, id.toString());
            
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al verificar existencia de la póliza: " + e.getMessage(), e);
        }
    }
    
    @Override
    public String generateNextNumeroPoliza() {
        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(NEXT_NUMERO_SQL);
             ResultSet rs = stmt.executeQuery()) {
            
            if (rs.next()) {
                int nextNum = rs.getInt("next_num");
                return String.format("POL%06d", nextNum);
            }
            return "POL000001";
            
        } catch (SQLException e) {
            // En caso de error, generar un número basado en timestamp
            return "POL" + System.currentTimeMillis() % 1000000;
        }
    }
    
    /**
     * Mapea una fila de ResultSet a un objeto Poliza
     */
    private Poliza mapRowToPoliza(ResultSet rs) throws SQLException {
        Poliza poliza = new Poliza();
        poliza.setIdPoliza(UUID.fromString(rs.getString("id_poliza")));
        poliza.setNumeroPoliza(rs.getString("numero_poliza"));
        
        Timestamp fechaEmision = rs.getTimestamp("fecha_emision");
        if (fechaEmision != null) {
            poliza.setFechaEmision(fechaEmision.toLocalDateTime());
        }
        
        Timestamp fechaVencimiento = rs.getTimestamp("fecha_vencimiento");
        if (fechaVencimiento != null) {
            poliza.setFechaVencimiento(fechaVencimiento.toLocalDateTime());
        }
        
        poliza.setEstado(EstadoPoliza.valueOf(rs.getString("estado")));
        poliza.setClienteId(UUID.fromString(rs.getString("cliente_id")));
        poliza.setAgenteId(UUID.fromString(rs.getString("agente_id")));
        
        BigDecimal prima = rs.getBigDecimal("prima");
        if (prima != null) {
            poliza.setPrima(prima);
        }
        
        poliza.setTipoSeguro(rs.getString("tipo_seguro"));
        poliza.setObservaciones(rs.getString("observaciones"));
        
        return poliza;
    }
}
