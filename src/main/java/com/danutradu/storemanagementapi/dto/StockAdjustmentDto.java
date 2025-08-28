package com.danutradu.storemanagementapi.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class StockAdjustmentDto {
    @NotNull(message = "Adjustment value is required")
    private Integer adjustment;
    private String reason = "Manual adjustment";
}
