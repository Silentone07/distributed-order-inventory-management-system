package com.inventory.system.service;

import com.inventory.system.dto.NotificationRequest;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {
    public void sendNotification(NotificationRequest notificationRequest) {
        System.out.println("To: " + notificationRequest.getEmail());
        System.out.println("Order: " + notificationRequest.getOrderNumber());
        System.out.println("Message: " + notificationRequest.getMessage());
    }
}
