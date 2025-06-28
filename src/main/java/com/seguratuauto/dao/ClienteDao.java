package com.seguratuauto.dao;

import com.seguratuauto.model.Cliente;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Interface DAO para la entidad Cliente
 */
public interface ClienteDao {
    
    /**
     * Crea un nuevo cliente en la base de datos
     * @param cliente el cliente a crear
     * @return el cliente creado con su ID generado
     */
    Cliente create(Cliente cliente);
    
    /**
     * Busca un cliente por su ID
     * @param id el ID del cliente
     * @return Optional con el cliente si existe
     */
    Optional<Cliente> findById(UUID id);
    
    /**
     * Busca un cliente por su email
     * @param email el email del cliente
     * @return Optional con el cliente si existe
     */
    Optional<Cliente> findByEmail(String email);
    
    /**
     * Obtiene todos los clientes
     * @return lista de todos los clientes
     */
    List<Cliente> findAll();
    
    /**
     * Actualiza un cliente existente
     * @param cliente el cliente con los datos actualizados
     * @return el cliente actualizado
     */
    Cliente update(Cliente cliente);
    
    /**
     * Elimina un cliente por su ID
     * @param id el ID del cliente a eliminar
     * @return true si se eliminó correctamente
     */
    boolean deleteById(UUID id);
    
    /**
     * Verifica si existe un cliente con el ID dado
     * @param id el ID del cliente
     * @return true si existe
     */
    boolean existsById(UUID id);
}
