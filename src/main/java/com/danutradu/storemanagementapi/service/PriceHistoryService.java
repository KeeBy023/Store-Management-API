package com.danutradu.storemanagementapi.service;

import com.danutradu.storemanagementapi.entity.PriceHistory;
import com.danutradu.storemanagementapi.entity.Product;
import com.danutradu.storemanagementapi.repository.PriceHistoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PriceHistoryService {

    private final PriceHistoryRepository priceHistoryRepository;

    public void recordPriceChange(Product product, BigDecimal oldPrice, BigDecimal newPrice, String changedBy) {
        log.info("Recording price change for product {}: {} -> {}", product.getId(), oldPrice, newPrice);
        var priceHistory = new PriceHistory();
        priceHistory.setProduct(product);
        priceHistory.setOldPrice(oldPrice);
        priceHistory.setNewPrice(newPrice);
        priceHistory.setChangedAt(LocalDateTime.now());
        priceHistory.setChangedBy(changedBy);
        priceHistoryRepository.save(priceHistory);
    }

    public List<PriceHistory> getPriceHistory(Long productId) {
        log.info("Retrieving price history for product: {}", productId);
        return priceHistoryRepository.findByProductIdOrderByChangedAtDesc(productId);
    }
}
