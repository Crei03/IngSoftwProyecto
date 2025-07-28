package com.seguratuauto.dao;

import com.seguratuauto.model.Agente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio JPA para la entidad Agente
 */
@Repository
public interface AgenteRepository extends JpaRepository<Agente, Long> {
    
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
     * Busca un agente por teléfono
     * @param telefono el teléfono del agente
     * @return Optional con el agente si existe
     */
    Optional<Agente> findByTelefono(String telefono);
    
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
    
    /**
     * Verifica si existe un agente con el teléfono dado
     * @param telefono teléfono a verificar
     * @return true si existe
     */
    boolean existsByTelefono(String telefono);
    
    /**
     * Busca agentes por email o código
     * @param email email del agente
     * @param codigo código del agente
     * @return lista de agentes que coinciden
     */
    List<Agente> findByEmailOrCodigo(String email, String codigo);
    
    /**
     * Cuenta el número total de agentes
     * @return número de agentes registrados
     */
    @Query("SELECT COUNT(a) FROM Agente a")
    long countTotalAgentes();
    
    /**
     * Busca agentes activos (que tienen pólizas asignadas)
     * @return lista de agentes con pólizas
     */
    @Query("SELECT DISTINCT a FROM Agente a WHERE EXISTS " +
           "(SELECT p FROM Poliza p WHERE p.agenteId = a.idAgente)")
    List<Agente> findAgentesActivos();
    
    /**
     * Busca agentes por múltiples criterios
     * @param nombre nombre del agente (puede ser parcial)
     * @param codigo código del agente
     * @param email email del agente
     * @return lista de agentes que coinciden
     */
    @Query("SELECT a FROM Agente a WHERE " +
           "(:nombre IS NULL OR LOWER(a.nombre) LIKE LOWER(CONCAT('%', :nombre, '%'))) AND " +
           "(:codigo IS NULL OR LOWER(a.codigo) = LOWER(:codigo)) AND " +
           "(:email IS NULL OR LOWER(a.email) = LOWER(:email))")
    List<Agente> findByMultipleCriteria(@Param("nombre") String nombre, 
                                       @Param("codigo") String codigo, 
                                       @Param("email") String email);
    
    /**
     * Obtiene el último código de agente para generar el siguiente
     * @return el código de agente más alto actualmente
     */
    @Query("SELECT a.codigo FROM Agente a WHERE a.codigo IS NOT NULL ORDER BY a.codigo DESC LIMIT 1")
    Optional<String> findLastCodigo();
    
    /**
     * Cuenta las pólizas asignadas a un agente
     * @param agenteId ID del agente
     * @return número de pólizas del agente
     */
    @Query("SELECT COUNT(p) FROM Poliza p WHERE p.agenteId = :agenteId")
    long countPolizasByAgente(@Param("agenteId") Long agenteId);
}
