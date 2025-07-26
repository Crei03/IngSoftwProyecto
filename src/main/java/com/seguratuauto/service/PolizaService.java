package com.seguratuauto.service;

import com.seguratuauto.model.Cliente;
import com.seguratuauto.model.EstadoPoliza;
import com.seguratuauto.model.Poliza;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Servicio de negocio para el manejo de pólizas
 */
public interface PolizaService {
    
    /**
     * Crea una nueva póliza en el sistema
     * @param cliente el cliente asociado a la póliza
     * @param polizaDatos los datos de la póliza a crear
     * @return la póliza creada
     */
    Poliza crearPoliza(Cliente cliente, Poliza polizaDatos);
    
    /**
     * Busca una póliza por su ID
     * @param id el ID de la póliza
     * @return la póliza encontrada o null si no existe
     */
    Poliza buscarPolizaPorId(UUID id);
    
    /**
     * Busca una póliza por su número
     * @param numeroPoliza el número de la póliza
     * @return la póliza encontrada o null si no existe
     */
    Poliza buscarPolizaPorNumero(String numeroPoliza);
    
    /**
     * Obtiene todas las pólizas de un cliente
     * @param clienteId el ID del cliente
     * @return lista de pólizas del cliente
     */
    List<Poliza> obtenerPolizasPorCliente(UUID clienteId);
    
    /**
     * Obtiene todas las pólizas de un agente
     * @param agenteId el ID del agente
     * @return lista de pólizas del agente
     */
    List<Poliza> obtenerPolizasPorAgente(UUID agenteId);
    
    /**
     * Obtiene pólizas por estado
     * @param estado el estado de las pólizas
     * @return lista de pólizas con el estado especificado
     */
    List<Poliza> obtenerPolizasPorEstado(EstadoPoliza estado);
    
    /**
     * Aprueba una póliza cambiando su estado
     * @param polizaId el ID de la póliza a aprobar
     * @return la póliza aprobada
     */
    Poliza aprobarPoliza(UUID polizaId);
    
    /**
     * Rechaza una póliza cambiando su estado
     * @param polizaId el ID de la póliza a rechazar
     * @param motivo el motivo del rechazo
     * @return la póliza rechazada
     */
    Poliza rechazarPoliza(UUID polizaId, String motivo);
    
    /**
     * Cancela una póliza
     * @param polizaId el ID de la póliza a cancelar
     * @param motivo el motivo de la cancelación
     * @return la póliza cancelada
     */
    Poliza cancelarPoliza(UUID polizaId, String motivo);
    
    /**
     * Actualiza una póliza existente
     * @param poliza la póliza con los datos actualizados
     * @return la póliza actualizada
     */
    Poliza actualizarPoliza(Poliza poliza);
    
    /**
     * Obtiene pólizas emitidas en un rango de fechas
     * @param fechaInicio fecha de inicio del rango
     * @param fechaFin fecha de fin del rango
     * @return lista de pólizas en el rango especificado
     */
    List<Poliza> obtenerPolizasPorRangoFechas(LocalDateTime fechaInicio, LocalDateTime fechaFin);
    
    /**
     * Obtiene todas las pólizas del sistema
     * @return lista de todas las pólizas
     */
    List<Poliza> obtenerTodasLasPolizas();
    
    /**
     * Valida los datos de una póliza antes de crearla
     * @param poliza la póliza a validar
     * @return true si es válida, false en caso contrario
     */
    boolean validarPoliza(Poliza poliza);
    
    /**
     * Genera el siguiente número de póliza disponible
     * @return el siguiente número de póliza
     */
    String generarSiguienteNumeroPoliza();
}
