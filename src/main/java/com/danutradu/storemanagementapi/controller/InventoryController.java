package com.danutradu.storemanagementapi.controller;

import com.danutradu.storemanagementapi.dto.StockAdjustmentDto;
import com.danutradu.storemanagementapi.entity.Product;
import com.danutradu.storemanagementapi.service.InventoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/inventory")
@Tag(name = "Inventory", description = "Product management operations")
@SecurityRequirement(name = "basicAuth")
@Validated
public class InventoryController {

    private final InventoryService inventoryService;

    @GetMapping("/low-stock")
    @Operation(summary = "Get low stock products", description = "Retrieve products below specified stock threshold")
    public ResponseEntity<List<Product>> getLowStockProducts(@RequestParam(defaultValue = "10") int threshold) {
        log.info("Request to get low stock products with threshold: {}", threshold);
        var products = inventoryService.findLowStockProducts(threshold);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/out-of-stock")
    @Operation(summary = "Get out of stock products", description = "Retrieve products with zero quantity")
    public ResponseEntity<List<Product>> getOutOfStockProducts() {
        log.info("Request to get out of stock products");
        var products = inventoryService.findOutOfStockProducts();
        return ResponseEntity.ok(products);
    }

    @PostMapping("/{productId}/adjust")
    @Operation(summary = "Adjust product stock", description = "Increase or decrease product stock quantity")
    public ResponseEntity<Product> adjustStock(@PathVariable Long productId, @Valid @RequestBody StockAdjustmentDto request) {
        log.info("Request to adjust stock for product {} by {} units", productId, request.getAdjustment());
        var product = inventoryService.adjustStock(productId, request.getAdjustment(), request.getReason());
        return ResponseEntity.ok(product);
    }

    @PostMapping("/{productId}/restock")
    @Operation(summary = "Restock product", description = "Add stock to a product")
    public ResponseEntity<Product> restockProduct(@PathVariable Long productId,
                                                  @RequestParam @Min(value = 1, message = "Quantity must be positive") int quantity) {
        log.info("Request to restock product {} with {} units", productId, quantity);
        var product = inventoryService.restockProduct(productId, quantity);
        return ResponseEntity.ok(product);
    }

    @PostMapping("/{productId}/reserve")
    @Operation(summary = "Reserve product stock", description = "Reserve stock to a product")
    public ResponseEntity<Product> reserveStock(@PathVariable Long productId,
                                                @RequestParam @Min(value = 1, message = "Quantity must be positive") int quantity) {
        log.info("Request to reserve {} units for product {}", quantity, productId);
        var product = inventoryService.reserveStock(productId, quantity);
        return ResponseEntity.ok(product);
    }
}
