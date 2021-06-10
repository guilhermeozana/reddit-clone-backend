package com.guilherme.redditclone.service;

import java.time.Instant;
import java.util.UUID;

import javax.transaction.Transactional;

import com.guilherme.redditclone.dto.RegisterRequest;
import com.guilherme.redditclone.exception.SpringRedditException;
import com.guilherme.redditclone.model.NotificationEmail;
import com.guilherme.redditclone.model.User;
import com.guilherme.redditclone.model.VerificationToken;
import com.guilherme.redditclone.repository.UserRepository;
import com.guilherme.redditclone.repository.VerificationTokenRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final VerificationTokenRepository verificationTokenRepository;
    private final MailService mailService;
    
    @Transactional
    public void signup(RegisterRequest registerRequest){
        User user = User.builder()
            .username(registerRequest.getUsername())
            .email(registerRequest.getEmail())
            .password(passwordEncoder.encode(registerRequest.getPassword()))
            .created(Instant.now())
            .enabled(false)
            .build();
        
        userRepository.save(user);

        String token = generateVerificationToken(user);

        mailService.sendMail(NotificationEmail.builder()
                        .subject("Please, activate your account")
                        .recipient(user.getEmail())
                        .body("Thank you for signing up to Reddit Clone, "+
                            "please click on the below url to activate your account: "+
                            "http://localhost:8080/api/auth/accountVerification/"+ token)
                        .build());
    }

    private String generateVerificationToken(User user) {
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = VerificationToken.builder()
            .token(token)
            .user(user)
            .build();

            verificationTokenRepository.save(verificationToken);
        return token;
    }

    public void verifyAccount(String token) {
        VerificationToken verificationToken = verificationTokenRepository.findByToken(token).orElseThrow(() ->
            new SpringRedditException("Invalid Token"));

        fetchUserAndEnable(verificationToken);
    }

    @Transactional
    private void fetchUserAndEnable(VerificationToken verificationToken) {
        String username = verificationToken.getUser().getUsername();
        
        User user = userRepository.findByUsername(username).orElseThrow(() ->
        new SpringRedditException("Invalid User"));

        user.setEnabled(true);

        userRepository.save(user);
    }
}
