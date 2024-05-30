package com.pastebin.service.impl;

import com.pastebin.service.MailService;
import io.micrometer.core.annotation.Timed;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {

    private final MimeMessageHelper mimeMessageHelper;
    private final JavaMailSender mailSender;

    @Override
    @Timed("sendEmailMethod")
    public void sendMail(String to, String url) {
        try {
            String html = "<h1>Hello,from Pastebin</h1><p>"+ url +"</p>";
            mimeMessageHelper.setTo(to);
            mimeMessageHelper.setFrom("pastebin@gmail.com");
            mimeMessageHelper.setText(html, true);
            mailSender.send(mimeMessageHelper.getMimeMessage());

        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
