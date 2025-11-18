package com.seguratuauto.service;

public interface PasswordResetService {
    boolean solicitarRecuperacion(String email);
    boolean restablecerContraseña(String token, String nuevaContraseña);
    boolean validarToken(String token);
    String generarTokenRecuperacion(String email);
}
