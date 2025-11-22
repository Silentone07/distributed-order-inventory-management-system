package com.example.productservice.dto;

import lombok.Data;

@Data
public class ReserveStock {

    private String sku;
    private Integer quantity;
}
