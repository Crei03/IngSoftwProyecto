package com.seguratuauto.service.impl;

import com.seguratuauto.dao.ClienteRepository;
import com.seguratuauto.model.Cliente;
import com.seguratuauto.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Random;

/**
 * Implementación del servicio de clientes usando Spring Data JPA
 */
@Service
@Transactional
public class ClienteServiceImpl implements ClienteService {
    
    private static final Random random = new Random();
    private final ClienteRepository clienteRepository;
    
    @Autowired
    public ClienteServiceImpl(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }
    
    @Override
    public Cliente crearCliente(Cliente cliente) {
        // Validar los datos del cliente
        if (!validarCliente(cliente)) {
            throw new IllegalArgumentException("Los datos del cliente no son válidos");
        }
        
        // Verificar que no exista un cliente con el mismo email
        if (cliente.getEmail() != null && !cliente.getEmail().trim().isEmpty()) {
            if (clienteRepository.existsByEmail(cliente.getEmail())) {
                throw new IllegalArgumentException("Ya existe un cliente con este email: " + cliente.getEmail());
            }
        }
        
        // Verificar que no exista un cliente con el mismo teléfono
        if (cliente.getTelefono() != null && !cliente.getTelefono().trim().isEmpty()) {
            if (clienteRepository.existsByTelefono(cliente.getTelefono())) {
                throw new IllegalArgumentException("Ya existe un cliente con este teléfono: " + cliente.getTelefono());
            }
        }
        
        return clienteRepository.save(cliente);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Cliente buscarClientePorId(Long id) {
        return clienteRepository.findById(id).orElse(null);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Cliente buscarClientePorEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return null;
        }
        return clienteRepository.findByEmail(email).orElse(null);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Cliente buscarClientePorTelefono(String telefono) {
        if (telefono == null || telefono.trim().isEmpty()) {
            return null;
        }
        return clienteRepository.findByTelefono(telefono).orElse(null);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Cliente> buscarClientesPorNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            return List.of();
        }
        return clienteRepository.findByNombreContainingIgnoreCase(nombre);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Cliente> obtenerTodosLosClientes() {
        return clienteRepository.findAll();
    }
    
    @Override
    public Cliente actualizarCliente(Cliente cliente) {
        // Validar que el cliente tenga ID
        if (cliente.getIdCliente() == null) {
            throw new IllegalArgumentException("El cliente debe tener un ID para ser actualizado");
        }
        
        // Verificar que el cliente existe
        if (!clienteRepository.existsById(cliente.getIdCliente())) {
            throw new IllegalArgumentException("El cliente especificado no existe");
        }
        
        // Validar los datos del cliente
        if (!validarCliente(cliente)) {
            throw new IllegalArgumentException("Los datos del cliente no son válidos");
        }
        
        // Verificar unicidad de email (excluyendo el cliente actual)
        if (cliente.getEmail() != null && !cliente.getEmail().trim().isEmpty()) {
            Cliente clienteExistenteEmail = clienteRepository.findByEmail(cliente.getEmail()).orElse(null);
            if (clienteExistenteEmail != null && !clienteExistenteEmail.getIdCliente().equals(cliente.getIdCliente())) {
                throw new IllegalArgumentException("Ya existe otro cliente con este email: " + cliente.getEmail());
            }
        }
        
        // Verificar unicidad de teléfono (excluyendo el cliente actual)
        if (cliente.getTelefono() != null && !cliente.getTelefono().trim().isEmpty()) {
            Cliente clienteExistenteTelefono = clienteRepository.findByTelefono(cliente.getTelefono()).orElse(null);
            if (clienteExistenteTelefono != null && !clienteExistenteTelefono.getIdCliente().equals(cliente.getIdCliente())) {
                throw new IllegalArgumentException("Ya existe otro cliente con este teléfono: " + cliente.getTelefono());
            }
        }
        
        return clienteRepository.save(cliente);
    }
    
    @Override
    public boolean eliminarCliente(Long id) {
        if (id == null) {
            return false;
        }
        
        if (!clienteRepository.existsById(id)) {
            return false;
        }
        
        try {
            clienteRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            // En caso de restricciones de FK u otros errores
            throw new IllegalStateException("No se puede eliminar el cliente. Puede tener pólizas asociadas.", e);
        }
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean existeCliente(Long id) {
        return id != null && clienteRepository.existsById(id);
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean existeClientePorEmail(String email) {
        return email != null && !email.trim().isEmpty() && clienteRepository.existsByEmail(email);
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean existeClientePorTelefono(String telefono) {
        return telefono != null && !telefono.trim().isEmpty() && clienteRepository.existsByTelefono(telefono);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Cliente> buscarClientesPorCriterios(String nombre, String email) {
        return clienteRepository.findByMultipleCriteria(nombre, email);
    }
    
    @Override
    @Transactional(readOnly = true)
    public long contarClientes() {
        return clienteRepository.countTotalClientes();
    }
    
    @Override
    public boolean validarCliente(Cliente cliente) {
        if (cliente == null) {
            return false;
        }
        
        // Validar nombre (obligatorio)
        if (cliente.getNombre() == null || cliente.getNombre().trim().isEmpty()) {
            return false;
        }
        
        // Validar longitud del nombre
        if (cliente.getNombre().length() > 100) {
            return false;
        }
        
        // Validar email si está presente
        if (cliente.getEmail() != null && !cliente.getEmail().trim().isEmpty()) {
            if (cliente.getEmail().length() > 150) {
                return false;
            }
            // Validación básica de formato de email
            if (!cliente.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
                return false;
            }
        }
        
        // Validar teléfono si está presente
        if (cliente.getTelefono() != null && !cliente.getTelefono().trim().isEmpty()) {
            if (cliente.getTelefono().length() > 20) {
                return false;
            }
        }
        
        return true;
    }
}
