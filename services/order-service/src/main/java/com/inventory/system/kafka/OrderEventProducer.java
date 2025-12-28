package com.inventory.system.kafka;

import com.inventory.system.dto.OrderEvent;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;

@Data
@RequiredArgsConstructor
public class OrderEventProducer {

    private final KafkaTemplate<String, OrderEvent> kafkaTemplate;

    public void sendOrderCreatedEvent(OrderEvent event) {
        kafkaTemplate.send("order-event",event);

        System.out.println("Send kafka Event : " + event);
    }
}
