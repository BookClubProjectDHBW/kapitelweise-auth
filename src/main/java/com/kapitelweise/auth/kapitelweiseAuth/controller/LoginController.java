package com.kapitelweise.auth.kapitelweiseAuth.controller;

import com.kapitelweise.auth.kapitelweiseAuth.request.RegistrationRequest;
import com.kapitelweise.auth.kapitelweiseAuth.response.LoginResponse;
import com.kapitelweise.auth.kapitelweiseAuth.response.RegisterResponse;
import com.kapitelweise.auth.kapitelweiseAuth.service.RegistrationService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.kapitelweise.auth.kapitelweiseAuth.request.LoginRequest;
import com.kapitelweise.auth.kapitelweiseAuth.service.JwtService;

@RestController
public class LoginController {

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
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password()));
        String token = jwtService.generateToken(request.username());
        return new LoginResponse(token);
    }
}