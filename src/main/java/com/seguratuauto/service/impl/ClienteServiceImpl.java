package com.seguratuauto.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.seguratuauto.api.dto.LoginResponse;
import com.seguratuauto.service.ClienteService;
import com.seguratuauto.model.Cliente;
import com.seguratuauto.dao.ClienteRepository;
import java.util.List;
import java.util.Optional;

@Service
public class ClienteServiceImpl implements ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Cliente crearCliente(Cliente cliente) {
        if (cliente == null) {
            throw new IllegalArgumentException("El cliente no puede ser nulo");
        }
        return clienteRepository.save(cliente);
    }

    @Override
    public Cliente obtenerClientePorId(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("El ID debe ser válido");
        }
        return clienteRepository.findById(id).orElse(null);
    }

    @Override
    public List<Cliente> obtenerTodosLosClientes() {
        return clienteRepository.findAll();
    }

    @Override
    public Cliente actualizarCliente(Long id, Cliente cliente) {
        if (id == null || cliente == null) {
            throw new IllegalArgumentException("Los parámetros no pueden ser nulos");
        }
        Optional<Cliente> existente = clienteRepository.findById(id);
        if (existente.isPresent()) {
            Cliente c = existente.get();
            c.setNombre(cliente.getNombre());
            c.setCorreo(cliente.getCorreo());
            c.setTelefono(cliente.getTelefono());
            return clienteRepository.save(c);
        }
        return null;
    }

    @Override
    public void eliminarCliente(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("El ID debe ser válido");
        }
        clienteRepository.deleteById(id);
    }

    @Override
    public Cliente obtenerClientePorCorreo(String correo) {
        if (correo == null || correo.isEmpty()) {
            throw new IllegalArgumentException("El correo no puede estar vacío");
        }
        return clienteRepository.findByCorreo(correo).orElse(null);
    }

    @Override
    public LoginResponse autenticar(String email, String password) {
        try {
            Optional<Cliente> cliente = clienteRepository.findByCorreo(email);
            if (cliente.isEmpty()) {
                return new LoginResponse(null, null, null, null, false, "Cliente no encontrado");
            }

            Cliente c = cliente.get();
            // En una implementación real, compararíamos la contraseña encriptada
            // Por ahora, aceptamos cualquier contraseña para pruebas
            if (password != null && !password.isEmpty()) {
                return new LoginResponse(
                    c.getIdCliente(),
                    c.getNombre(),
                    c.getCorreo(),
                    "token-jwt-aqui",
                    true,
                    "Autenticación exitosa"
                );
            }
            return new LoginResponse(null, null, null, null, false, "Contraseña inválida");
        } catch (Exception e) {
            return new LoginResponse(null, null, null, null, false, "Error en autenticación: " + e.getMessage());
        }
    }
}
