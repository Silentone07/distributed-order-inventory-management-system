package com.inventory.system.service;

import com.inventory.system.dto.NotificationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class NotificationClient {

    @Autowired
    private WebClient.Builder webClientBuilder;

    @Autowired
   private NotificationFeignClient notificationFeignClient;
    public void sendNotification(NotificationRequest request) {
//        webClientBuilder.build()
//                .post()
//                .uri("http://localhost:8084/api/notify")
//                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
//                .bodyValue(request)
//                .retrieve()
//                .bodyToMono(String.class)
//                .block();
        notificationFeignClient.sendNotification(request);

    }
}
