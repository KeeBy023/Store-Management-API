package com.danutradu.storemanagementapi.service;

import com.danutradu.storemanagementapi.entity.Product;
import com.danutradu.storemanagementapi.exception.InsufficientStockException;
import com.danutradu.storemanagementapi.exception.ProductNotFoundException;
import com.danutradu.storemanagementapi.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryService {

    private final ProductRepository productRepository;

    public List<Product> findLowStockProducts(int threshold) {
        log.info("Finding products with stock below: {}", threshold);
        return productRepository.findByQuantityLessThan(threshold);
    }

    public List<Product> findOutOfStockProducts() {
        log.info("Finding out of stock products");
        return productRepository.findByQuantityEquals(0);
    }

    @Transactional
    public Product adjustStock(Long productId, int adjustment, String reason) {
        log.info("Adjusting stock for product {} by {} units. Reason {}", productId, adjustment, reason);
        var product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + productId));

        int newQuantity = product.getQuantity() + adjustment;
        if (newQuantity < 0) {
            throw new InsufficientStockException("Insufficient stock. Current: " + product.getQuantity() + ", Requested: " + Math.abs(adjustment));
        }

        product.setQuantity(newQuantity);
        return productRepository.save(product);
    }

    public Product restockProduct(Long productId, int quantity) {
        return adjustStock(productId, quantity, "Restock");
    }

    public Product reserveStock(Long productId, int quantity) {
        return adjustStock(productId, -quantity, "Stock reservation");
    }
}
