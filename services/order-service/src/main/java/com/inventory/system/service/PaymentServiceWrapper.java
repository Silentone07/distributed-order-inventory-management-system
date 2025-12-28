package com.inventory.system.service;

import com.inventory.system.dto.PaymentRequest;
import com.inventory.system.dto.PaymentResponse;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class PaymentServiceWrapper {

    @Autowired
   private final PaymentFeignClient paymentFeignClient;

    @Value("${test.failure.enabled:false}")
    private boolean failMode;

    public PaymentServiceWrapper(PaymentFeignClient  paymentFeignClient) {
        this.paymentFeignClient=paymentFeignClient;
    }
    @CircuitBreaker(name = "paymentCB", fallbackMethod = "paymentFallback")
    public PaymentResponse processPayment(PaymentRequest paymentRequest) {

        if(failMode) {
            throw new RuntimeException("Forced failure for circuit breaker testing");
        }
         return paymentFeignClient.processPayment(paymentRequest);
    }

    public PaymentResponse paymentFallback(PaymentRequest req, Throwable ex) {
        return new PaymentResponse(false, "Fallback triggered: " + ex.getMessage());
    }
}
