package com.seguratuauto.dao;

import com.seguratuauto.model.Agente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repositorio JPA para la entidad Agente
 */
@Repository
public interface AgenteRepository extends JpaRepository<Agente, UUID> {
    
    /**
     * Busca un agente por código
     * @param codigo el código del agente
     * @return Optional con el agente si existe
     */
    Optional<Agente> findByCodigo(String codigo);
    
    /**
     * Busca agentes por nombre (búsqueda parcial)
     * @param nombre parte del nombre a buscar
     * @return lista de agentes que coinciden
     */
    List<Agente> findByNombreContainingIgnoreCase(String nombre);
    
    /**
     * Busca un agente por email
     * @param email el email del agente
     * @return Optional con el agente si existe
     */
    Optional<Agente> findByEmail(String email);
    
    /**
     * Verifica si existe un agente con el código dado
     * @param codigo código a verificar
     * @return true si existe
     */
    boolean existsByCodigo(String codigo);
    
    /**
     * Verifica si existe un agente con el email dado
     * @param email email a verificar
     * @return true si existe
     */
    boolean existsByEmail(String email);
}
