package com.seguratuauto.api.dto;

import com.seguratuauto.model.EstadoDocumento;
import java.time.LocalDateTime;

/**
 * DTO para las respuestas que contienen información de documentos adjuntos
 */
public class DocumentoResponse {
    
    private Long idDocumento;
    private Long reclamacionId;
    private String nombreOriginal;
    private String nombreArchivo;
    private String tipoContenido;
    private Long tamaño;
    private String tamañoFormatted;
    private String rutaDescarga;
    private LocalDateTime fechaSubida;
    private String descripcion;
    private EstadoDocumento estado;
    private String extension;
    private boolean isPDF;
    private boolean isImage;
    private boolean isDocument;
    
    // Constructor por defecto
    public DocumentoResponse() {}
    
    // Constructor básico
    public DocumentoResponse(Long idDocumento, String nombreOriginal, String tipoContenido, 
                            Long tamaño, LocalDateTime fechaSubida) {
        this.idDocumento = idDocumento;
        this.nombreOriginal = nombreOriginal;
        this.tipoContenido = tipoContenido;
        this.tamaño = tamaño;
        this.fechaSubida = fechaSubida;
        this.tamañoFormatted = formatearTamaño(tamaño);
        this.extension = obtenerExtension(nombreOriginal);
        this.isPDF = "application/pdf".equals(tipoContenido);
        this.isImage = tipoContenido != null && tipoContenido.startsWith("image/");
        this.isDocument = tipoContenido != null && 
                        (tipoContenido.equals("application/msword") || 
                        tipoContenido.equals("application/vnd.openxmlformats-officedocument.wordprocessingml.document"));
    }
    
    // Constructor completo
    public DocumentoResponse(Long idDocumento, Long reclamacionId, String nombreOriginal, 
                        String nombreArchivo, String tipoContenido, Long tamaño, 
                        String rutaDescarga, LocalDateTime fechaSubida, String descripcion, 
                        EstadoDocumento estado) {
        this.idDocumento = idDocumento;
        this.reclamacionId = reclamacionId;
        this.nombreOriginal = nombreOriginal;
        this.nombreArchivo = nombreArchivo;
        this.tipoContenido = tipoContenido;
        this.tamaño = tamaño;
        this.tamañoFormatted = formatearTamaño(tamaño);
        this.rutaDescarga = rutaDescarga;
        this.fechaSubida = fechaSubida;
        this.descripcion = descripcion;
        this.estado = estado;
        this.extension = obtenerExtension(nombreOriginal);
        this.isPDF = "application/pdf".equals(tipoContenido);
        this.isImage = tipoContenido != null && tipoContenido.startsWith("image/");
        this.isDocument = tipoContenido != null && 
                        (tipoContenido.equals("application/msword") || 
                        tipoContenido.equals("application/vnd.openxmlformats-officedocument.wordprocessingml.document"));
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
        this.extension = obtenerExtension(nombreOriginal);
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
        this.isPDF = "application/pdf".equals(tipoContenido);
        this.isImage = tipoContenido != null && tipoContenido.startsWith("image/");
        this.isDocument = tipoContenido != null && 
                        (tipoContenido.equals("application/msword") || 
                        tipoContenido.equals("application/vnd.openxmlformats-officedocument.wordprocessingml.document"));
    }
    
    public Long getTamaño() {
        return tamaño;
    }
    
    public void setTamaño(Long tamaño) {
        this.tamaño = tamaño;
        this.tamañoFormatted = formatearTamaño(tamaño);
    }
    
    public String getTamañoFormatted() {
        return tamañoFormatted;
    }
    
    public void setTamañoFormatted(String tamañoFormatted) {
        this.tamañoFormatted = tamañoFormatted;
    }
    
    public String getRutaDescarga() {
        return rutaDescarga;
    }
    
    public void setRutaDescarga(String rutaDescarga) {
        this.rutaDescarga = rutaDescarga;
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
    
    public String getExtension() {
        return extension;
    }
    
    public void setExtension(String extension) {
        this.extension = extension;
    }
    
    public boolean isPDF() {
        return isPDF;
    }
    
    public void setPDF(boolean PDF) {
        isPDF = PDF;
    }
    
    public boolean isImage() {
        return isImage;
    }
    
    public void setImage(boolean image) {
        isImage = image;
    }
    
    public boolean isDocument() {
        return isDocument;
    }
    
    public void setDocument(boolean document) {
        isDocument = document;
    }
    
    // Métodos de utilidad privados
    private String formatearTamaño(Long bytes) {
        if (bytes == null || bytes == 0) return "0 B";
        
        String[] unidades = {"B", "KB", "MB", "GB"};
        int unidadIndex = 0;
        double tamaño = bytes.doubleValue();
        
        while (tamaño >= 1024 && unidadIndex < unidades.length - 1) {
            tamaño /= 1024;
            unidadIndex++;
        }
        
        return String.format("%.2f %s", tamaño, unidades[unidadIndex]);
    }
    
    private String obtenerExtension(String nombreArchivo) {
        if (nombreArchivo == null) return "";
        int lastDot = nombreArchivo.lastIndexOf('.');
        return lastDot > 0 ? nombreArchivo.substring(lastDot) : "";
    }
    
    @Override
    public String toString() {
        return "DocumentoResponse{" +
                "idDocumento=" + idDocumento +
                ", reclamacionId=" + reclamacionId +
                ", nombreOriginal='" + nombreOriginal + '\'' +
                ", tipoContenido='" + tipoContenido + '\'' +
                ", tamaño=" + tamaño +
                ", fechaSubida=" + fechaSubida +
                ", estado=" + estado +
                '}';
    }
}