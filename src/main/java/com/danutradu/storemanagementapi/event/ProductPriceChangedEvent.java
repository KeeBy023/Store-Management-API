package com.danutradu.storemanagementapi.event;

import com.danutradu.storemanagementapi.entity.Product;

import java.math.BigDecimal;

public record ProductPriceChangedEvent(
        Product product,
        BigDecimal oldPrice,
        BigDecimal newPrice,
        String changedBy
) {}
