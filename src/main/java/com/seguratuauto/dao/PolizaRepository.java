package com.seguratuauto.dao;

import com.seguratuauto.model.EstadoPoliza;
import com.seguratuauto.model.Poliza;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repositorio JPA para la entidad Poliza
 * Reemplaza la funcionalidad de JdbcPolizaDao usando Spring Data JPA
 */
@Repository
public interface PolizaRepository extends JpaRepository<Poliza, UUID> {
    
    /**
     * Busca una póliza por su número
     * @param numeroPoliza el número de la póliza
     * @return Optional con la póliza si existe
     */
    Optional<Poliza> findByNumeroPoliza(String numeroPoliza);
    
    /**
     * Busca todas las pólizas de un cliente
     * @param clienteId el ID del cliente
     * @return lista de pólizas del cliente
     */
    List<Poliza> findByClienteId(UUID clienteId);
    
    /**
     * Busca todas las pólizas de un agente
     * @param agenteId el ID del agente
     * @return lista de pólizas del agente
     */
    List<Poliza> findByAgenteId(UUID agenteId);
    
    /**
     * Busca pólizas por estado
     * @param estado el estado de las pólizas
     * @return lista de pólizas con el estado dado
     */
    List<Poliza> findByEstado(EstadoPoliza estado);
    
    /**
     * Busca pólizas emitidas en un rango de fechas
     * @param fechaInicio fecha de inicio del rango
     * @param fechaFin fecha de fin del rango
     * @return lista de pólizas en el rango
     */
    List<Poliza> findByFechaEmisionBetween(LocalDateTime fechaInicio, LocalDateTime fechaFin);
    
    /**
     * Busca pólizas de un cliente por estado
     * @param clienteId ID del cliente
     * @param estado estado de la póliza
     * @return lista de pólizas del cliente con el estado dado
     */
    List<Poliza> findByClienteIdAndEstado(UUID clienteId, EstadoPoliza estado);
    
    /**
     * Busca pólizas de un agente por estado
     * @param agenteId ID del agente
     * @param estado estado de la póliza
     * @return lista de pólizas del agente con el estado dado
     */
    List<Poliza> findByAgenteIdAndEstado(UUID agenteId, EstadoPoliza estado);
    
    /**
     * Cuenta las pólizas por estado
     * @param estado el estado a contar
     * @return número de pólizas con ese estado
     */
    long countByEstado(EstadoPoliza estado);
    
    /**
     * Obtiene el último número de póliza para generar el siguiente
     * @return el número de póliza más alto actualmente
     */
    @Query("SELECT p.numeroPoliza FROM Poliza p WHERE p.numeroPoliza IS NOT NULL ORDER BY p.numeroPoliza DESC LIMIT 1")
    Optional<String> findLastNumeroPoliza();
    
    /**
     * Verifica si existe una póliza con el número dado
     * @param numeroPoliza número de póliza a verificar
     * @return true si existe
     */
    boolean existsByNumeroPoliza(String numeroPoliza);
    
    /**
     * Busca pólizas próximas a vencer
     * @param fechaLimite fecha límite para considerar próximo vencimiento
     * @return lista de pólizas próximas a vencer
     */
    @Query("SELECT p FROM Poliza p WHERE p.fechaVencimiento <= :fechaLimite AND p.estado = :estado")
    List<Poliza> findPolizasProximasAVencer(@Param("fechaLimite") LocalDateTime fechaLimite, 
                                           @Param("estado") EstadoPoliza estado);
}
