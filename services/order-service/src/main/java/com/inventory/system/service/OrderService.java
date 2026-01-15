package com.inventory.system.service;

import com.inventory.system.dto.*;
import com.inventory.system.entity.Order;
import com.inventory.system.kafka.OrderEventProducer;
import com.inventory.system.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Log4j2
@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ProductClient productClient;

    @Autowired
   private  NotificationClient notificationClient;

    @Autowired
    private  WebClient webClient;
    private final WebClient.Builder webClientBuilder;

    @Autowired
   private PaymentFeignClient paymentFeignClient;

    @Autowired
   private OrderServiceCircuitBreakerImplementation orderServiceCircuit;

    @Autowired
   private PaymentServiceWrapper paymentServiceWrapper;



   private  OrderEventProducer orderEventProducer;

    public OrderService(WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
    }

//    public Order createOrder(OrderRequest orderRequest) {
//        Boolean reserveSuccess = webClientBuilder.build()
//                .post()
//                .uri("http://localhost:8081/api/products/reserve?sku={sku}&quantity={quantity}",
//                        orderRequest.getSku(), orderRequest.getQuantity())
//                .retrieve()
//                .bodyToMono(Boolean.class)
//                .block();
//
//        // 2️⃣ Create order entity
//        Order order = new Order();
//        order.setSku(orderRequest.getSku());
//        order.setQuantity(orderRequest.getQuantity());
//        order.setAmount(0.0); // optional: can calculate later based on product price
//
//        // 3️⃣ Update status based on reservation result
//        if (Boolean.TRUE.equals(reserveSuccess)) {
//            order.setStatus("CONFIRMED");
//        } else {
//            order.setStatus("FAILED");
//        }
//
//        // 4️⃣ Save order to DB
//        return orderRepository.save(order);
//    }
     public Order getOrderDetailsById(long id) {
         return orderRepository.findById(id).orElseThrow(()->
                 new RuntimeException("Order not found with given id" + id));
     }
     public List<Order> getAllOrderDetails() {
           return orderRepository.findAll();
     }
     public Order updateOrderDetails(Order updatedOrder,Long id) {
         Order existingOrderDet=getOrderDetailsById(id);
         existingOrderDet.setStatus(updatedOrder.getStatus());
         existingOrderDet.setProductId(updatedOrder.getProductId());
         existingOrderDet.setAmount(updatedOrder.getAmount());
         existingOrderDet.setQuantity(updatedOrder.getQuantity());
         return orderRepository.save(existingOrderDet);
     }
     public void deleteOrder(long id) {
         orderRepository.deleteById(id);
     }

//     public OrderResponse placeOrder(OrderRequest request) {
//          boolean reserved=productClient.reserveStock(request.getSku(),request.getQuantity());
//          if(!reserved) {
//              throw new RuntimeException("Stock reservation failed for SKU:" + request.getSku());
//          }
//          Order order=new Order();
//          order.setSku(request.getSku());
//          order.setQuantity(request.getQuantity());
//          order.setStatus("RESERVED");
//         orderRepository.save(order);
//
//         return new OrderResponse(order);
//
//
//     }

//    public String createNewOrder(OrderRequest orderRequest) {
//        ReserveStock reserveStock = new ReserveStock();
//        reserveStock.setSku(orderRequest.getSku());
//        reserveStock.setQuantity(orderRequest.getQuantity());
//
//        System.out.println("👉 Sending request to Product Service: " + reserveStock);
//
//        String response = webClient.post()
//                .uri("/api/products/reserve")
//                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
//                .bodyValue(reserveStock)
//                .retrieve()
//                .bodyToMono(String.class)
//                .block();
//
//        System.out.println("✅ Response from Product Service: " + response);
//
//        return response;
//    }

//    public String reserveProduct(String sku, Integer quantity) {
//        ReserveStock reserveStock = new ReserveStock();
//        reserveStock.setSku(sku);
//        reserveStock.setQuantity(quantity);
//
//        System.out.println("Sending reserveStock to Product Service: " + reserveStock);
//
//        return webClientBuilder.build()
//                .post()
//                .uri("http://localhost:8081/api/products/reserve")
//                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
//                .bodyValue(reserveStock)    // ✅ ensure this sends body
//                .retrieve()
//                .bodyToMono(String.class)
//                .block();
//    }

    public Order createOrder(OrderRequest orderRequest) {
        ReserveStock reserveStock=new ReserveStock(orderRequest.getSku(),orderRequest.getQuantity());

        ReserveResponse reserveResponse = orderServiceCircuit.productReserveResponse(reserveStock);

          Order order=new Order();
          order.setSku(orderRequest.getSku());
          order.setQuantity(orderRequest.getQuantity());
          order.setAmount(orderRequest.getAmount());

          if(reserveResponse!=null && reserveResponse.isSuccess()) {
              order.setStatus("RESERVED");
              PaymentRequest paymentRequest = new PaymentRequest(
                      order.getOrderNumber(),
                      orderRequest.getAmount()
              );
              try {
//                  PaymentResponse paymentResponse=webClientBuilder.build()
//                          .post()
//                          .uri("http://localhost:8083/api/payments/pay")
//                          .bodyValue(paymentRequest)
//                          .retrieve()
//                          .bodyToMono(PaymentResponse.class)
//                          .block();
                  PaymentResponse paymentResponse=paymentServiceWrapper.processPayment(paymentRequest);
                  if(paymentResponse!=null && paymentResponse.isSuccess()) {
                      order.setStatus("CONFIRMED");



                      NotificationRequest notificationRequest= new NotificationRequest(order.getOrderNumber(), "user@example.com",
                              "Your order " + order.getOrderNumber() + " has been completed successfully!");

                      orderServiceCircuit.notificationResponse(notificationRequest);
                  } else {
                      log.info("Orderservice ->" + " paymentresponse : " + paymentResponse);
                      order.setStatus("PAYMENT_FAILED");
                      productClient.releaseStock(orderRequest);
                      NotificationRequest notificationRequest= new NotificationRequest(order.getOrderNumber(), "user@example.com",
                              "Your order " + order.getOrderNumber() + " payment failed!");

                      notificationClient.sendNotification(notificationRequest);
                  }
              } catch(Exception e) {
                  order.setStatus("PAYMENT-ERROR");
              }

          } else {
              order.setStatus("FAILED");
          }

          OrderEvent event=new OrderEvent(
                  "ORDER-CREATED",
                  order.getOrderNumber(),
                  order.getSku(),
                  order.getQuantity(),
                  order.getStatus());

        orderEventProducer.sendOrderCreatedEvent(event);
        return  orderRepository.save(order);
         // return "Order service";
    }
}
