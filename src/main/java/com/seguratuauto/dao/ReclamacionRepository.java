package com.seguratuauto.dao;

import com.seguratuauto.model.Reclamacion;
import com.seguratuauto.model.EstadoReclamacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio JPA para la entidad Reclamacion
 */
@Repository
public interface ReclamacionRepository extends JpaRepository<Reclamacion, Long> {
    
    /**
     * Busca reclamaciones por estado
     * @param estado el estado de la reclamación
     * @return lista de reclamaciones con el estado especificado
     */
    List<Reclamacion> findByEstado(EstadoReclamacion estado);
    
    /**
     * Busca reclamaciones por ID de póliza
     * @param polizaId el ID de la póliza
     * @return lista de reclamaciones para la póliza especificada
     */
    List<Reclamacion> findByPolizaId(Long polizaId);
    
    /**
     * Busca una reclamación por número de reclamación
     * @param numeroReclamacion el número único de la reclamación
     * @return reclamación encontrada
     */
    Optional<Reclamacion> findByNumeroReclamacion(String numeroReclamacion);
    
    /**
     * Cuenta el total de reclamaciones
     * @return número total de reclamaciones
     */
    @Query("SELECT COUNT(r) FROM Reclamacion r")
    long contarReclamaciones();
    
    /**
     * Busca reclamaciones por ID de evaluador
     * @param evaluadorId el ID del evaluador
     * @return lista de reclamaciones asignadas al evaluador
     */
    List<Reclamacion> findByEvaluadorId(Long evaluadorId);
    
    /**
     * Verifica si existe una reclamación con el número especificado
     * @param numeroReclamacion el número de reclamación a verificar
     * @return true si existe, false en caso contrario
     */
    boolean existsByNumeroReclamacion(String numeroReclamacion);
}
