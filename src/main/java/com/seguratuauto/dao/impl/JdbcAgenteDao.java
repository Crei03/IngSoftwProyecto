package com.seguratuauto.dao.impl;

import com.seguratuauto.dao.AgenteDao;
import com.seguratuauto.model.Agente;
import com.seguratuauto.util.JDBCUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Implementación JDBC del DAO para Agente
 */
public class JdbcAgenteDao implements AgenteDao {
    
    private static final String INSERT_SQL = 
        "INSERT INTO agentes (id_agente, nombre, codigo, email, telefono) VALUES (?, ?, ?, ?, ?)";
    
    private static final String SELECT_BY_ID_SQL = 
        "SELECT id_agente, nombre, codigo, email, telefono FROM agentes WHERE id_agente = ?";
    
    private static final String SELECT_BY_CODIGO_SQL = 
        "SELECT id_agente, nombre, codigo, email, telefono FROM agentes WHERE codigo = ?";
    
    private static final String SELECT_BY_EMAIL_SQL = 
        "SELECT id_agente, nombre, codigo, email, telefono FROM agentes WHERE email = ?";
    
    private static final String SELECT_ALL_SQL = 
        "SELECT id_agente, nombre, codigo, email, telefono FROM agentes ORDER BY nombre";
    
    private static final String UPDATE_SQL = 
        "UPDATE agentes SET nombre = ?, codigo = ?, email = ?, telefono = ? WHERE id_agente = ?";
    
    private static final String DELETE_SQL = 
        "DELETE FROM agentes WHERE id_agente = ?";
    
    private static final String EXISTS_SQL = 
        "SELECT 1 FROM agentes WHERE id_agente = ?";
    
    @Override
    public Agente create(Agente agente) {
        if (agente.getIdAgente() == null) {
            agente.setIdAgente(UUID.randomUUID());
        }
        
        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(INSERT_SQL)) {
            
            stmt.setString(1, agente.getIdAgente().toString());
            stmt.setString(2, agente.getNombre());
            stmt.setString(3, agente.getCodigo());
            stmt.setString(4, agente.getEmail());
            stmt.setString(5, agente.getTelefono());
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Error al crear el agente, no se insertaron filas");
            }
            
            return agente;
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al crear agente: " + e.getMessage(), e);
        }
    }
    
    @Override
    public Optional<Agente> findById(UUID id) {
        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_BY_ID_SQL)) {
            
            stmt.setString(1, id.toString());
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRowToAgente(rs));
                }
                return Optional.empty();
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar agente por ID: " + e.getMessage(), e);
        }
    }
    
    @Override
    public Optional<Agente> findByCodigo(String codigo) {
        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_BY_CODIGO_SQL)) {
            
            stmt.setString(1, codigo);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRowToAgente(rs));
                }
                return Optional.empty();
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar agente por código: " + e.getMessage(), e);
        }
    }
    
    @Override
    public Optional<Agente> findByEmail(String email) {
        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_BY_EMAIL_SQL)) {
            
            stmt.setString(1, email);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRowToAgente(rs));
                }
                return Optional.empty();
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar agente por email: " + e.getMessage(), e);
        }
    }
    
    @Override
    public List<Agente> findAll() {
        List<Agente> agentes = new ArrayList<>();
        
        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_ALL_SQL);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                agentes.add(mapRowToAgente(rs));
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener todos los agentes: " + e.getMessage(), e);
        }
        
        return agentes;
    }
    
    @Override
    public Agente update(Agente agente) {
        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(UPDATE_SQL)) {
            
            stmt.setString(1, agente.getNombre());
            stmt.setString(2, agente.getCodigo());
            stmt.setString(3, agente.getEmail());
            stmt.setString(4, agente.getTelefono());
            stmt.setString(5, agente.getIdAgente().toString());
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Error al actualizar el agente, agente no encontrado");
            }
            
            return agente;
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar agente: " + e.getMessage(), e);
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
            throw new RuntimeException("Error al eliminar agente: " + e.getMessage(), e);
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
            throw new RuntimeException("Error al verificar existencia del agente: " + e.getMessage(), e);
        }
    }
    
    /**
     * Mapea una fila de ResultSet a un objeto Agente
     */
    private Agente mapRowToAgente(ResultSet rs) throws SQLException {
        Agente agente = new Agente();
        agente.setIdAgente(UUID.fromString(rs.getString("id_agente")));
        agente.setNombre(rs.getString("nombre"));
        agente.setCodigo(rs.getString("codigo"));
        agente.setEmail(rs.getString("email"));
        agente.setTelefono(rs.getString("telefono"));
        return agente;
    }
}
