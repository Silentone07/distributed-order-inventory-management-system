package com.inventory.system.dto;

import com.inventory.system.entity.Order;
import lombok.Data;

@Data
public class OrderResponse {

    private String orderId;
    private String sku;
    private int quantity;
    private String status;

    public OrderResponse(Order order) {
        this.orderId = order.getId().toString();
        this.sku = order.getSku();
        this.quantity = order.getQuantity();
        this.status = order.getStatus();
    }

}
