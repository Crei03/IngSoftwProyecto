package com.seguratuauto.dao;

import com.seguratuauto.model.Agente;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Interface DAO para la entidad Agente
 */
public interface AgenteDao {
    
    /**
     * Crea un nuevo agente en la base de datos
     * @param agente el agente a crear
     * @return el agente creado con su ID generado
     */
    Agente create(Agente agente);
    
    /**
     * Busca un agente por su ID
     * @param id el ID del agente
     * @return Optional con el agente si existe
     */
    Optional<Agente> findById(UUID id);
    
    /**
     * Busca un agente por su código
     * @param codigo el código del agente
     * @return Optional con el agente si existe
     */
    Optional<Agente> findByCodigo(String codigo);
    
    /**
     * Busca un agente por su email
     * @param email el email del agente
     * @return Optional con el agente si existe
     */
    Optional<Agente> findByEmail(String email);
    
    /**
     * Obtiene todos los agentes
     * @return lista de todos los agentes
     */
    List<Agente> findAll();
    
    /**
     * Actualiza un agente existente
     * @param agente el agente con los datos actualizados
     * @return el agente actualizado
     */
    Agente update(Agente agente);
    
    /**
     * Elimina un agente por su ID
     * @param id el ID del agente a eliminar
     * @return true si se eliminó correctamente
     */
    boolean deleteById(UUID id);
    
    /**
     * Verifica si existe un agente con el ID dado
     * @param id el ID del agente
     * @return true si existe
     */
    boolean existsById(UUID id);
}
