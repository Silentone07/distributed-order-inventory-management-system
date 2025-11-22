package com.inventory.system.controller;

import com.inventory.system.dto.PaymentRequest;
import com.inventory.system.dto.PaymentResponse;
import com.inventory.system.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    @Autowired
   private PaymentService paymentService;

   @PostMapping("/pay")
   public ResponseEntity<PaymentResponse>processPayment(@RequestBody PaymentRequest request) {
       PaymentResponse response = paymentService.processPayment(request);
       return ResponseEntity.ok(response);
   }
}
