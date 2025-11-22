package com.inventory.system.dto;

import lombok.Data;

@Data
public class ProductDto {

    private Long id;
    private String sku;
    private String name;
    private Double price;
    private Integer quantity;
}
