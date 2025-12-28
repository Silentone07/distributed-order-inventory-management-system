package com.inventory.system.service;

import com.inventory.system.dto.*;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceCircuitBreakerImplementation {

    @Autowired
     private PaymentFeignClient paymentFeignClient;

    @Autowired
    private ProductFeignClient productFeignClient;

    @Autowired
   private NotificationFeignClient notificationFeignClient;

   @RateLimiter(name = "paymentRateLimiter", fallbackMethod = "paymentRateLimitFallback")
    @CircuitBreaker(
            name = "paymentServiceCB",
            fallbackMethod = "paymentFallback"
    )
     public PaymentResponse paymentService(PaymentRequest paymentRequest) {
          return paymentFeignClient.processPayment(paymentRequest);
     }

    @CircuitBreaker(name="productService" , fallbackMethod = "reserveProductFallback")
     public ReserveResponse productReserveResponse(ReserveStock reserveStock) {
        return  productFeignClient.reserveStock(reserveStock);
     }

    public PaymentResponse paymentFallback(PaymentRequest request, Throwable t) {
        PaymentResponse response = new PaymentResponse();
       // response.setSuccess("FAILED");
        response.setMessage("Payment service unavailable, fallback triggered!");
        return response;
    }

    public PaymentResponse paymentRateLimitFallback(
            PaymentRequest request,
            io.github.resilience4j.ratelimiter.RequestNotPermitted ex) {

        return new PaymentResponse(
                false,
                "Too many payment requests. Please try again later."
        );
    }


//    @CircuitBreaker(name="notificationService" , fallbackMethod = "notificationFallback")
    public String notificationResponse(NotificationRequest notificationRequest) {
        return notificationFeignClient.sendNotification(notificationRequest);
    }

    public ReserveResponse reserveProductFallback(ReserveStock reserveStock, Throwable ex) {
        return new ReserveResponse(false, "Fallback: Product service unavailable");
    }

//public String notificationFallback() {
//        return "Fallback : Noitication service is down";
//    }



}
