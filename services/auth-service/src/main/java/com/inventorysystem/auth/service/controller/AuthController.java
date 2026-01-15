package com.inventorysystem.auth.service.controller;

import com.inventorysystem.auth.service.dto.LoginRequest;
import com.inventorysystem.auth.service.dto.LoginResponse;
import com.inventorysystem.auth.service.service.AuthService;
import com.inventorysystem.auth.service.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth/api")
public class AuthController {

    private  AuthService authService;

    @Autowired
    public void setAuthService(AuthService authService) {
        this.authService=authService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        if(!authService.validateUser(loginRequest.getUserName(), loginRequest.getPassword())) {
              return ResponseEntity.status(401).body("Invalid credentials");
        }

        String token= JwtUtil.generateToken(loginRequest.getUserName());

        return ResponseEntity.ok(new LoginResponse(token));
    }

}
