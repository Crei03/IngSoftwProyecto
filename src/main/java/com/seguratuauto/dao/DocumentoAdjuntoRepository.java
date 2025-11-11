package com.seguratuauto.dao;

import com.seguratuauto.model.DocumentoAdjunto;
import com.seguratuauto.model.EstadoDocumento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repositorio para operaciones de base de datos de DocumentoAdjunto
 */
@Repository
public interface DocumentoAdjuntoRepository extends JpaRepository<DocumentoAdjunto, Long> {
    
    /**
     * Buscar documentos por reclamación
     */
    List<DocumentoAdjunto> findByReclamacionId(Long reclamacionId);
    
    /**
     * Buscar documentos activos por reclamación
     */
    List<DocumentoAdjunto> findByReclamacionIdAndEstado(Long reclamacionId, EstadoDocumento estado);
    
    /**
     * Buscar documento por nombre de archivo
     */
    Optional<DocumentoAdjunto> findByNombreArchivo(String nombreArchivo);
    
    /**
     * Buscar documentos por tipo de contenido
     */
    List<DocumentoAdjunto> findByTipoContenido(String tipoContenido);
    
    /**
     * Contar documentos por reclamación
     */
    @Query("SELECT COUNT(d) FROM DocumentoAdjunto d WHERE d.reclamacionId = :reclamacionId AND d.estado = :estado")
    int countByReclamacionIdAndEstado(@Param("reclamacionId") Long reclamacionId, @Param("estado") EstadoDocumento estado);
    
    /**
     * Buscar documentos subidos en un rango de fechas
     */
    List<DocumentoAdjunto> findByFechaSubidaBetween(LocalDateTime fechaInicio, LocalDateTime fechaFin);
    
    /**
     * Buscar documentos por tamaño mayor a un valor
     */
    List<DocumentoAdjunto> findByTamañoGreaterThan(Long tamaño);
    
    /**
     * Obtener tamaño total de archivos por reclamación
     */
    @Query("SELECT COALESCE(SUM(d.tamaño), 0) FROM DocumentoAdjunto d WHERE d.reclamacionId = :reclamacionId AND d.estado = :estado")
    Long getTamañoTotalByReclamacionIdAndEstado(@Param("reclamacionId") Long reclamacionId, @Param("estado") EstadoDocumento estado);
    
    /**
     * Buscar documentos por descripción (busqueda parcial)
     */
    List<DocumentoAdjunto> findByDescripcionContainingIgnoreCase(String descripcion);
    
    /**
     * Buscar documentos por nombre original (busqueda parcial)
     */
    List<DocumentoAdjunto> findByNombreOriginalContainingIgnoreCase(String nombreOriginal);
    
    /**
     * Eliminar documentos por reclamación (cambiar estado a eliminado)
     */
    @Query("UPDATE DocumentoAdjunto d SET d.estado = :nuevoEstado WHERE d.reclamacionId = :reclamacionId")
    int updateEstadoByReclamacionId(@Param("reclamacionId") Long reclamacionId, @Param("nuevoEstado") EstadoDocumento nuevoEstado);
    
    /**
     * Obtener estadísticas de documentos
     */
    @Query("SELECT " +
        "COUNT(d) as total, " +
        "COUNT(CASE WHEN d.estado = 'ACTIVO' THEN 1 END) as activos, " +
        "COUNT(CASE WHEN d.estado = 'ELIMINADO' THEN 1 END) as eliminados, " +
        "COALESCE(SUM(d.tamaño), 0) as tamañoTotal " +
        "FROM DocumentoAdjunto d")
    Object[] getEstadisticasDocumentos();
}