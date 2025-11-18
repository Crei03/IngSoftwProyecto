package com.seguratuauto.service.impl;

import com.seguratuauto.dao.ClienteRepository;
import com.seguratuauto.model.Cliente;
import com.seguratuauto.service.ClienteService;
import com.seguratuauto.service.EmailVerificationService;
import com.seguratuauto.service.PasswordResetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.UUID;

/**
 * Implementación del servicio de clientes usando Spring Data JPA
 */
@Service
@Transactional
public class ClienteServiceImpl implements ClienteService {
    
    private static final Random random = new Random();
    private final ClienteRepository clienteRepository;
    private final EmailVerificationService emailVerificationService;
    private final PasswordResetService passwordResetService;
    private final PasswordEncoder passwordEncoder;
    private final long verificationTtlHours;
    private final long passwordResetTtlHours;
    
    @Autowired
    public ClienteServiceImpl(ClienteRepository clienteRepository,
                              EmailVerificationService emailVerificationService,
                              PasswordResetService passwordResetService,
                              PasswordEncoder passwordEncoder,
                              @Value("${app.verification.expiration-hours:24}") long verificationTtlHours,
                              @Value("${app.password-reset.expiration-hours:2}") long passwordResetTtlHours) {
        this.clienteRepository = clienteRepository;
        this.emailVerificationService = emailVerificationService;
        this.passwordResetService = passwordResetService;
        this.passwordEncoder = passwordEncoder;
        this.verificationTtlHours = verificationTtlHours;
        this.passwordResetTtlHours = passwordResetTtlHours;
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
        
        // Validar contraseña
        if (cliente.getPassword() == null || cliente.getPassword().trim().isEmpty()) {
            throw new IllegalArgumentException("La contraseña es obligatoria");
        }
        
        // Inicializar estado de verificación
        cliente.setVerificado(false);
        cliente.setTokenVerificacion(UUID.randomUUID().toString().replace("-", ""));
        cliente.setTokenExpira(LocalDateTime.now().plusHours(verificationTtlHours));
        cliente.setFechaVerificacion(null);
        cliente.setPassword(passwordEncoder.encode(cliente.getPassword()));
        
        Cliente clienteCreado = clienteRepository.save(cliente);
        
        // Enviar correo de verificación (solo si el cliente proporcionó email)
        if (clienteCreado.getEmail() != null) {
            emailVerificationService.enviarCorreoConfirmacion(clienteCreado);
        }
        
        return clienteCreado;
        
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
        
        // Si la contraseña viene en texto plano (por ejemplo en una actualización manual), encriptarla
        if (cliente.getPassword() != null && !cliente.getPassword().startsWith("$2")) {
            cliente.setPassword(passwordEncoder.encode(cliente.getPassword()));
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
        
        if (cliente.getPassword() == null || cliente.getPassword().trim().isEmpty()) {
            return false;
        }
        
        return true;
    }
    
    @Override
    public Cliente verificarCliente(String token) {
        if (token == null || token.trim().isEmpty()) {
            throw new IllegalArgumentException("El token de verificación es obligatorio");
        }
        
        Cliente cliente = clienteRepository.findByTokenVerificacion(token)
                .orElseThrow(() -> new IllegalArgumentException("El token de verificación no es válido"));
        
        if (cliente.isVerificado()) {
            return cliente;
        }
        
        if (cliente.getTokenExpira() != null && cliente.getTokenExpira().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("El token de verificación ha expirado. Registra una nueva cuenta.");
        }
        
        cliente.setVerificado(true);
        cliente.setFechaVerificacion(LocalDateTime.now());
        cliente.setTokenVerificacion(null);
        cliente.setTokenExpira(null);
        
        return clienteRepository.save(cliente);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Cliente autenticarCliente(String email, String password) {
        if (email == null || password == null) {
            throw new IllegalArgumentException("Email y contraseña son obligatorios");
        }
        
        Cliente cliente = clienteRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Credenciales inválidas"));
        
        if (!cliente.isVerificado()) {
            throw new IllegalStateException("Debes confirmar tu correo antes de iniciar sesión");
        }
        
        if (!passwordEncoder.matches(password, cliente.getPassword())) {
            throw new IllegalArgumentException("Credenciales inválidas");
        }
        
        return cliente;
    }
    
    @Override
    public void solicitarRecuperacionPassword(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("El email es obligatorio");
        }
        
        Cliente cliente = clienteRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("No existe un cliente con este email"));
        
        String token = UUID.randomUUID().toString().replace("-", "").substring(0, 16);
        cliente.setResetPasswordToken(token);
        cliente.setResetTokenExpira(LocalDateTime.now().plusHours(passwordResetTtlHours));
        clienteRepository.save(cliente);
        
        passwordResetService.enviarCorreoRecuperacion(cliente, token);
    }
    
    @Override
    public void restablecerPassword(String token, String nuevaPassword) {
        if (token == null || token.trim().isEmpty()) {
            throw new IllegalArgumentException("El token es obligatorio");
        }
        if (nuevaPassword == null || nuevaPassword.trim().isEmpty()) {
            throw new IllegalArgumentException("La contraseña es obligatoria");
        }
        
        Cliente cliente = clienteRepository.findByResetPasswordToken(token)
                .orElseThrow(() -> new IllegalArgumentException("El token no es válido"));
        
        if (cliente.getResetTokenExpira() == null || cliente.getResetTokenExpira().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("El token ha expirado. Solicita un nuevo restablecimiento.");
        }
        
        cliente.setPassword(passwordEncoder.encode(nuevaPassword));
        cliente.setResetPasswordToken(null);
        cliente.setResetTokenExpira(null);
        clienteRepository.save(cliente);
    }
}
