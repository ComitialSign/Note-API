package com.comitialsign.api.controller;

import com.comitialsign.api.domain.user.User;
import com.comitialsign.api.dtos.user.LoginDTO;
import com.comitialsign.api.dtos.user.LoginResponseDTO;
import com.comitialsign.api.dtos.user.RegisterRequestDTO;
import com.comitialsign.api.infra.security.TokenService;
import com.comitialsign.api.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid LoginDTO data){
        try {
            var usernamePassword = this.authService.login(data);
            var auth = this.authenticationManager.authenticate(usernamePassword);
            String token = tokenService.generateToken((User) auth.getPrincipal());

            return ResponseEntity.ok(new LoginResponseDTO(token));
        } catch (BadCredentialsException e) {
            throw  new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Nome do usuário ou senha incorreto.");
        } catch (Exception e) {
            throw  new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro ao fazer login.");
        }
    }


    @PostMapping("/signup")
    public ResponseEntity<Void> signup(@RequestBody @Valid RegisterRequestDTO data) {
        authService.signUp(data);
        return ResponseEntity.ok().build();
    }
}
