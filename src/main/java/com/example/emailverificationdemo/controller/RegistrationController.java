package com.example.emailverificationdemo.controller;

import com.example.emailverificationdemo.event.RegistrationCompleteEvent;
import com.example.emailverificationdemo.registration.RegistrationRequest;
import com.example.emailverificationdemo.registration.token.VerificationToken;
import com.example.emailverificationdemo.repository.VerificationTokenRepository;
import com.example.emailverificationdemo.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/register")
public class RegistrationController {
    private final UserService userService;
    private final ApplicationEventPublisher publisher;
    private final VerificationTokenRepository tokenRepository;
    @PostMapping("")
    public String registerUser(@RequestBody RegistrationRequest registrationRequest, final HttpServletRequest request){
        User user = UserService.registerUser(registrationRequest);
        publisher.publishEvent(new RegistrationCompleteEvent(user, applicationUrl(request)));
        return "Your registration is successful! please check your email and complete your registration";

    }
    @GetMapping("verifyEmail")
    public String verifyEmail(@RequestParam("token") String token){
        VerificationToken theToken = tokenRepository.findByToken(token);
        if(theToken.getUser().isEnabled()){
            return "This Account has already been verified please login.";

        }

        String verificationResult = userService.validateToken(token);
        if (verificationResult.equalsIgnoreCase("Valid")){
            return "Email verified successfully. Now you can login to your Account";
        }
        return "Invalid verification token";
    }

    public String applicationUrl(HttpServletRequest request) {
        return "http://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath();
    }
}
