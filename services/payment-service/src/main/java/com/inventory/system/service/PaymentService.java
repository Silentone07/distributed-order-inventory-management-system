package com.inventory.system.service;

import com.inventory.system.dto.PaymentRequest;
import com.inventory.system.dto.PaymentResponse;
import com.inventory.system.entity.Payment;
import com.inventory.system.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    public PaymentResponse processPayment(PaymentRequest paymentRequest) {
        boolean paymentSuccess = Math.random() > 0.2;
        Payment payment =new Payment();
        payment.setOrderNumber(paymentRequest.getOrderNumber());
        payment.setAmount(paymentRequest.getAmount());
        payment.setPaymentStatus(paymentSuccess ? "SUCCESS" : "FAILED");
        paymentRepository.save(payment);

        return paymentSuccess
                ? new PaymentResponse(true, "Payment successful")
                : new PaymentResponse(false, "Payment failed");
    }
}