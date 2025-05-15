package com.comitialsign.api.controller;

import com.comitialsign.api.domain.user.User;
import com.comitialsign.api.domain.user.UserRole;
import com.comitialsign.api.dtos.user.LoginDTO;
import com.comitialsign.api.dtos.user.LoginResponseDTO;
import com.comitialsign.api.dtos.user.RegisterRequestDTO;
import com.comitialsign.api.infra.security.TokenService;
import com.comitialsign.api.repository.UserRepository;
import jakarta.validation.Valid;
import jdk.jfr.Description;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/auth")
@Description("Auth user")
@Transactional
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid LoginDTO data) {
       var usernamePassword = new UsernamePasswordAuthenticationToken(data.username(), data.password());
       var auth = authenticationManager.authenticate(usernamePassword);
       if(auth.isAuthenticated()) {
           String token = tokenService.generateToken((User)auth.getPrincipal());
           return ResponseEntity.ok(new LoginResponseDTO(token));
       }
       throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Nome de usuário ou senha incorreto.");
    }


    @PostMapping("/signup")
    public ResponseEntity<Void> signup(@RequestBody @Valid RegisterRequestDTO data) {
        if (this.userRepository.findByUsername(data.username()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Nome do usuário esta já em uso.");
        }
        if(!data.password().equals(data.confirmPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "As senhas precisam ser iguais.");
        }
        if(data.password().length() < 6 || data.password().length() > 16) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A senha deve ter entre 6 e 16 caracteres.");
        }
        if(data.username().length() < 4 || data.username().length() > 20) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O username deve ter entre 4 e 20 caracteres.");
        }

        String passwordEncoder = new BCryptPasswordEncoder().encode(data.password());
        User user = new User(data.username(), passwordEncoder, UserRole.USER);

        this.userRepository.save(user);

        ResponseEntity.status(HttpStatus.CREATED).body("Usuário criado com sucesso.");
        return ResponseEntity.ok().build();
    }
}
