package com.danutradu.storemanagementapi.controller;

import com.danutradu.storemanagementapi.dto.ProductDto;
import com.danutradu.storemanagementapi.entity.Product;
import com.danutradu.storemanagementapi.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/products")
@Tag(name = "Products", description = "Product management operations")
@SecurityRequirement(name = "basicAuth")
@Validated
public class ProductController {

    private final ProductService productService;

    @Operation(summary = "Get products with pagination", description = "Retrieve products with pagination, sorting and optional name search")
    @GetMapping
    public ResponseEntity<Page<Product>> findProducts(@RequestParam(required = false) String name,
                                                      @RequestParam(defaultValue = "0") @Min(value = 0, message = "Page must be non-negative") int pageNum,
                                                      @RequestParam(defaultValue = "10") @Min(value = 1, message = "Size must be positive") int pageSize,
                                                      @RequestParam(defaultValue = "id") String sortBy) {
        log.info("Request to find products with name: {}, page: {}, size: {}", name, pageNum, pageSize);
        var productPage = productService.findProducts(name, pageNum, pageSize, sortBy);
        return ResponseEntity.ok(productPage);
    }

    @Operation(summary = "Get products by ID", description = "Retrieve a specific product by its ID")
    @GetMapping("/{id}")
    public ResponseEntity<Product> findProduct(@PathVariable Long id) {
        log.info("Request to find product with id: {}", id);
        var product = productService.findProductById(id);
        return ResponseEntity.ok(product);
    }

    @Operation(summary = "Add new product", description = "Create a new product (Admin only)")
    @PostMapping
    public ResponseEntity<Product> addProduct(@Valid @RequestBody ProductDto productDto) {
        log.info("Received request to add product: {}", productDto.getName());
        var product = productService.addProduct(productDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(product);
    }

    @Operation(summary = "Update product fields", description = "Partially update any product fields (Admin only)")
    @PatchMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody ProductDto updates) {
        log.info("Request to update product with id: {}", id);
        var product = productService.updateProduct(id, updates);
        return ResponseEntity.ok(product);
    }

    @Operation(summary = "Delete product", description = "Remove a product from the store (Admin only)")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        log.info("Request to delete product with id: {}", id);
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}
