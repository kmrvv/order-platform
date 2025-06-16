package com.example.platform.controller;

import com.example.platform.dto.JwtResponse;
import com.example.platform.dto.LoginRequest;
import com.example.platform.dto.UserDto;
import com.example.platform.entity.User;
import com.example.platform.security.JwtUtils;
import com.example.platform.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {
    private final UserService userService;
    private final JwtUtils jwtUtils;

    @PostMapping("/register")
    public ResponseEntity<Void> register(@Valid @RequestBody UserDto dto) {
        log.debug("Received registration request for {}", dto.getUsername());
        userService.register(dto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@Valid @RequestBody LoginRequest req) {
        log.debug("Received login request for {}", req.getUsername());
        User user = userService.login(req);
        String token = jwtUtils.generateToken(user.getUsername());
        JwtResponse resp = JwtResponse
                .builder()
                .token(token)
                .build();
        return ResponseEntity.ok(resp);
    }

    @GetMapping("/confirm")
    public ResponseEntity<Void> confirmEmail(@RequestParam("token") String token) {
        userService.confirmRegistration(token);
        return ResponseEntity.ok().build();
    }

}
