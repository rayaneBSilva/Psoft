package com.ufcg.psoft.commerce.service.notificacao;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender emailSender;

    @Transactional
    public void sendEmail(String toEmail, String subject, String body) {
        SimpleMailMessage message=new SimpleMailMessage();
        message.setFrom("pitsadelivery.contato@gmail.com");
        message.setTo(toEmail);
        message.setText(body);
        message.setSubject(subject);
        emailSender.send(message);
        System.out.println("Email enviado...");

    }
}