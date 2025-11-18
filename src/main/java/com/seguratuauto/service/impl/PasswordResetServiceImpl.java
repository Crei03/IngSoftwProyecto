package com.seguratuauto.service.impl;

import com.seguratuauto.model.Cliente;
import com.seguratuauto.service.PasswordResetService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.Year;

/**
 * Implementación para el envío de correos de restablecimiento de contraseña usando MailDev
 */
@Service
public class PasswordResetServiceImpl implements PasswordResetService {
    
    private static final Logger log = LoggerFactory.getLogger(PasswordResetServiceImpl.class);
    
    private final JavaMailSender mailSender;
    private final String frontendUrl;
    
    public PasswordResetServiceImpl(JavaMailSender mailSender,
                                    @Value("${app.frontend-url:http://localhost:5173}") String frontendUrl) {
        this.mailSender = mailSender;
        this.frontendUrl = frontendUrl;
    }
    
    @Override
    public void enviarCorreoRecuperacion(Cliente cliente, String token) {
        if (cliente == null || cliente.getEmail() == null) {
            log.warn("No se puede enviar correo de recuperación: cliente o email nulo");
            return;
        }
        
        String resetUrl = frontendUrl + "?resetToken=" + token + "&email=" + cliente.getEmail();
        
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    StandardCharsets.UTF_8.name());
            
            helper.setTo(cliente.getEmail());
            helper.setFrom("no-reply@seguratuauto.com");
            helper.setSubject("Restablece tu contraseña en SeguraTuAuto");
            helper.setText(buildHtmlTemplate(cliente.getNombre(), resetUrl, token), true);
            
            mailSender.send(message);
            log.info("Correo de recuperación enviado a {}", cliente.getEmail());
        } catch (MailException | MessagingException e) {
            log.error("Error enviando correo de recuperación a {}", cliente.getEmail(), e);
        }
    }
    
    private String buildHtmlTemplate(String nombre, String resetUrl, String token) {
        String nombreSeguro = (nombre == null || nombre.isBlank()) ? "cliente" : nombre;
        return """
                <div style="font-family: 'Segoe UI', Arial, sans-serif; background-color: #0f172a; padding: 40px;">
                  <div style="max-width: 560px; margin: auto; background: #0b1220; border-radius: 24px; overflow: hidden;
                              box-shadow: 0 25px 60px rgba(14, 116, 144, 0.35); border: 1px solid rgba(148,163,184,0.3);">
                    <div style="background: linear-gradient(135deg, #2563eb, #7c3aed); padding: 32px; color: #fff;">
                      <h1 style="margin: 0; font-size: 26px;">SeguraTuAuto</h1>
                      <p style="margin: 6px 0 0; opacity: 0.95;">Restablecimiento de contraseña</p>
                    </div>
                    <div style="padding: 32px; color: #e2e8f0;">
                      <p style="font-size: 18px; margin: 0 0 16px;">Hola %s,</p>
                      <p style="line-height: 1.6; margin: 0 0 20px;">
                        Recibimos una solicitud para restablecer tu contraseña. Haz clic en el botón para crear una nueva.
                        Este enlace es válido por 2 horas. Si no solicitaste el cambio, puedes ignorar este mensaje.
                      </p>
                      <div style="text-align: center; margin: 30px 0;">
                        <a href="%s" style="display: inline-block; background: linear-gradient(135deg, #f97316, #fb923c);
                           color: #fff; text-decoration: none; padding: 16px 32px; border-radius: 999px; font-weight: 600;">
                          Restablecer contraseña
                        </a>
                      </div>
                      <div style="margin: 24px 0; padding: 18px; border-radius: 16px; background: rgba(59,130,246,.12); border: 1px solid rgba(59,130,246,.35); text-align: center;">
                        <p style="margin: 0 0 6px; color: #94a3b8; font-size: 13px; letter-spacing: 0.05em;">
                          CÓDIGO DE VERIFICACIÓN
                        </p>
                        <p style="margin: 0; font-size: 22px; font-weight: 700; color: #f8fafc; letter-spacing: 0.3em;">
                          %s
                        </p>
                      </div>
                      <p style="color: #94a3b8; font-size: 14px;">
                        También puedes copiar y pegar este enlace en tu navegador:
                      </p>
                      <p style="font-size: 14px; color: #38bdf8; word-break: break-all;">%s</p>
                    </div>
                    <div style="background: #020617; color: #94a3b8; padding: 20px; text-align: center;">
                      <p style="margin: 4px 0; font-size: 13px;">© %s SeguraTuAuto • Todos los derechos reservados</p>
                    </div>
                  </div>
                </div>
                """.formatted(nombreSeguro, resetUrl, token.toUpperCase(), resetUrl, Year.now());
    }
}
