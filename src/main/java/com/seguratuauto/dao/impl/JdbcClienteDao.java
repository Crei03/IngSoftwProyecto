package com.seguratuauto.dao.impl;

import com.seguratuauto.dao.ClienteDao;
import com.seguratuauto.model.Cliente;
import com.seguratuauto.util.JDBCUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Implementación JDBC del DAO para Cliente
 */
public class JdbcClienteDao implements ClienteDao {
    
    private static final String INSERT_SQL = 
        "INSERT INTO clientes (id_cliente, nombre, email, telefono) VALUES (?, ?, ?, ?)";
    
    private static final String SELECT_BY_ID_SQL = 
        "SELECT id_cliente, nombre, email, telefono FROM clientes WHERE id_cliente = ?";
    
    private static final String SELECT_BY_EMAIL_SQL = 
        "SELECT id_cliente, nombre, email, telefono FROM clientes WHERE email = ?";
    
    private static final String SELECT_ALL_SQL = 
        "SELECT id_cliente, nombre, email, telefono FROM clientes ORDER BY nombre";
    
    private static final String UPDATE_SQL = 
        "UPDATE clientes SET nombre = ?, email = ?, telefono = ? WHERE id_cliente = ?";
    
    private static final String DELETE_SQL = 
        "DELETE FROM clientes WHERE id_cliente = ?";
    
    private static final String EXISTS_SQL = 
        "SELECT 1 FROM clientes WHERE id_cliente = ?";
    
    @Override
    public Cliente create(Cliente cliente) {
        if (cliente.getIdCliente() == null) {
            cliente.setIdCliente(UUID.randomUUID());
        }
        
        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(INSERT_SQL)) {
            
            stmt.setString(1, cliente.getIdCliente().toString());
            stmt.setString(2, cliente.getNombre());
            stmt.setString(3, cliente.getEmail());
            stmt.setString(4, cliente.getTelefono());
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Error al crear el cliente, no se insertaron filas");
            }
            
            return cliente;
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al crear cliente: " + e.getMessage(), e);
        }
    }
    
    @Override
    public Optional<Cliente> findById(UUID id) {
        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_BY_ID_SQL)) {
            
            stmt.setString(1, id.toString());
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRowToCliente(rs));
                }
                return Optional.empty();
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar cliente por ID: " + e.getMessage(), e);
        }
    }
    
    @Override
    public Optional<Cliente> findByEmail(String email) {
        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_BY_EMAIL_SQL)) {
            
            stmt.setString(1, email);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRowToCliente(rs));
                }
                return Optional.empty();
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar cliente por email: " + e.getMessage(), e);
        }
    }
    
    @Override
    public List<Cliente> findAll() {
        List<Cliente> clientes = new ArrayList<>();
        
        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_ALL_SQL);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                clientes.add(mapRowToCliente(rs));
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener todos los clientes: " + e.getMessage(), e);
        }
        
        return clientes;
    }
    
    @Override
    public Cliente update(Cliente cliente) {
        try (Connection conn = JDBCUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(UPDATE_SQL)) {
            
            stmt.setString(1, cliente.getNombre());
            stmt.setString(2, cliente.getEmail());
            stmt.setString(3, cliente.getTelefono());
            stmt.setString(4, cliente.getIdCliente().toString());
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Error al actualizar el cliente, cliente no encontrado");
            }
            
            return cliente;
            
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar cliente: " + e.getMessage(), e);
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
            throw new RuntimeException("Error al eliminar cliente: " + e.getMessage(), e);
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
            throw new RuntimeException("Error al verificar existencia del cliente: " + e.getMessage(), e);
        }
    }
    
    /**
     * Mapea una fila de ResultSet a un objeto Cliente
     */
    private Cliente mapRowToCliente(ResultSet rs) throws SQLException {
        Cliente cliente = new Cliente();
        cliente.setIdCliente(UUID.fromString(rs.getString("id_cliente")));
        cliente.setNombre(rs.getString("nombre"));
        cliente.setEmail(rs.getString("email"));
        cliente.setTelefono(rs.getString("telefono"));
        return cliente;
    }
}
