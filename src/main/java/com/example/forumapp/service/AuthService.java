package com.example.forumapp.service;

import com.example.forumapp.configuration.security.JwtTokenUtil;
import com.example.forumapp.domain.mapper.UserMapper;
import com.example.forumapp.domain.dto.AuthenticationResponse;
import com.example.forumapp.domain.dto.LoginRequest;
import com.example.forumapp.domain.dto.RegisterUserRequest;
import com.example.forumapp.domain.exception.EmailExistsException;
import com.example.forumapp.domain.exception.TokenNotFoundException;
import com.example.forumapp.domain.exception.UsernameExistsException;
import com.example.forumapp.domain.model.User;
import com.example.forumapp.domain.model.VerificationToken;
import com.example.forumapp.repository.UserRepository;
import com.example.forumapp.repository.VerificationTokenRepository;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ValidationException;
import java.time.Instant;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AuthService {
    public static final String TOKEN_BASE_PATH = "http://localhost:8080/api/auth/verification?token=";

    private final UserRepository userRepository;
    private final VerificationTokenRepository verificationTokenRepository;
    private final UserMapper userMapper;
    private final MailService mailService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void signUp(RegisterUserRequest request) {
        validateRegistrationRequest(request);

        User user = new User();
        user.setEmail(request.getEmail());
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setCreatedDate(Instant.now());
        user.setEnabled(false);

        userRepository.save(user);

        String token = generateVerificationToken(user);
        String activationLink = TOKEN_BASE_PATH + token;
        String message = "Thank you for registration, " +
                "please confirm your account using activation link: " + activationLink;
        mailService.sendMail(user.getEmail(), "Activate your account", message);
    }

    private void validateRegistrationRequest(RegisterUserRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new EmailExistsException("Email: " + request.getEmail() + " already exists");
        }
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new UsernameExistsException("Username: " + request.getUsername() + " already exists");
        }
        if (!request.getPassword().equals(request.getRePassword())) {
            throw new ValidationException("Passwords doesn't match");
        }
    }

    private String generateVerificationToken(User user) {
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(UUID.randomUUID().toString());
        verificationToken.setUser(user);
        verificationTokenRepository.save(verificationToken);
        return verificationToken.getToken();
    }

    @Transactional
    public void verifyAccount(String token) {
        VerificationToken verificationToken = verificationTokenRepository.findByToken(token)
                .orElseThrow(() -> new TokenNotFoundException("Invalid token"));
        String username = verificationToken.getUser().getUsername();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with name - " + username));
        user.setEnabled(true);
        userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public User getLoggedUser() {
        return (User) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
    }

    @Transactional
    public AuthenticationResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        User user = getLoggedUser();
        String jwtToken = jwtTokenUtil.generate(authentication);

        return new AuthenticationResponse(userMapper.toUserView(user), jwtToken);
    }
}
