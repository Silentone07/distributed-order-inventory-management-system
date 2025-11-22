package com.inventory.system.service;

import com.inventory.system.dto.OrderRequest;
import com.inventory.system.dto.ReserveResponse;
import com.inventory.system.dto.ReserveStock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class ProductClient {

    @Autowired
    private  WebClient webClient;
    private final WebClient.Builder webClientBuilder;

    @Autowired
     private ProductClientInterface productClientInterface;

    public ProductClient(WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
    }

    public ReserveResponse reserveStock(OrderRequest orderRequest) {
        ReserveStock reserveStock = new ReserveStock(orderRequest.getSku(), orderRequest.getQuantity());

//        return webClientBuilder.build()
//                .post()
//                .uri("http://localhost:8081/api/products/reserve")
//                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
//                .bodyValue(reserveStock)
//                .retrieve()
//                .bodyToMono(ReserveResponse.class)
//                .block(); // returns ReserveResponse object
        return productClientInterface.reserveStock(reserveStock);
    }

    public void releaseStock(String sku, int quantity) {
        ReserveStock request = new ReserveStock();

        webClient.post()
                .uri("http://localhost:8081/api/products/release")
                .bodyValue(request)
                .retrieve()
                .toBodilessEntity()
                .block();
    }
}
