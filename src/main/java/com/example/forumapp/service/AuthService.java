package com.example.forumapp.service;

import com.example.forumapp.domain.UserMapper;
import com.example.forumapp.domain.dto.RegisterUserRequest;
import com.example.forumapp.domain.dto.UserView;
import com.example.forumapp.domain.exception.EmailExistsException;
import com.example.forumapp.domain.exception.UsernameExistsException;
import com.example.forumapp.domain.model.User;
import com.example.forumapp.repository.UserRepo;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ValidationException;
import java.time.Instant;

@Service
@AllArgsConstructor
public class AuthService {
    private final UserRepo userRepo;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserView signUp(RegisterUserRequest request) {
        validateRegistrationRequest(request);

        User user = new User();
        user.setEmail(request.getEmail());
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setCreatedDate(Instant.now());
        user.setEnabled(false);

        userRepo.save(user);
        return userMapper.toUserView(user);
    }

    private void validateRegistrationRequest(RegisterUserRequest request) {
        if (userRepo.existsByEmail(request.getEmail())) {
            throw new EmailExistsException("Email: " + request.getEmail() + " already exists");
        }
        if (userRepo.existsByUsername(request.getUsername())) {
            throw new UsernameExistsException("Username: " + request.getUsername() + " already exists");
        }
        if (!request.getPassword().equals(request.getRePassword())) {
            throw new ValidationException("Passwords doesn't match");
        }
    }
}
