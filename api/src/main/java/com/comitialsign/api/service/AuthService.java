package com.comitialsign.api.service;

import com.comitialsign.api.domain.user.User;
import com.comitialsign.api.domain.user.UserRole;
import com.comitialsign.api.dtos.user.LoginDTO;
import com.comitialsign.api.dtos.user.RegisterRequestDTO;
import com.comitialsign.api.infra.security.TokenService;
import com.comitialsign.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AuthService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    TokenService tokenService;

    public String login(LoginDTO data) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.username(), data.password());
        var auth = this.authenticationManager.authenticate(usernamePassword);
        return tokenService.generateToken((User) auth.getPrincipal());
    }

    public void signUp(RegisterRequestDTO data) {
        if(loadUserByUsername(data.username()) != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username já em uso.");
        }

        validatedUsername(data.username());
        validatedPassword(data.password());
        validatedConfirmPassword(data.password(), data.confirmPassword());

        String passwordEncoder = new BCryptPasswordEncoder().encode(data.password());
        User user = new User(data.username(), passwordEncoder, UserRole.USER);

        this.userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return this.userRepository.findByUsername(username);
    }

    private void validatedPassword(String password) {
        if(password.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A campo da senha não pode ser vazio.");
        }
        if(password.length() < 6) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A senha não pode ser menor do que 6 caracteres.");
        }
        if(password.length() > 20) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A senha não pode ser maior do que 20 caracteres.");
        }
    }

    private void validatedConfirmPassword(String password, String confirmPassword) {
        if(!password.equals(confirmPassword)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "As senhas não iguais.");
        }
    }

    private void validatedUsername(String username) {
        if(username.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O nome do usuário não pode ser vazio.");
        }
        if(username.length() < 4) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O nome do usuário não pode ser menor do que 4 caracteres.");
        }
        if(username.length() > 16) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O nome do usuário não pode ser maior do que 16 caracteres.");
        }
    }

}
