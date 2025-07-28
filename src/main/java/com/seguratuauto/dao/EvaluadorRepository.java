package com.seguratuauto.dao;

import com.seguratuauto.model.Evaluador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio JPA para la entidad Evaluador
 */
@Repository
public interface EvaluadorRepository extends JpaRepository<Evaluador, Long> {
    
    /**
     * Busca un evaluador por código
     * @param codigo el código del evaluador
     * @return Optional con el evaluador si existe
     */
    Optional<Evaluador> findByCodigo(String codigo);
    
    /**
     * Busca evaluadores por nombre (búsqueda parcial)
     * @param nombre parte del nombre a buscar
     * @return lista de evaluadores que coinciden
     */
    List<Evaluador> findByNombreContainingIgnoreCase(String nombre);
    
    /**
     * Busca un evaluador por email
     * @param email el email del evaluador
     * @return Optional con el evaluador si existe
     */
    Optional<Evaluador> findByEmail(String email);
    
    /**
     * Busca evaluadores por especialidad
     * @param especialidad la especialidad a buscar
     * @return lista de evaluadores con esa especialidad
     */
    List<Evaluador> findByEspecialidadContainingIgnoreCase(String especialidad);
    
    /**
     * Busca evaluadores activos
     * @param activo estado activo
     * @return lista de evaluadores activos
     */
    List<Evaluador> findByActivo(Boolean activo);
    
    /**
     * Busca evaluadores por nombre y email
     * @param nombre parte del nombre a buscar
     * @param email email a buscar
     * @return lista de evaluadores que coinciden
     */
    @Query("SELECT e FROM Evaluador e WHERE " +
           "(:nombre IS NULL OR LOWER(e.nombre) LIKE LOWER(CONCAT('%', :nombre, '%'))) AND " +
           "(:email IS NULL OR LOWER(e.email) = LOWER(:email))")
    List<Evaluador> findByNombreContainingIgnoreCaseAndEmail(@Param("nombre") String nombre, @Param("email") String email);
    
    /**
     * Cuenta evaluadores activos
     * @return número de evaluadores activos
     */
    long countByActivo(Boolean activo);
    
    /**
     * Verifica si existe un evaluador con el código dado (excluyendo un ID específico)
     * @param codigo el código a verificar
     * @param idEvaluador ID del evaluador a excluir de la búsqueda
     * @return true si existe otro evaluador con ese código
     */
    @Query("SELECT COUNT(e) > 0 FROM Evaluador e WHERE e.codigo = :codigo AND e.idEvaluador != :idEvaluador")
    boolean existsByCodigoAndIdEvaluadorNot(@Param("codigo") String codigo, @Param("idEvaluador") Long idEvaluador);
    
    /**
     * Verifica si existe un evaluador con el email dado (excluyendo un ID específico)
     * @param email el email a verificar
     * @param idEvaluador ID del evaluador a excluir de la búsqueda
     * @return true si existe otro evaluador con ese email
     */
    @Query("SELECT COUNT(e) > 0 FROM Evaluador e WHERE e.email = :email AND e.idEvaluador != :idEvaluador")
    boolean existsByEmailAndIdEvaluadorNot(@Param("email") String email, @Param("idEvaluador") Long idEvaluador);
    
    /**
     * Obtiene el último código generado para generar el siguiente
     * @return el último código usado
     */
    @Query("SELECT e.codigo FROM Evaluador e WHERE e.codigo LIKE 'EV%' ORDER BY e.codigo DESC LIMIT 1")
    Optional<String> findLastGeneratedCodigo();
    
    /**
     * Busca evaluadores por múltiples criterios
     * @param nombre nombre del evaluador (opcional)
     * @param codigo código del evaluador (opcional)
     * @param email email del evaluador (opcional)
     * @param especialidad especialidad del evaluador (opcional)
     * @return lista de evaluadores que coinciden
     */
    @Query("SELECT e FROM Evaluador e WHERE " +
           "(:nombre IS NULL OR LOWER(e.nombre) LIKE LOWER(CONCAT('%', :nombre, '%'))) AND " +
           "(:codigo IS NULL OR LOWER(e.codigo) = LOWER(:codigo)) AND " +
           "(:email IS NULL OR LOWER(e.email) = LOWER(:email)) AND " +
           "(:especialidad IS NULL OR LOWER(e.especialidad) LIKE LOWER(CONCAT('%', :especialidad, '%')))")
    List<Evaluador> findByMultipleCriteria(@Param("nombre") String nombre, 
                                          @Param("codigo") String codigo, 
                                          @Param("email") String email,
                                          @Param("especialidad") String especialidad);
}
