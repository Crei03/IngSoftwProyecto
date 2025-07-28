package com.seguratuauto.service.impl;

import com.seguratuauto.dao.EvaluadorRepository;
import com.seguratuauto.model.Evaluador;
import com.seguratuauto.service.EvaluadorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Implementación del servicio de evaluadores
 */
@Service
@Transactional
public class EvaluadorServiceImpl implements EvaluadorService {
    
    private final EvaluadorRepository evaluadorRepository;
    
    @Autowired
    public EvaluadorServiceImpl(EvaluadorRepository evaluadorRepository) {
        this.evaluadorRepository = evaluadorRepository;
    }
    
    @Override
    public Evaluador crearEvaluador(Evaluador evaluador) {
        // Validar datos
        validarEvaluador(evaluador, false);
        
        // Generar código automáticamente si no se proporciona
        if (evaluador.getCodigo() == null || evaluador.getCodigo().trim().isEmpty()) {
            evaluador.setCodigo(generarNuevoCodigo());
        }
        
        // Establecer valores por defecto
        if (evaluador.getActivo() == null) {
            evaluador.setActivo(true);
        }
        
        if (evaluador.getFechaIngreso() == null) {
            evaluador.setFechaIngreso(LocalDateTime.now());
        }
        
        return evaluadorRepository.save(evaluador);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Evaluador buscarEvaluadorPorId(Long id) {
        return evaluadorRepository.findById(id).orElse(null);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Evaluador buscarEvaluadorPorCodigo(String codigo) {
        return evaluadorRepository.findByCodigo(codigo).orElse(null);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Evaluador buscarEvaluadorPorEmail(String email) {
        return evaluadorRepository.findByEmail(email).orElse(null);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Evaluador> buscarEvaluadoresPorNombre(String nombre) {
        return evaluadorRepository.findByNombreContainingIgnoreCase(nombre);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Evaluador> buscarEvaluadoresPorEspecialidad(String especialidad) {
        return evaluadorRepository.findByEspecialidadContainingIgnoreCase(especialidad);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Evaluador> obtenerTodosLosEvaluadores() {
        return evaluadorRepository.findAll();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Evaluador> obtenerEvaluadoresActivos() {
        return evaluadorRepository.findByActivo(true);
    }
    
    @Override
    public Evaluador actualizarEvaluador(Long id, Evaluador evaluadorActualizado) {
        Evaluador evaluadorExistente = buscarEvaluadorPorId(id);
        if (evaluadorExistente == null) {
            throw new IllegalArgumentException("Evaluador no encontrado con ID: " + id);
        }
        
        // Preservar ID y fecha de ingreso original
        evaluadorActualizado.setIdEvaluador(id);
        evaluadorActualizado.setFechaIngreso(evaluadorExistente.getFechaIngreso());
        
        // Validar datos actualizados
        validarEvaluador(evaluadorActualizado, true);
        
        return evaluadorRepository.save(evaluadorActualizado);
    }
    
    @Override
    public Evaluador eliminarEvaluador(Long id) {
        Evaluador evaluador = buscarEvaluadorPorId(id);
        if (evaluador == null) {
            throw new IllegalArgumentException("Evaluador no encontrado con ID: " + id);
        }
        
        // Eliminación lógica: marcar como inactivo
        evaluador.setActivo(false);
        return evaluadorRepository.save(evaluador);
    }
    
    @Override
    public void eliminarEvaluadorPermanente(Long id) {
        if (!existeEvaluador(id)) {
            throw new IllegalArgumentException("Evaluador no encontrado con ID: " + id);
        }
        evaluadorRepository.deleteById(id);
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean existeEvaluador(Long id) {
        return evaluadorRepository.existsById(id);
    }
    
    @Override
    @Transactional(readOnly = true)
    public long contarTotalEvaluadores() {
        return evaluadorRepository.count();
    }
    
    @Override
    @Transactional(readOnly = true)
    public long contarEvaluadoresActivos() {
        return evaluadorRepository.countByActivo(true);
    }
    
    @Override
    @Transactional(readOnly = true)
    public String generarNuevoCodigo() {
        String ultimoCodigo = evaluadorRepository.findLastGeneratedCodigo().orElse("EV0000");
        
        // Extraer el número del último código (formato: EV####)
        String numeroStr = ultimoCodigo.substring(2);
        int numero = Integer.parseInt(numeroStr);
        
        // Incrementar y formatear
        numero++;
        return String.format("EV%04d", numero);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Evaluador> buscarEvaluadoresConCriterios(String nombre, String codigo, String email, String especialidad) {
        return evaluadorRepository.findByMultipleCriteria(nombre, codigo, email, especialidad);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Evaluador> buscarEvaluadoresPorNombreYEmail(String nombre, String email) {
        return evaluadorRepository.findByNombreContainingIgnoreCaseAndEmail(nombre, email);
    }
    
    @Override
    public Evaluador reactivarEvaluador(Long id) {
        Evaluador evaluador = buscarEvaluadorPorId(id);
        if (evaluador == null) {
            throw new IllegalArgumentException("Evaluador no encontrado con ID: " + id);
        }
        
        evaluador.setActivo(true);
        return evaluadorRepository.save(evaluador);
    }
    
    @Override
    public void validarEvaluador(Evaluador evaluador, boolean esActualizacion) {
        if (evaluador == null) {
            throw new IllegalArgumentException("Los datos del evaluador son requeridos");
        }
        
        // Validar nombre
        if (evaluador.getNombre() == null || evaluador.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del evaluador es obligatorio");
        }
        
        if (evaluador.getNombre().length() > 100) {
            throw new IllegalArgumentException("El nombre no puede exceder 100 caracteres");
        }
        
        // Validar código (si se proporciona)
        if (evaluador.getCodigo() != null && !evaluador.getCodigo().trim().isEmpty()) {
            if (evaluador.getCodigo().length() > 20) {
                throw new IllegalArgumentException("El código no puede exceder 20 caracteres");
            }
            
            if (!evaluador.getCodigo().matches("^[A-Za-z0-9]*$")) {
                throw new IllegalArgumentException("El código solo puede contener letras y números");
            }
            
            // Verificar unicidad del código
            if (esActualizacion) {
                if (evaluadorRepository.existsByCodigoAndIdEvaluadorNot(evaluador.getCodigo(), evaluador.getIdEvaluador())) {
                    throw new IllegalArgumentException("Ya existe otro evaluador con el código: " + evaluador.getCodigo());
                }
            } else {
                if (evaluadorRepository.findByCodigo(evaluador.getCodigo()).isPresent()) {
                    throw new IllegalArgumentException("Ya existe un evaluador con el código: " + evaluador.getCodigo());
                }
            }
        }
        
        // Validar email (si se proporciona)
        if (evaluador.getEmail() != null && !evaluador.getEmail().trim().isEmpty()) {
            if (evaluador.getEmail().length() > 150) {
                throw new IllegalArgumentException("El email no puede exceder 150 caracteres");
            }
            
            // Validación básica de formato email
            if (!evaluador.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
                throw new IllegalArgumentException("El formato del email no es válido");
            }
            
            // Verificar unicidad del email
            if (esActualizacion) {
                if (evaluadorRepository.existsByEmailAndIdEvaluadorNot(evaluador.getEmail(), evaluador.getIdEvaluador())) {
                    throw new IllegalArgumentException("Ya existe otro evaluador con el email: " + evaluador.getEmail());
                }
            } else {
                if (evaluadorRepository.findByEmail(evaluador.getEmail()).isPresent()) {
                    throw new IllegalArgumentException("Ya existe un evaluador con el email: " + evaluador.getEmail());
                }
            }
        }
        
        // Validar teléfono (si se proporciona)
        if (evaluador.getTelefono() != null && evaluador.getTelefono().length() > 20) {
            throw new IllegalArgumentException("El teléfono no puede exceder 20 caracteres");
        }
        
        // Validar especialidad (si se proporciona)
        if (evaluador.getEspecialidad() != null && evaluador.getEspecialidad().length() > 100) {
            throw new IllegalArgumentException("La especialidad no puede exceder 100 caracteres");
        }
    }
}
