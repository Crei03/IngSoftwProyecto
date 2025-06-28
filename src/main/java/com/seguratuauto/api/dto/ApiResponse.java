package com.seguratuauto.api.dto;

/**
 * DTO para respuestas estándar de la API
 */
public class ApiResponse<T> {
    
    private boolean success;
    private String message;
    private T data;
    private String timestamp;
    
    // Constructor por defecto
    public ApiResponse() {
        this.timestamp = java.time.LocalDateTime.now().toString();
    }
    
    // Constructor de éxito
    public ApiResponse(T data) {
        this();
        this.success = true;
        this.data = data;
        this.message = "Operación exitosa";
    }
    
    // Constructor de éxito con mensaje personalizado
    public ApiResponse(T data, String message) {
        this();
        this.success = true;
        this.data = data;
        this.message = message;
    }
    
    // Constructor de error
    public ApiResponse(String errorMessage) {
        this();
        this.success = false;
        this.message = errorMessage;
        this.data = null;
    }
    
    // Métodos estáticos para crear respuestas
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(data);
    }
    
    public static <T> ApiResponse<T> success(T data, String message) {
        return new ApiResponse<>(data, message);
    }
    
    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>(message);
    }
    
    // Getters y Setters
    public boolean isSuccess() {
        return success;
    }
    
    public void setSuccess(boolean success) {
        this.success = success;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public T getData() {
        return data;
    }
    
    public void setData(T data) {
        this.data = data;
    }
    
    public String getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
    
    @Override
    public String toString() {
        return "ApiResponse{" +
                "success=" + success +
                ", message='" + message + '\'' +
                ", data=" + data +
                ", timestamp='" + timestamp + '\'' +
                '}';
    }
}
