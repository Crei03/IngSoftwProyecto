package com.seguratuauto.service.impl;

import com.seguratuauto.dao.AgenteRepository;
import com.seguratuauto.model.Agente;
import com.seguratuauto.service.AgenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Random;

/**
 * Implementación del servicio de agentes usando Spring Data JPA
 */
@Service
@Transactional
public class AgenteServiceImpl implements AgenteService {
    
    private static final Random random = new Random();
    private final AgenteRepository agenteRepository;
    
    @Autowired
    public AgenteServiceImpl(AgenteRepository agenteRepository) {
        this.agenteRepository = agenteRepository;
    }
    
    @Override
    public Agente crearAgente(Agente agente) {
        // Validar los datos del agente
        if (!validarAgente(agente)) {
            throw new IllegalArgumentException("Los datos del agente no son válidos");
        }
        
        // Verificar que no exista un agente con el mismo código
        if (agente.getCodigo() != null && !agente.getCodigo().trim().isEmpty()) {
            if (agenteRepository.existsByCodigo(agente.getCodigo())) {
                throw new IllegalArgumentException("Ya existe un agente con este código: " + agente.getCodigo());
            }
        }
        
        // Verificar que no exista un agente con el mismo email
        if (agente.getEmail() != null && !agente.getEmail().trim().isEmpty()) {
            if (agenteRepository.existsByEmail(agente.getEmail())) {
                throw new IllegalArgumentException("Ya existe un agente con este email: " + agente.getEmail());
            }
        }
        
        // Verificar que no exista un agente con el mismo teléfono
        if (agente.getTelefono() != null && !agente.getTelefono().trim().isEmpty()) {
            if (agenteRepository.existsByTelefono(agente.getTelefono())) {
                throw new IllegalArgumentException("Ya existe un agente con este teléfono: " + agente.getTelefono());
            }
        }
        
        // Asignar ID si no tiene
        if (agente.getIdAgente() == null) {
            agente.setIdAgente(Math.abs(random.nextLong()));
        }
        
        // Generar código si no tiene
        if (agente.getCodigo() == null || agente.getCodigo().trim().isEmpty()) {
            agente.setCodigo(generarSiguienteCodigoAgente());
        }
        
        return agenteRepository.save(agente);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Agente buscarAgentePorId(Long id) {
        return agenteRepository.findById(id).orElse(null);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Agente buscarAgentePorCodigo(String codigo) {
        if (codigo == null || codigo.trim().isEmpty()) {
            return null;
        }
        return agenteRepository.findByCodigo(codigo).orElse(null);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Agente buscarAgentePorEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return null;
        }
        return agenteRepository.findByEmail(email).orElse(null);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Agente buscarAgentePorTelefono(String telefono) {
        if (telefono == null || telefono.trim().isEmpty()) {
            return null;
        }
        return agenteRepository.findByTelefono(telefono).orElse(null);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Agente> buscarAgentesPorNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            return List.of();
        }
        return agenteRepository.findByNombreContainingIgnoreCase(nombre);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Agente> obtenerTodosLosAgentes() {
        return agenteRepository.findAll();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Agente> obtenerAgentesActivos() {
        return agenteRepository.findAgentesActivos();
    }
    
    @Override
    public Agente actualizarAgente(Agente agente) {
        // Validar que el agente tenga ID
        if (agente.getIdAgente() == null) {
            throw new IllegalArgumentException("El agente debe tener un ID para ser actualizado");
        }
        
        // Verificar que el agente existe
        if (!agenteRepository.existsById(agente.getIdAgente())) {
            throw new IllegalArgumentException("El agente especificado no existe");
        }
        
        // Validar los datos del agente
        if (!validarAgente(agente)) {
            throw new IllegalArgumentException("Los datos del agente no son válidos");
        }
        
        // Verificar unicidad de código (excluyendo el agente actual)
        if (agente.getCodigo() != null && !agente.getCodigo().trim().isEmpty()) {
            Agente agenteExistenteCodigo = agenteRepository.findByCodigo(agente.getCodigo()).orElse(null);
            if (agenteExistenteCodigo != null && !agenteExistenteCodigo.getIdAgente().equals(agente.getIdAgente())) {
                throw new IllegalArgumentException("Ya existe otro agente con este código: " + agente.getCodigo());
            }
        }
        
        // Verificar unicidad de email (excluyendo el agente actual)
        if (agente.getEmail() != null && !agente.getEmail().trim().isEmpty()) {
            Agente agenteExistenteEmail = agenteRepository.findByEmail(agente.getEmail()).orElse(null);
            if (agenteExistenteEmail != null && !agenteExistenteEmail.getIdAgente().equals(agente.getIdAgente())) {
                throw new IllegalArgumentException("Ya existe otro agente con este email: " + agente.getEmail());
            }
        }
        
        // Verificar unicidad de teléfono (excluyendo el agente actual)
        if (agente.getTelefono() != null && !agente.getTelefono().trim().isEmpty()) {
            Agente agenteExistenteTelefono = agenteRepository.findByTelefono(agente.getTelefono()).orElse(null);
            if (agenteExistenteTelefono != null && !agenteExistenteTelefono.getIdAgente().equals(agente.getIdAgente())) {
                throw new IllegalArgumentException("Ya existe otro agente con este teléfono: " + agente.getTelefono());
            }
        }
        
        return agenteRepository.save(agente);
    }
    
    @Override
    public boolean eliminarAgente(Long id) {
        if (id == null) {
            return false;
        }
        
        if (!agenteRepository.existsById(id)) {
            return false;
        }
        
        // Verificar si el agente tiene pólizas asignadas
        long polizasAsignadas = agenteRepository.countPolizasByAgente(id);
        if (polizasAsignadas > 0) {
            throw new IllegalStateException("No se puede eliminar el agente. Tiene " + polizasAsignadas + " pólizas asignadas.");
        }
        
        try {
            agenteRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            throw new IllegalStateException("No se puede eliminar el agente.", e);
        }
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean existeAgente(Long id) {
        return id != null && agenteRepository.existsById(id);
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean existeAgentePorCodigo(String codigo) {
        return codigo != null && !codigo.trim().isEmpty() && agenteRepository.existsByCodigo(codigo);
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean existeAgentePorEmail(String email) {
        return email != null && !email.trim().isEmpty() && agenteRepository.existsByEmail(email);
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean existeAgentePorTelefono(String telefono) {
        return telefono != null && !telefono.trim().isEmpty() && agenteRepository.existsByTelefono(telefono);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Agente> buscarAgentesPorCriterios(String nombre, String codigo, String email) {
        return agenteRepository.findByMultipleCriteria(nombre, codigo, email);
    }
    
    @Override
    @Transactional(readOnly = true)
    public long contarAgentes() {
        return agenteRepository.countTotalAgentes();
    }
    
    @Override
    @Transactional(readOnly = true)
    public long contarPolizasPorAgente(Long agenteId) {
        if (agenteId == null) {
            return 0;
        }
        return agenteRepository.countPolizasByAgente(agenteId);
    }
    
    @Override
    public String generarSiguienteCodigoAgente() {
        // Buscar el último código de agente
        String ultimoCodigo = agenteRepository.findLastCodigo().orElse("AGT000");
        
        // Extraer el número secuencial
        String prefijo = "AGT";
        int siguienteNumero = 1;
        
        if (ultimoCodigo.startsWith(prefijo)) {
            try {
                String numeroStr = ultimoCodigo.substring(prefijo.length());
                int numeroActual = Integer.parseInt(numeroStr);
                siguienteNumero = numeroActual + 1;
            } catch (NumberFormatException e) {
                // Si hay error, comenzar desde 1
                siguienteNumero = 1;
            }
        }
        
        // Formatear con ceros a la izquierda
        return String.format("%s%03d", prefijo, siguienteNumero);
    }
    
    @Override
    public boolean validarAgente(Agente agente) {
        if (agente == null) {
            return false;
        }
        
        // Validar nombre (obligatorio)
        if (agente.getNombre() == null || agente.getNombre().trim().isEmpty()) {
            return false;
        }
        
        // Validar longitud del nombre
        if (agente.getNombre().length() > 100) {
            return false;
        }
        
        // Validar código si está presente
        if (agente.getCodigo() != null && !agente.getCodigo().trim().isEmpty()) {
            if (agente.getCodigo().length() > 20) {
                return false;
            }
            // Validar formato de código (alfanumérico)
            if (!agente.getCodigo().matches("^[A-Za-z0-9]+$")) {
                return false;
            }
        }
        
        // Validar email si está presente
        if (agente.getEmail() != null && !agente.getEmail().trim().isEmpty()) {
            if (agente.getEmail().length() > 150) {
                return false;
            }
            // Validación básica de formato de email
            if (!agente.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
                return false;
            }
        }
        
        // Validar teléfono si está presente
        if (agente.getTelefono() != null && !agente.getTelefono().trim().isEmpty()) {
            if (agente.getTelefono().length() > 20) {
                return false;
            }
        }
        
        return true;
    }
}
