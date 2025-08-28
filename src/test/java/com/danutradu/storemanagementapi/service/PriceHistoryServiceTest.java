package com.danutradu.storemanagementapi.service;

import com.danutradu.storemanagementapi.entity.PriceHistory;
import com.danutradu.storemanagementapi.entity.Product;
import com.danutradu.storemanagementapi.repository.PriceHistoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PriceHistoryServiceTest {

    @Mock
    private PriceHistoryRepository priceHistoryRepository;

    @InjectMocks
    private PriceHistoryService priceHistoryService;

    private Product testProduct;

    @BeforeEach
    void setUp() {
        testProduct = new Product(1L, "Test Product", "Description", new BigDecimal("10.99"), 50);
    }

    @Test
    void recordPriceChange() {
        var oldPrice = new BigDecimal("10.99");
        var newPrice = new BigDecimal("12.99");
        var changedBy = "admin";

        priceHistoryService.recordPriceChange(testProduct, oldPrice, newPrice, changedBy);

        var captor = ArgumentCaptor.forClass(PriceHistory.class);
        verify(priceHistoryRepository).save(captor.capture());

        var savedHistory = captor.getValue();
        assertEquals(testProduct, savedHistory.getProduct());
        assertEquals(oldPrice, savedHistory.getOldPrice());
        assertEquals(newPrice, savedHistory.getNewPrice());
        assertEquals(changedBy, savedHistory.getChangedBy());
        assertNotNull(savedHistory.getChangedAt());
    }

    @Test
    void getPriceHistory() {
        var history1 = new PriceHistory();
        var history2 = new PriceHistory();
        when(priceHistoryRepository.findByProductIdOrderByChangedAtDesc(1L)).thenReturn(Arrays.asList(history1, history2));

        var result = priceHistoryService.getPriceHistory(1L);

        assertEquals(2, result.size());
        verify(priceHistoryRepository).findByProductIdOrderByChangedAtDesc(1L);
    }
}