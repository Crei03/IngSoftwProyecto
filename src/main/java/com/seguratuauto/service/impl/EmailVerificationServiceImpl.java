package com.seguratuauto.service.impl;

import com.seguratuauto.model.Cliente;
import com.seguratuauto.service.EmailVerificationService;
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

/**
 * Implementación basada en MailDev para envío de correos de verificación
 */
@Service
public class EmailVerificationServiceImpl implements EmailVerificationService {
    
    private static final Logger log = LoggerFactory.getLogger(EmailVerificationServiceImpl.class);
    
    private final JavaMailSender mailSender;
    private final String verificationBaseUrl;
    private final String frontendUrl;
    
    public EmailVerificationServiceImpl(JavaMailSender mailSender,
                                        @Value("${app.verification.base-url:http://localhost:8080/api/verificacion/confirmar}") String verificationBaseUrl,
                                        @Value("${app.frontend-url:http://localhost:5173}") String frontendUrl) {
        this.mailSender = mailSender;
        this.verificationBaseUrl = verificationBaseUrl;
        this.frontendUrl = frontendUrl;
    }
    
    @Override
    public void enviarCorreoConfirmacion(Cliente cliente) {
        if (cliente == null || cliente.getEmail() == null || cliente.getTokenVerificacion() == null) {
            log.warn("No se pudo enviar correo de verificación: datos incompletos");
            return;
        }
        
        String confirmUrl = verificationBaseUrl + "?token=" + cliente.getTokenVerificacion();
        
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    StandardCharsets.UTF_8.name());
            
            helper.setTo(cliente.getEmail());
            helper.setFrom("no-reply@seguratuauto.com");
            helper.setSubject("Confirma tu cuenta en SeguraTuAuto");
            helper.setText(buildHtmlTemplate(cliente, confirmUrl), true);
            
            mailSender.send(message);
            log.info("Correo de verificación enviado a {}", cliente.getEmail());
            
        } catch (MailException | MessagingException e) {
            log.error("Error enviando correo de verificación a {}", cliente.getEmail(), e);
        }
    }
    
    private String buildHtmlTemplate(Cliente cliente, String confirmUrl) {
        String nombre = cliente.getNombre() != null ? cliente.getNombre() : "cliente";
        return """
                <div style="font-family: 'Segoe UI', Arial, sans-serif; background-color: #f4f6fb; padding: 40px;">
                  <div style="max-width: 560px; margin: auto; background: #ffffff; border-radius: 24px; overflow: hidden;
                              box-shadow: 0 20px 45px rgba(15,23,42,0.12);">
                    <div style="background: linear-gradient(135deg, #2563eb, #3b82f6); padding: 32px; color: #fff;">
                      <h1 style="margin: 0; font-size: 28px;">SeguraTuAuto</h1>
                      <p style="margin: 8px 0 0; opacity: 0.9;">Seguridad y confianza para tu vehículo</p>
                    </div>
                    <div style="padding: 32px;">
                      <p style="font-size: 18px; color: #0f172a; margin: 0 0 16px;">Hola %s,</p>
                      <p style="color: #475569; line-height: 1.6; margin: 0 0 24px;">
                        Hemos recibido tu registro en SeguraTuAuto. Para activar tu cuenta y comenzar a gestionar tus pólizas,
                        confirma tu correo electrónico.
                      </p>
                      <div style="text-align: center; margin: 32px 0;">
                        <a href="%s" style="display: inline-block; background: linear-gradient(135deg, #f97316, #fb923c);
                           color: #fff; text-decoration: none; padding: 16px 32px; border-radius: 999px; font-weight: 600;
                           letter-spacing: 0.3px;">
                          Confirmar mi cuenta
                        </a>
                      </div>
                      <p style="color: #475569; line-height: 1.6; margin: 0 0 8px;">
                        Si el botón no funciona, copia y pega este enlace en tu navegador:
                      </p>
                      <p style="font-size: 14px; color: #2563eb; word-break: break-all;">%s</p>
                      <p style="color: #94a3b8; font-size: 13px; margin-top: 32px;">
                        Si no solicitaste esta verificación, ignora este mensaje. Este enlace expirará en 24 horas para tu seguridad.
                      </p>
                    </div>
                    <div style="background: #0f172a; color: #ffffff; padding: 24px; text-align: center;">
                      <p style="margin: 0; font-size: 14px;">© %s SeguraTuAuto • Todos los derechos reservados</p>
                      <p style="margin: 8px 0 0; font-size: 12px; opacity: 0.7;">Serás redirigido a nuestra pantalla de inicio
                      de sesión tras confirmar tu correo: %s</p>
                    </div>
                  </div>
                </div>
                """.formatted(nombre, confirmUrl, confirmUrl, java.time.Year.now(), frontendUrl);
    }
}
