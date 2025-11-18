package com.seguratuauto.service;

public interface EmailVerificationService {
    boolean verificarEmail(String token);
    boolean reenviarEmail(String email);
    boolean estaVerificado(String email);
    String generarTokenVerificacion(String email);
}
