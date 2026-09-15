package com.example.LoginPUC.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * Serviço de envio de e-mails, baseado no projeto SendEmail da disciplina.
 *
 * Usa o JavaMailSender do Spring Boot, configurado com SMTP do Gmail
 * via variáveis de ambiente (ver application.properties).
 */
@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    /**
     * Envia um e-mail simples (texto puro) para o destino informado.
     *
     * Falhas NÃO propagam para o controller: se o SMTP não estiver
     * configurado, a aplicação continua funcionando normalmente.
     */
    public void sendEmail(String to, String subject, String body) {
        try {
            // Contrói a mensagem básica (sem HTML, suficiente para o desafio)
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject(subject);
            message.setText(body);

            mailSender.send(message);
        } catch (Exception e) {
            // Loga o erro sem derrubar o fluxo da aplicação
            System.err.println("Falha ao enviar e-mail para " + to + ": " + e.getMessage());
        }
    }
}