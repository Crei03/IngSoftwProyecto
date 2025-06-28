package com.seguratuauto.dao;

import com.seguratuauto.model.EstadoPoliza;
import com.seguratuauto.model.Poliza;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Interface DAO para la entidad Poliza
 */
public interface PolizaDao {
    
    /**
     * Crea una nueva póliza en la base de datos
     * @param poliza la póliza a crear
     * @return la póliza creada con su ID generado
     */
    Poliza create(Poliza poliza);
    
    /**
     * Busca una póliza por su ID
     * @param id el ID de la póliza
     * @return Optional con la póliza si existe
     */
    Optional<Poliza> findById(UUID id);
    
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
     * Obtiene todas las pólizas
     * @return lista de todas las pólizas
     */
    List<Poliza> findAll();
    
    /**
     * Actualiza una póliza existente
     * @param poliza la póliza con los datos actualizados
     * @return la póliza actualizada
     */
    Poliza update(Poliza poliza);
    
    /**
     * Elimina una póliza por su ID
     * @param id el ID de la póliza a eliminar
     * @return true si se eliminó correctamente
     */
    boolean deleteById(UUID id);
    
    /**
     * Verifica si existe una póliza con el ID dado
     * @param id el ID de la póliza
     * @return true si existe
     */
    boolean existsById(UUID id);
    
    /**
     * Genera el siguiente número de póliza disponible
     * @return el siguiente número de póliza
     */
    String generateNextNumeroPoliza();
}
