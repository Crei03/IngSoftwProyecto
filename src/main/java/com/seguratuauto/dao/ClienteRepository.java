package com.seguratuauto.dao;

import com.seguratuauto.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repositorio JPA para la entidad Cliente
 */
@Repository
public interface ClienteRepository extends JpaRepository<Cliente, UUID> {
    
    /**
     * Busca un cliente por email
     * @param email el email del cliente
     * @return Optional con el cliente si existe
     */
    Optional<Cliente> findByEmail(String email);
    
    /**
     * Busca clientes por nombre (búsqueda parcial)
     * @param nombre parte del nombre a buscar
     * @return lista de clientes que coinciden
     */
    List<Cliente> findByNombreContainingIgnoreCase(String nombre);
    
    /**
     * Busca un cliente por teléfono
     * @param telefono el teléfono del cliente
     * @return Optional con el cliente si existe
     */
    Optional<Cliente> findByTelefono(String telefono);
    
    /**
     * Verifica si existe un cliente con el email dado
     * @param email email a verificar
     * @return true si existe
     */
    boolean existsByEmail(String email);
    
    /**
     * Verifica si existe un cliente con el teléfono dado
     * @param telefono teléfono a verificar
     * @return true si existe
     */
    boolean existsByTelefono(String telefono);
    
    /**
     * Busca clientes por email o teléfono
     * @param email email del cliente
     * @param telefono teléfono del cliente
     * @return lista de clientes que coinciden
     */
    List<Cliente> findByEmailOrTelefono(String email, String telefono);
    
    /**
     * Cuenta el número total de clientes
     * @return número de clientes registrados
     */
    @Query("SELECT COUNT(c) FROM Cliente c")
    long countTotalClientes();
    
    /**
     * Busca clientes por múltiples criterios
     * @param nombre nombre del cliente (puede ser parcial)
     * @param email email del cliente
     * @return lista de clientes que coinciden
     */
    @Query("SELECT c FROM Cliente c WHERE " +
           "(:nombre IS NULL OR LOWER(c.nombre) LIKE LOWER(CONCAT('%', :nombre, '%'))) AND " +
           "(:email IS NULL OR LOWER(c.email) = LOWER(:email))")
    List<Cliente> findByMultipleCriteria(@Param("nombre") String nombre, @Param("email") String email);
}
