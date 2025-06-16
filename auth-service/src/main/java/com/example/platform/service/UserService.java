package com.example.platform.service;

import com.example.platform.dto.LoginRequest;
import com.example.platform.dto.UserDto;
import com.example.platform.entity.Role;
import com.example.platform.entity.User;
import com.example.platform.entity.VerificationToken;
import com.example.platform.exception.ResourceNotFoundException;
import com.example.platform.exception.UserAlreadyExistsException;
import com.example.platform.exception.VerificationTokenException;
import com.example.platform.mapper.UserMapper;
import com.example.platform.repository.RoleRepository;
import com.example.platform.repository.UserRepository;
import com.example.platform.repository.VerificationTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final VerificationTokenRepository verificationTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;


    @Transactional
    public User register(UserDto dto) {
        log.info("UserService:   Registering new user with username={} and email={}", dto.getUsername(),
                dto.getEmail());

        if (userRepository.existsByEmail(dto.getEmail())) {
            log.warn("UserService:  Email {} already exists", dto.getEmail());
            throw new UserAlreadyExistsException("Email already in use");
        }
        User user = userMapper.toEntity(dto, passwordEncoder);

        Role defaultRole = roleRepository
                .findByName("ROLE_USER")
                .orElseThrow(() -> new IllegalStateException("Default role not found"));
        user.getRoles().add(defaultRole);
        User saved = userRepository.save(user);

        String tokenStr = UUID.randomUUID().toString();
        VerificationToken token = VerificationToken.builder()
                .user(saved)
                .token(tokenStr)
                .expiryDate(LocalDateTime.now().plusDays(1))
                .build();
        verificationTokenRepository.save(token);

        log.info("UserService:   User registered successfully: id={}", saved.getId());
        return saved;
    }


    @Transactional
    public void confirmRegistration(String token) {
        VerificationToken vToken = verificationTokenRepository.findByToken(token)
                .orElseThrow(() -> new VerificationTokenException("Invalid verification token"));
        if (vToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new VerificationTokenException("Token expired");
        }
        User user = vToken.getUser();
        user.setEnabled(true);
        userRepository.save(user);
        verificationTokenRepository.delete(vToken);
        log.info("User id={} verified and enabled", user.getId());
    }


    @Transactional(readOnly = true)
    public User login(LoginRequest req) {
        log.info("UserService:  Authenticating user {}", req.getUsername());

        User user = userRepository.findByUsername(req.getUsername())
                .orElseThrow(() -> {
                    log.warn("UserService:  User {} not found", req.getUsername());
                    return new ResourceNotFoundException("User not found");
                });
        if (!user.isEnabled()) {
            log.warn("User {} not verified", req.getUsername());
            throw new VerificationTokenException("Email not verified");
        }
        if (!passwordEncoder.matches(req.getPassword(), user.getPassword())) {
            log.warn("UserService:  Invalid password for user {}", req.getUsername());
            throw new ResourceNotFoundException("Invalid credentials");
        }

        log.info("UserService:  User {} authenticated successfully", req.getUsername());
        return user;
    }
}
