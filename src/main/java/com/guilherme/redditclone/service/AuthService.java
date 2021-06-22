package com.guilherme.redditclone.service;

import java.time.Instant;
import java.util.UUID;

import com.guilherme.redditclone.dto.AuthenticationResponse;
import com.guilherme.redditclone.dto.LoginRequest;
import com.guilherme.redditclone.dto.RegisterRequest;
import com.guilherme.redditclone.exception.RedditCloneException;
import com.guilherme.redditclone.model.NotificationEmail;
import com.guilherme.redditclone.model.User;
import com.guilherme.redditclone.model.VerificationToken;
import com.guilherme.redditclone.repository.UserRepository;
import com.guilherme.redditclone.repository.VerificationTokenRepository;
import com.guilherme.redditclone.security.JwtProvider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final VerificationTokenRepository verificationTokenRepository;
    private final MailService mailService;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    
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
                        .body("Thank you for signing up to Reddit Clone, please click on the below url to activate"
                                +" your account: http://localhost:8080/api/auth/accountVerification/"+ token)
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
            new RedditCloneException("Invalid Token"));

        fetchUserAndEnable(verificationToken);
    }

    @Transactional
    private void fetchUserAndEnable(VerificationToken verificationToken) {
        String username = verificationToken.getUser().getUsername();
        
        User user = userRepository.findByUsername(username).orElseThrow(() ->
        new RedditCloneException("Invalid User"));

        user.setEnabled(true);

        userRepository.save(user);
    }

    public AuthenticationResponse login(LoginRequest loginRequest) {
        Authentication authenticate = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authenticate);

        String token = jwtProvider.generateToken(authenticate);
            
        return AuthenticationResponse.builder()
                .authenticationToken(token)
                .username(loginRequest.getUsername())
                .build();
    }

    @Transactional(readOnly = true)
	public User getCurrentUser() {
		org.springframework.security.core.userdetails.User principal = (org.springframework.security.core.userdetails.User) SecurityContextHolder
                .getContext().getAuthentication().getPrincipal();
        String username = principal.getUsername();

        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RedditCloneException("No user found with name "+username));
	}
}
