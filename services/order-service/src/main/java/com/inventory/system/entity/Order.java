package com.inventory.system.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@Table(name="orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String orderNumber = UUID.randomUUID().toString();

    private Long productId;
    private Integer quantity;
    private Double amount;
    private String status;
    private String sku;
    private LocalDateTime createdAt = LocalDateTime.now();
}
