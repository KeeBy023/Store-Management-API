package com.danutradu.storemanagementapi.event;

import com.danutradu.storemanagementapi.service.PriceHistoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class PriceHistoryEventHandler {

    private final PriceHistoryService priceHistoryService;

    @EventListener
    public void handlePriceChange(ProductPriceChangedEvent event) {
        log.info("Handling price change event for product: {}", event.product().getId());
        priceHistoryService.recordPriceChange(
                event.product(),
                event.oldPrice(),
                event.newPrice(),
                event.changedBy()
        );
    }
}
