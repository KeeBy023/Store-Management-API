package com.danutradu.storemanagementapi.event;

import com.danutradu.storemanagementapi.entity.Product;
import com.danutradu.storemanagementapi.service.PriceHistoryService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class PriceHistoryEventHandlerTest {

    @Mock
    private PriceHistoryService priceHistoryService;

    @InjectMocks
    private PriceHistoryEventHandler eventHandler;

    @Test
    void handlePriceChange() {
        var testProduct = new Product(1L, "Test Product", "Description", new BigDecimal("10.99"), 50);
        var oldPrice = new BigDecimal("10.99");
        var newPrice = new BigDecimal("12.99");
        var changedBy = "admin";
        var event = new ProductPriceChangedEvent(testProduct, oldPrice, newPrice, changedBy);

        eventHandler.handlePriceChange(event);

        verify(priceHistoryService).recordPriceChange(testProduct, oldPrice, newPrice, changedBy);
    }
}