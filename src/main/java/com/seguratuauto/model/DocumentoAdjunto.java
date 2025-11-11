package com.seguratuauto.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Entidad DocumentoAdjunto para el manejo de archivos adjuntos en reclamaciones
 */
@Entity
@Table(name = "documentos_adjuntos")
public class DocumentoAdjunto {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_documento")
    private Long idDocumento;
    
    @Column(name = "reclamacion_id", nullable = false)
    private Long reclamacionId;
    
    @Column(name = "nombre_original", nullable = false, length = 255)
    private String nombreOriginal;
    
    @Column(name = "nombre_archivo", nullable = false, length = 255)
    private String nombreArchivo;
    
    @Column(name = "tipo_contenido", nullable = false, length = 100)
    private String tipoContenido;
    
    @Column(name = "tamaño", nullable = false)
    private Long tamaño;
    
    @Column(name = "ruta_archivo", nullable = false, length = 500)
    private String rutaArchivo;
    
    @Column(name = "fecha_subida", nullable = false)
    private LocalDateTime fechaSubida;
    
    @Column(name = "descripcion", length = 500)
    private String descripcion;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, length = 20)
    private EstadoDocumento estado;
    
    // Constructor por defecto
    public DocumentoAdjunto() {
        this.fechaSubida = LocalDateTime.now();
        this.estado = EstadoDocumento.ACTIVO;
    }
    
    // Constructor para nuevo documento
    public DocumentoAdjunto(Long reclamacionId, String nombreOriginal, String nombreArchivo, 
                           String tipoContenido, Long tamaño, String rutaArchivo) {
        this();
        this.reclamacionId = reclamacionId;
        this.nombreOriginal = nombreOriginal;
        this.nombreArchivo = nombreArchivo;
        this.tipoContenido = tipoContenido;
        this.tamaño = tamaño;
        this.rutaArchivo = rutaArchivo;
    }
    
    // Constructor completo
    public DocumentoAdjunto(Long idDocumento, Long reclamacionId, String nombreOriginal, 
                           String nombreArchivo, String tipoContenido, Long tamaño, 
                           String rutaArchivo, LocalDateTime fechaSubida, String descripcion, 
                           EstadoDocumento estado) {
        this.idDocumento = idDocumento;
        this.reclamacionId = reclamacionId;
        this.nombreOriginal = nombreOriginal;
        this.nombreArchivo = nombreArchivo;
        this.tipoContenido = tipoContenido;
        this.tamaño = tamaño;
        this.rutaArchivo = rutaArchivo;
        this.fechaSubida = fechaSubida;
        this.descripcion = descripcion;
        this.estado = estado;
    }
    
    // Getters y Setters
    public Long getIdDocumento() {
        return idDocumento;
    }
    
    public void setIdDocumento(Long idDocumento) {
        this.idDocumento = idDocumento;
    }
    
    public Long getReclamacionId() {
        return reclamacionId;
    }
    
    public void setReclamacionId(Long reclamacionId) {
        this.reclamacionId = reclamacionId;
    }
    
    public String getNombreOriginal() {
        return nombreOriginal;
    }
    
    public void setNombreOriginal(String nombreOriginal) {
        this.nombreOriginal = nombreOriginal;
    }
    
    public String getNombreArchivo() {
        return nombreArchivo;
    }
    
    public void setNombreArchivo(String nombreArchivo) {
        this.nombreArchivo = nombreArchivo;
    }
    
    public String getTipoContenido() {
        return tipoContenido;
    }
    
    public void setTipoContenido(String tipoContenido) {
        this.tipoContenido = tipoContenido;
    }
    
    public Long getTamaño() {
        return tamaño;
    }
    
    public void setTamaño(Long tamaño) {
        this.tamaño = tamaño;
    }
    
    public String getRutaArchivo() {
        return rutaArchivo;
    }
    
    public void setRutaArchivo(String rutaArchivo) {
        this.rutaArchivo = rutaArchivo;
    }
    
    public LocalDateTime getFechaSubida() {
        return fechaSubida;
    }
    
    public void setFechaSubida(LocalDateTime fechaSubida) {
        this.fechaSubida = fechaSubida;
    }
    
    public String getDescripcion() {
        return descripcion;
    }
    
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    
    public EstadoDocumento getEstado() {
        return estado;
    }
    
    public void setEstado(EstadoDocumento estado) {
        this.estado = estado;
    }
    
    // Métodos de utilidad
    public String getTamañoFormatted() {
        if (tamaño == null) return "N/A";
        
        double kb = tamaño / 1024.0;
        double mb = kb / 1024.0;
        
        if (mb >= 1) {
            return String.format("%.2f MB", mb);
        } else {
            return String.format("%.2f KB", kb);
        }
    }
    
    public String getExtension() {
        if (nombreOriginal == null) return "";
        int lastDot = nombreOriginal.lastIndexOf('.');
        return lastDot > 0 ? nombreOriginal.substring(lastDot) : "";
    }
    
    public boolean isPDF() {
        return "application/pdf".equals(tipoContenido);
    }
    
    public boolean isImage() {
        return tipoContenido != null && tipoContenido.startsWith("image/");
    }
    
    public boolean isDocument() {
        return tipoContenido != null && 
               (tipoContenido.equals("application/msword") || 
                tipoContenido.equals("application/vnd.openxmlformats-officedocument.wordprocessingml.document"));
    }
    
    // Métodos equals, hashCode y toString
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DocumentoAdjunto that = (DocumentoAdjunto) o;
        return Objects.equals(idDocumento, that.idDocumento);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(idDocumento);
    }
    
    @Override
    public String toString() {
        return "DocumentoAdjunto{" +
                "idDocumento=" + idDocumento +
                ", reclamacionId=" + reclamacionId +
                ", nombreOriginal='" + nombreOriginal + '\'' +
                ", nombreArchivo='" + nombreArchivo + '\'' +
                ", tipoContenido='" + tipoContenido + '\'' +
                ", tamaño=" + tamaño +
                ", fechaSubida=" + fechaSubida +
                ", estado=" + estado +
                '}';
    }
}