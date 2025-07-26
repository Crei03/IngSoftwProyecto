package com.seguratuauto.service;

import com.seguratuauto.model.Agente;

import java.util.List;
import java.util.UUID;

/**
 * Servicio de negocio para el manejo de agentes
 */
public interface AgenteService {
    
    /**
     * Crea un nuevo agente en el sistema
     * @param agente los datos del agente a crear
     * @return el agente creado
     */
    Agente crearAgente(Agente agente);
    
    /**
     * Busca un agente por su ID
     * @param id el ID del agente
     * @return el agente encontrado o null si no existe
     */
    Agente buscarAgentePorId(UUID id);
    
    /**
     * Busca un agente por su código
     * @param codigo el código del agente
     * @return el agente encontrado o null si no existe
     */
    Agente buscarAgentePorCodigo(String codigo);
    
    /**
     * Busca un agente por su email
     * @param email el email del agente
     * @return el agente encontrado o null si no existe
     */
    Agente buscarAgentePorEmail(String email);
    
    /**
     * Busca un agente por su teléfono
     * @param telefono el teléfono del agente
     * @return el agente encontrado o null si no existe
     */
    Agente buscarAgentePorTelefono(String telefono);
    
    /**
     * Busca agentes por nombre (búsqueda parcial)
     * @param nombre parte del nombre a buscar
     * @return lista de agentes que coinciden
     */
    List<Agente> buscarAgentesPorNombre(String nombre);
    
    /**
     * Obtiene todos los agentes del sistema
     * @return lista de todos los agentes
     */
    List<Agente> obtenerTodosLosAgentes();
    
    /**
     * Obtiene agentes activos (que tienen pólizas asignadas)
     * @return lista de agentes con pólizas
     */
    List<Agente> obtenerAgentesActivos();
    
    /**
     * Actualiza un agente existente
     * @param agente el agente con los datos actualizados
     * @return el agente actualizado
     */
    Agente actualizarAgente(Agente agente);
    
    /**
     * Elimina un agente por su ID
     * @param id el ID del agente a eliminar
     * @return true si se eliminó correctamente
     */
    boolean eliminarAgente(UUID id);
    
    /**
     * Verifica si existe un agente con el ID dado
     * @param id el ID del agente
     * @return true si existe
     */
    boolean existeAgente(UUID id);
    
    /**
     * Verifica si existe un agente con el código dado
     * @param codigo el código a verificar
     * @return true si existe
     */
    boolean existeAgentePorCodigo(String codigo);
    
    /**
     * Verifica si existe un agente con el email dado
     * @param email el email a verificar
     * @return true si existe
     */
    boolean existeAgentePorEmail(String email);
    
    /**
     * Verifica si existe un agente con el teléfono dado
     * @param telefono el teléfono a verificar
     * @return true si existe
     */
    boolean existeAgentePorTelefono(String telefono);
    
    /**
     * Busca agentes por múltiples criterios
     * @param nombre nombre del agente (puede ser parcial)
     * @param codigo código del agente
     * @param email email del agente
     * @return lista de agentes que coinciden
     */
    List<Agente> buscarAgentesPorCriterios(String nombre, String codigo, String email);
    
    /**
     * Obtiene el número total de agentes registrados
     * @return número de agentes
     */
    long contarAgentes();
    
    /**
     * Obtiene el número de pólizas asignadas a un agente
     * @param agenteId ID del agente
     * @return número de pólizas del agente
     */
    long contarPolizasPorAgente(UUID agenteId);
    
    /**
     * Genera el siguiente código de agente disponible
     * @return el siguiente código de agente
     */
    String generarSiguienteCodigoAgente();
    
    /**
     * Valida los datos de un agente antes de crearlo o actualizarlo
     * @param agente el agente a validar
     * @return true si es válido, false en caso contrario
     */
    boolean validarAgente(Agente agente);
}
