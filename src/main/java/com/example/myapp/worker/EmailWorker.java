package com.example.myapp.worker;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.example.myapp.dtos.EmailMessage;

import lombok.*;

@Service
@RequiredArgsConstructor
public class EmailWorker {

    private final JavaMailSender mailSender;

    @RabbitListener(queues = "email.queue")
    public void receiveEmailMessage(EmailMessage emailMessage) {
        try {
            send(emailMessage);
            System.out.println("Email sent successfully to: " + emailMessage.getTo());
        } catch (Exception e) {
            System.err.println("Failed to send email to " + emailMessage.getTo() + ": " + e.getMessage());
            // In a real application, you might want to implement retry logic here,
            // possibly by republishing to a dead-letter queue or logging for manual intervention.
        }
    }

    private void send(EmailMessage email) {

        SimpleMailMessage message =
            new SimpleMailMessage();

        message.setTo(email.getTo());
        message.setSubject(email.getSubject());
        message.setText(email.getContent());

        mailSender.send(message);
    }
}
