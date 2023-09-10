package com.example.emailverificationdemo.event.listener;

import com.example.emailverificationdemo.event.RegistrationCompleteEvent;
import com.example.emailverificationdemo.service.UserService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.User;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.UUID;
@Slf4j
@Component
@RequiredArgsConstructor

public class RegistrationCompleteEventListener implements ApplicationListener<RegistrationCompleteEvent> {
    private final UserService userService;
    private  JavaMailSender mailSender;
    private User user;
    private User theUser;
    private MimeMessage message;

    @SneakyThrows
    @Override
    public void onApplicationEvent(RegistrationCompleteEvent event) {
         theUser = event.getUser();
        String verificationToken = UUID.randomUUID().toString();
        userService.saveUserVerificationToken(theUser, verificationToken);
        String url = event.getApplicationUrl()+"/register/verifyEmail?token="+verificationToken;
        sendVerificationEmail(url);
        log.info("Please Click the link to verify your registration : {}", url);

    }
    public void sendVerificationEmail(String url) throws MessagingException, UnsupportedEncodingException {
        String subject = "Email verification";
        String senderName = "User Registration portal Service";
        String mailContent = "Hi, " + theUser.getUsername()+ ","+
                "Thanks for Registering with us, " + "Please follow the link below to complete your registration."
                +"a href=/" +url+  "\">verify your email to activate your account</a>"+
                "Thank you Users Registration portal Service";
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        var messageHelper = new MimeMessageHelper(message);
        messageHelper.setFrom("jamesugwuodoke@gmail.com", senderName);
        messageHelper.setTo(theUser.getUsername());
        messageHelper.setSubject(subject);
        messageHelper.setText(mailContent, true);
        mailSender.send(message);
    }
}
