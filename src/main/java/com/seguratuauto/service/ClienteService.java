package com.seguratuauto.service;

import com.seguratuauto.model.Cliente;

import java.util.List;
import java.util.UUID;

/**
 * Servicio de negocio para el manejo de clientes
 */
public interface ClienteService {
    
    /**
     * Crea un nuevo cliente en el sistema
     * @param cliente los datos del cliente a crear
     * @return el cliente creado
     */
    Cliente crearCliente(Cliente cliente);
    
    /**
     * Busca un cliente por su ID
     * @param id el ID del cliente
     * @return el cliente encontrado o null si no existe
     */
    Cliente buscarClientePorId(UUID id);
    
    /**
     * Busca un cliente por su email
     * @param email el email del cliente
     * @return el cliente encontrado o null si no existe
     */
    Cliente buscarClientePorEmail(String email);
    
    /**
     * Busca un cliente por su teléfono
     * @param telefono el teléfono del cliente
     * @return el cliente encontrado o null si no existe
     */
    Cliente buscarClientePorTelefono(String telefono);
    
    /**
     * Busca clientes por nombre (búsqueda parcial)
     * @param nombre parte del nombre a buscar
     * @return lista de clientes que coinciden
     */
    List<Cliente> buscarClientesPorNombre(String nombre);
    
    /**
     * Obtiene todos los clientes del sistema
     * @return lista de todos los clientes
     */
    List<Cliente> obtenerTodosLosClientes();
    
    /**
     * Actualiza un cliente existente
     * @param cliente el cliente con los datos actualizados
     * @return el cliente actualizado
     */
    Cliente actualizarCliente(Cliente cliente);
    
    /**
     * Elimina un cliente por su ID
     * @param id el ID del cliente a eliminar
     * @return true si se eliminó correctamente
     */
    boolean eliminarCliente(UUID id);
    
    /**
     * Verifica si existe un cliente con el ID dado
     * @param id el ID del cliente
     * @return true si existe
     */
    boolean existeCliente(UUID id);
    
    /**
     * Verifica si existe un cliente con el email dado
     * @param email el email a verificar
     * @return true si existe
     */
    boolean existeClientePorEmail(String email);
    
    /**
     * Verifica si existe un cliente con el teléfono dado
     * @param telefono el teléfono a verificar
     * @return true si existe
     */
    boolean existeClientePorTelefono(String telefono);
    
    /**
     * Busca clientes por múltiples criterios
     * @param nombre nombre del cliente (puede ser parcial)
     * @param email email del cliente
     * @return lista de clientes que coinciden
     */
    List<Cliente> buscarClientesPorCriterios(String nombre, String email);
    
    /**
     * Obtiene el número total de clientes registrados
     * @return número de clientes
     */
    long contarClientes();
    
    /**
     * Valida los datos de un cliente antes de crearlo o actualizarlo
     * @param cliente el cliente a validar
     * @return true si es válido, false en caso contrario
     */
    boolean validarCliente(Cliente cliente);
}
