package com.example.forumapp.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class MailService {
    private final JavaMailSender mailSender;

    @Async
    public void sendMail(String recipient, String subject, String text) {
        MimeMessagePreparator messagePreparator = mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            messageHelper.setFrom("forumapp@example.com");
            messageHelper.setTo(recipient);
            messageHelper.setSubject(subject);
            messageHelper.setText(text);
        };
        try {
            mailSender.send(messagePreparator);
            log.info("Email sent");
        } catch (MailException e) {
            log.error("Exception occurred when sending mail", e);
        }
    }
}
