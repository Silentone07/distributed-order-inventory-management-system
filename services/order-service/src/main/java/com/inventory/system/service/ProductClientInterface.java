package com.inventory.system.service;

import com.inventory.system.dto.ReserveResponse;
import com.inventory.system.dto.ReserveStock;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "product-service")
public interface ProductClientInterface {

    @PostMapping("/api/products/reserve")
    ReserveResponse reserveStock(@RequestBody ReserveStock reserveStock);
}
