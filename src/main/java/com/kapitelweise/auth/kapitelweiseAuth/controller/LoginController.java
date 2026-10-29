package com.kapitelweise.auth.kapitelweiseAuth.controller;

import com.kapitelweise.auth.kapitelweiseAuth.request.RegistrationRequest;
import com.kapitelweise.auth.kapitelweiseAuth.response.LoginResponse;
import com.kapitelweise.auth.kapitelweiseAuth.response.RegisterResponse;
import com.kapitelweise.auth.kapitelweiseAuth.service.RegistrationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.kapitelweise.auth.kapitelweiseAuth.request.LoginRequest;
import com.kapitelweise.auth.kapitelweiseAuth.service.JwtService;

@RestController
public class LoginController {
    private static final Logger log = LoggerFactory.getLogger(LoginController.class);

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final RegistrationService registrationService;

    public LoginController(AuthenticationManager authenticationManager, JwtService jwtService, RegistrationService registrationService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.registrationService = registrationService;
    }




    @PostMapping("/register")
    public RegisterResponse register(@RequestBody RegistrationRequest request){
        return registrationService.register(request);

    }
    //so wie ich es verstehe, das ist nur die Logik um den Token zu erzeugen! Basic Auth ist immer noch möglich!!!
    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.username(), request.password()));
        } catch (AuthenticationException e) {
            log.warn("Login von " + request.username() + " fehlgeschlagen, Passwort oder Name falsch");
            throw e;
        }
        String token = jwtService.generateToken(request.username());
        log.info(request.username() + " hat sich eingeloggt, Token ist raus");
        return new LoginResponse(token);
    }
}