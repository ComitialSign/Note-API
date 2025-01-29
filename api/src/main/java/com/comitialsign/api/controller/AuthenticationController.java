package com.comitialsign.api.controller;

import com.comitialsign.api.domain.user.User;
import com.comitialsign.api.domain.user.UserRole;
import com.comitialsign.api.dtos.user.AuthenticationDTO;
import com.comitialsign.api.dtos.user.RegisterDTO;
import com.comitialsign.api.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Valid AuthenticationDTO data) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.username(), data.password());
        var auth = this.authenticationManager.authenticate(usernamePassword);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/signup")
    public ResponseEntity signup(@RequestBody @Valid RegisterDTO data) {
        if(this.userRepository.findByUsername(data.username()) != null) return ResponseEntity.badRequest().build();

        String encryptedPassword = passwordEncoder.encode(data.password());
        User newUser = new User(data.username(), encryptedPassword, UserRole.USER);

        this.userRepository.save(newUser);

        return ResponseEntity.ok().build();
    }
}
