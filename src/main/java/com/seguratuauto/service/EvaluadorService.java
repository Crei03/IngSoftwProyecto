package com.seguratuauto.service;

import com.seguratuauto.model.Evaluador;

import java.util.List;

/**
 * Servicio de negocio para el manejo de evaluadores
 */
public interface EvaluadorService {
    
    /**
     * Crea un nuevo evaluador en el sistema
     * @param evaluador los datos del evaluador a crear
     * @return el evaluador creado
     */
    Evaluador crearEvaluador(Evaluador evaluador);
    
    /**
     * Busca un evaluador por su ID
     * @param id el ID del evaluador
     * @return el evaluador encontrado o null si no existe
     */
    Evaluador buscarEvaluadorPorId(Long id);
    
    /**
     * Busca un evaluador por su código
     * @param codigo el código del evaluador
     * @return el evaluador encontrado o null si no existe
     */
    Evaluador buscarEvaluadorPorCodigo(String codigo);
    
    /**
     * Busca un evaluador por su email
     * @param email el email del evaluador
     * @return el evaluador encontrado o null si no existe
     */
    Evaluador buscarEvaluadorPorEmail(String email);
    
    /**
     * Busca evaluadores por nombre (búsqueda parcial)
     * @param nombre parte del nombre a buscar
     * @return lista de evaluadores que coinciden
     */
    List<Evaluador> buscarEvaluadoresPorNombre(String nombre);
    
    /**
     * Busca evaluadores por especialidad
     * @param especialidad la especialidad a buscar
     * @return lista de evaluadores con esa especialidad
     */
    List<Evaluador> buscarEvaluadoresPorEspecialidad(String especialidad);
    
    /**
     * Obtiene todos los evaluadores del sistema
     * @return lista de todos los evaluadores
     */
    List<Evaluador> obtenerTodosLosEvaluadores();
    
    /**
     * Obtiene solo los evaluadores activos
     * @return lista de evaluadores activos
     */
    List<Evaluador> obtenerEvaluadoresActivos();
    
    /**
     * Actualiza los datos de un evaluador existente
     * @param id el ID del evaluador a actualizar
     * @param evaluadorActualizado los nuevos datos del evaluador
     * @return el evaluador actualizado
     */
    Evaluador actualizarEvaluador(Long id, Evaluador evaluadorActualizado);
    
    /**
     * Elimina un evaluador del sistema (eliminación lógica)
     * @param id el ID del evaluador a eliminar
     * @return el evaluador marcado como inactivo
     */
    Evaluador eliminarEvaluador(Long id);
    
    /**
     * Elimina un evaluador del sistema (eliminación física)
     * @param id el ID del evaluador a eliminar permanentemente
     */
    void eliminarEvaluadorPermanente(Long id);
    
    /**
     * Verifica si existe un evaluador
     * @param id el ID del evaluador
     * @return true si existe, false en caso contrario
     */
    boolean existeEvaluador(Long id);
    
    /**
     * Obtiene el total de evaluadores en el sistema
     * @return número total de evaluadores
     */
    long contarTotalEvaluadores();
    
    /**
     * Obtiene el total de evaluadores activos
     * @return número de evaluadores activos
     */
    long contarEvaluadoresActivos();
    
    /**
     * Genera un nuevo código único para un evaluador
     * @return el nuevo código generado (formato: EV####)
     */
    String generarNuevoCodigo();
    
    /**
     * Busca evaluadores por múltiples criterios
     * @param nombre nombre del evaluador (opcional)
     * @param codigo código del evaluador (opcional)
     * @param email email del evaluador (opcional)
     * @param especialidad especialidad del evaluador (opcional)
     * @return lista de evaluadores que coinciden con los criterios
     */
    List<Evaluador> buscarEvaluadoresConCriterios(String nombre, String codigo, String email, String especialidad);
    
    /**
     * Busca evaluadores por nombre y email
     * @param nombre nombre del evaluador (opcional)
     * @param email email del evaluador (opcional)
     * @return lista de evaluadores que coinciden
     */
    List<Evaluador> buscarEvaluadoresPorNombreYEmail(String nombre, String email);
    
    /**
     * Reactiva un evaluador marcado como inactivo
     * @param id el ID del evaluador a reactivar
     * @return el evaluador reactivado
     */
    Evaluador reactivarEvaluador(Long id);
    
    /**
     * Valida los datos de un evaluador antes de crear o actualizar
     * @param evaluador el evaluador a validar
     * @param esActualizacion true si es una actualización, false si es creación
     * @throws IllegalArgumentException si los datos no son válidos
     */
    void validarEvaluador(Evaluador evaluador, boolean esActualizacion);
}
