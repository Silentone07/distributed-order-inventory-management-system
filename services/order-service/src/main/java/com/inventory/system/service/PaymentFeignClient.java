package com.inventory.system.service;

import com.inventory.system.dto.PaymentRequest;
import com.inventory.system.dto.PaymentResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "payment-service")
public interface PaymentFeignClient {

    @PostMapping("/api/payments/pay")
    PaymentResponse processPayment(@RequestBody  PaymentRequest paymentRequest);
}
