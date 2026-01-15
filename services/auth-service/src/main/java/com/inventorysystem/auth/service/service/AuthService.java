package com.inventorysystem.auth.service.service;

import org.springframework.stereotype.Service;

@Service
public class AuthService {

    public boolean validateUser(String userName,String password) {

        return "admin".equalsIgnoreCase(userName) && "admin123".equalsIgnoreCase(password);
    }
}
