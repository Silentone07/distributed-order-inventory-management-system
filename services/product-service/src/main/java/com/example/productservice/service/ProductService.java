package com.example.productservice.service;

import com.example.productservice.dto.ReserveStock;
import com.example.productservice.entity.Product;
import com.example.productservice.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productrepo;

    public Product addProduct(Product product) {
        return productrepo.save(product);
    }

    public List<Product> getAllProduct() {
        return productrepo.findAll();
    }

    public Product getById(Long id) {
        return productrepo.findById(id).orElseThrow(() -> new RuntimeException("Product not found with id" + id));
    }

    public Product updateProduct(Product product,Long id) {
        Product existingProduct = getById(id);
        existingProduct.setName(product.getName());
        existingProduct.setSku(product.getSku());
        existingProduct.setDescription(product.getDescription());
        existingProduct.setPrice(product.getPrice());
        existingProduct.setQuantity(product.getQuantity());

        return  productrepo.save(existingProduct);
    }
    public void deleteProductById(Long id) {
         productrepo.deleteById(id);
    }
   @Transactional
    public boolean reserveStock(ReserveStock productRequest) {
          Product product=productrepo.findBySku(productRequest.getSku()).orElseThrow(()->
                  new RuntimeException("Product not found"));
          if(product.getQuantity()>=productRequest.getQuantity()) {
              product.setQuantity(product.getQuantity()-productRequest.getQuantity());
              productrepo.save(product);
              return true;
          }

        return false;
    }

}
