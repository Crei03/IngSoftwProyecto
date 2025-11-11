package com.seguratuauto.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

/**
 * Configuración para el almacenamiento de archivos
 */
@Configuration
public class FileStorageConfig implements WebMvcConfigurer {

    @Value("${app.file.upload-dir:uploads}")
    private String uploadDir;

    @Value("${app.file.max-file-size:10485760}") // 10MB en bytes
    private long maxFileSize;

    @Value("${app.file.max-files-per-reclamacion:5}")
    private int maxFilesPerReclamacion;

    // Tipos de archivo permitidos
    private final List<String> allowedMimeTypes = Arrays.asList(
        "application/pdf",
        "application/msword",
        "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
        "image/jpeg",
        "image/png"
    );

    // Extensiones permitidas
    private final List<String> allowedExtensions = Arrays.asList(
        ".pdf", ".doc", ".docx", ".jpg", ".jpeg", ".png"
    );

    @PostConstruct
    public void init() {
        try {
            Path uploadPath = Paths.get(uploadDir).toAbsolutePath().normalize();
            
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
                System.out.println("Directorio de archivos creado: " + uploadPath);
            }
        } catch (IOException e) {
            throw new RuntimeException("No se pudo crear el directorio de archivos", e);
        }
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String uploadPath = Paths.get(uploadDir).toAbsolutePath().normalize().toString();
        
        registry.addResourceHandler("/files/**")
                .addResourceLocations("file:" + uploadPath + "/");
    }

    // Getters
    public String getUploadDir() {
        return uploadDir;
    }

    public long getMaxFileSize() {
        return maxFileSize;
    }

    public int getMaxFilesPerReclamacion() {
        return maxFilesPerReclamacion;
    }

    public List<String> getAllowedMimeTypes() {
        return allowedMimeTypes;
    }

    public List<String> getAllowedExtensions() {
        return allowedExtensions;
    }

    /**
     * Verifica si un tipo MIME está permitido
     */
    public boolean isMimeTypeAllowed(String mimeType) {
        return allowedMimeTypes.contains(mimeType);
    }

    /**
     * Verifica si una extensión está permitida
     */
    public boolean isExtensionAllowed(String extension) {
        return allowedExtensions.contains(extension.toLowerCase());
    }

    /**
     * Obtiene la ruta completa para almacenar archivos
     */
    public Path getUploadPath() {
        return Paths.get(uploadDir).toAbsolutePath().normalize();
    }

    /**
     * Formatea el tamaño máximo en formato legible
     */
    public String getMaxFileSizeFormatted() {
        double mb = maxFileSize / (1024.0 * 1024.0);
        return String.format("%.1f MB", mb);
    }
}