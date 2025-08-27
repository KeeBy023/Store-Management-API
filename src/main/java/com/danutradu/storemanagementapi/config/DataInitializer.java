package com.danutradu.storemanagementapi.config;

import com.danutradu.storemanagementapi.entity.Product;
import com.danutradu.storemanagementapi.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final ProductRepository productRepository;


    @Override
    public void run(String... args) {
        if (productRepository.count() == 0) {
            log.info("Initializing sample data...");

            productRepository.save(new Product(null, "Laptop", "Gaming laptop with RTX 4080", new BigDecimal("1299.99"), 15));
            productRepository.save(new Product(null, "Mouse", "Wireless mouse", new BigDecimal("79.99"), 50));
            productRepository.save(new Product(null, "Keyboard", "Wireless keyboard", new BigDecimal("149.99"), 30));
            productRepository.save(new Product(null, "Monitor", "27-inch 4K monitor", new BigDecimal("399.99"), 25));
            productRepository.save(new Product(null, "Headset", "Noise-cancelling headset", new BigDecimal("199.99"), 20));

            log.info("Sample data initialized successfully");
        }
    }
}
