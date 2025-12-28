package com.inventory.system.service;

import com.inventory.system.dto.ReserveResponse;
import com.inventory.system.dto.ReserveStock;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "product-service")
public interface ProductFeignClient {

    @PostMapping("/api/products/reserve")
    ReserveResponse reserveStock(@RequestBody ReserveStock reserveStock);

    @PostMapping("/api/products/release")
    ReserveResponse releaseStock(@RequestBody ReserveStock reserveStock);


}
