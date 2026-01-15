package com.inventory.system.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderEvent {

    private String eventType;   // ORDER_CREATED
    private String orderNumber;
    private String sku;
    private Integer quantity;
    private String status;
}
