package com.inventory.system.controller;

import com.inventory.system.dto.OrderRequest;
import com.inventory.system.entity.Order;
import com.inventory.system.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
     private OrderService orderService;
//    @PostMapping
//     public Order addOrder(@RequestBody Order order) {
//
//         return  orderService.createOrder(order);
//     }
     @GetMapping("/{id}")
     public Order getOrderById(@PathVariable long id) {
         return orderService.getOrderDetailsById(id);
     }
     @GetMapping
     public List<Order>getAllOrderDetails() {
         return orderService.getAllOrderDetails();
     }
     @PutMapping("/{id}")
     public Order updateOrder( @RequestBody Order updatedOrder, @PathVariable  long id) {
          return orderService.updateOrderDetails(updatedOrder,id);
     }
     @DeleteMapping("/{id}")
     public void deleteOrder(@PathVariable long id) {
          orderService.deleteOrder(id);
     }
//     @PostMapping("/create")
//     public Order createOrder(@RequestBody OrderRequest orderRequest) {
//          return orderService.createOrder(orderRequest);
//     }

//    @PostMapping("/create1")
//    public ResponseEntity<String> createNewOrder(@RequestBody OrderRequest orderRequest) {
//        try {
//            // Call service method that interacts with Product Service
//            String response = orderService.createNewOrder(orderRequest);
//
//            return ResponseEntity.ok(response);
//
//        } catch (Exception e) {
//            // Catch any exception (e.g., connection, bad request, etc.)
//            e.printStackTrace();
//            return ResponseEntity.badRequest()
//                    .body("❌ Failed to create order: " + e.getMessage());
//        }
//    }

    @PostMapping("/create")
    public ResponseEntity<Order> createNewOrder(@RequestBody OrderRequest orderRequest) {
      Order order=orderService.createOrder(orderRequest);
        return ResponseEntity.ok(order);
    }
}
