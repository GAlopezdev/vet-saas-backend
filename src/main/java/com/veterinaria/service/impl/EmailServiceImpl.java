package com.veterinaria.service.impl;

import com.veterinaria.model.entity.Usuario;
import com.veterinaria.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final Logger LOGGER = LoggerFactory.getLogger(EmailServiceImpl.class);

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    @Value("${app.frontend-url}")
    private String frontendUrl;

    @Async
    @Override
    public void sendRegistrationEmail(Usuario usuario, String token, String subject, String templateName) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            Context context = new Context();

            context.setVariable("nombre", usuario.getCorreo().split("@")[0]);
            context.setVariable("link", frontendUrl + "/verify?token=" + token);

            String htmlContent = templateEngine.process("email/" + templateName, context);

            helper.setTo(usuario.getCorreo());
            helper.setSubject(subject);
            helper.setText(htmlContent, true);

            mailSender.send(message);
        } catch (MessagingException e) {
            LOGGER.error("Error al enviar email a {}: {}", usuario.getCorreo(), e.getMessage());
        }
    }
}
