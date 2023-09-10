package com.example.emailverificationdemo.service;

import com.example.emailverificationdemo.exception.UserAlreadyExistsException;
import com.example.emailverificationdemo.registration.RegistrationRequest;
import com.example.emailverificationdemo.registration.token.VerificationToken;
import com.example.emailverificationdemo.repository.UserRepository;
import com.example.emailverificationdemo.repository.VerificationTokenRepository;
import com.example.emailverificationdemo.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.List;
import java.util.Optional;
@Service
@RequiredArgsConstructor
public class UserService implements IUserService {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;
    private final VerificationTokenRepository tokenRepository;

    public static org.apache.catalina.User registerUser(RegistrationRequest registrationRequest) {
        return null;
    }

    @Override
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @Override
    public User registration(RegistrationRequest request) {
        Optional<User> user = this.findByEmail(request.email());
        if(user.isPresent()){
            throw new UserAlreadyExistsException("User with this email "+request.email() +" already exist");
        }
        var newUser = new User();
        newUser.setFirstName(request.firstName());
        newUser.setLastName(request.lastName());
        newUser.setEmail(request.email());
        newUser.setPassword(passwordEncoder.encode(request.password()));
        newUser.setRole(request.role());

        return userRepository.save(newUser);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public void saveUserVerificationToken(org.apache.catalina.User theUser, String verificationToken) {

    }

    @Override
    public String validateToken(String theToken) {
        VerificationToken token = tokenRepository.findByToken(theToken);
        if(token == null){
            return "Invalid verification token";
        }
        User user = token.getUser();
        Calendar calendar = Calendar.getInstance();
        if((token.getTokenExpirationTime().getTime() - calendar.getTime().getTime()) <= 0){
            tokenRepository.delete(token);
            return "Token have Already Expired";

        }
        user.setEnabled(true);
        userRepository.save(user);
        return "valid";
    }

//      @Override
    public void saveUserVerificationToken(User theUser, String token){
        var verificationToken = new VerificationToken(token, theUser);
        tokenRepository.save(verificationToken);


    }
}
