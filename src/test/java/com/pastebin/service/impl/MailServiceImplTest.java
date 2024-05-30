package com.pastebin.service.impl;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class MailServiceImplTest {

    @Mock
    private MimeMessageHelper helper;
    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private MailServiceImpl mailService;

    @Test
    void sendMail() throws MessagingException {

        String to = "test@gmail.com";
        String url = "http://pastebin.com";

        mailService.sendMail(to, url);

        verify(helper).setTo(to);
        verify(helper).setFrom("pastebin@gmail.com");
        verify(mailSender).send(helper.getMimeMessage());

    }
}