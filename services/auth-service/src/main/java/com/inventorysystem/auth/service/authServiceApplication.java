package com.inventorysystem.auth.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class authServiceApplication {

    public static void main(String[]args) {
        SpringApplication.run(authServiceApplication.class, args);
         System.out.println("Auth service is running...");
    }
}
