package com.comitialsign.api.controller;

import com.comitialsign.api.dtos.user.LoginDTO;
import com.comitialsign.api.dtos.user.LoginResponseDTO;
import com.comitialsign.api.dtos.user.RegisterRequestDTO;
import com.comitialsign.api.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid LoginDTO data){
        String token = authenticationService.login(data);
        return ResponseEntity.ok(new LoginResponseDTO(token));
    }


    @PostMapping("/signup")
    public ResponseEntity<Void> signup(@RequestBody @Valid RegisterRequestDTO data) {
        authenticationService.signUp(data);
        return ResponseEntity.ok().build();
    }
}
