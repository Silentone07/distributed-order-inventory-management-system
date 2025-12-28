package com.example.productservice.controller;

import com.example.productservice.dto.ReserveResponse;
import com.example.productservice.dto.ReserveStock;
import com.example.productservice.entity.Product;
import com.example.productservice.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping
    public Product addProduct(@RequestBody Product product) {
        return productService.addProduct(product);
    }

    @GetMapping
    public List<Product> getAllProduct() {
        return productService.getAllProduct();
    }

    @GetMapping("/{id}")
    public Product getProductById(@PathVariable Long id) {
         return productService.getById(id);
    }
    @PutMapping("/{id}")
    public Product updateProduct(@RequestBody Product product, @PathVariable Long id) {
         return productService.updateProduct(product,id);
    }
    @DeleteMapping("/{id}")
    public void deleteProduct( @PathVariable Long id) {
        productService.deleteProductById(id);
    }
  @PostMapping("/reserve")
    public ResponseEntity<ReserveResponse>reserveStockDetails(@RequestBody ReserveStock reserveStock) {
        boolean reserveDet=productService.reserveStock(reserveStock);
        if(reserveDet) {
            return ResponseEntity.ok( new ReserveResponse(true,"Stock Reserved successfully"));
        } else {
             return ResponseEntity.badRequest().body( new ReserveResponse(false,"Insufficient stock"));
        }
    }

    @PostMapping("/release")
    public ResponseEntity<ReserveResponse>releaseStockDetails(@RequestBody ReserveStock reserveStock) {
        boolean reserveDet=productService.relaseStock(reserveStock);
        if(reserveDet) {
            return ResponseEntity.ok( new ReserveResponse(true,"Stock Released successfully"));
        } else {
            return ResponseEntity.badRequest().body( new ReserveResponse(false,"Insufficient stock"));
        }
    }


    @GetMapping("/test")
    public String test() {
        return "Product Service is running!";
    }
}
