package com.inventory.system.service;

import com.inventory.system.dto.NotificationRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "notification-service")
public interface NotificationFeignClient {
    @PostMapping("/api/notify")
    public String sendNotification( @RequestBody  NotificationRequest request);
}
