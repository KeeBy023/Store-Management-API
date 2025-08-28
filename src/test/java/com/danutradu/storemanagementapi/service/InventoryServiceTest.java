package com.danutradu.storemanagementapi.service;

import com.danutradu.storemanagementapi.entity.Product;
import com.danutradu.storemanagementapi.exception.InsufficientStockException;
import com.danutradu.storemanagementapi.exception.ProductNotFoundException;
import com.danutradu.storemanagementapi.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InventoryServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private InventoryService inventoryService;

    private Product testProduct;

    @BeforeEach
    void setUp() {
        testProduct = new Product(1L, "Test Product", "Test Description", new BigDecimal("10.99"), 50);
    }

    @Test
    void findLowStockProducts() {
        when(productRepository.findByQuantityLessThan(10)).thenReturn(Arrays.asList(testProduct));

        var result = inventoryService.findLowStockProducts(10);

        assertEquals(1, result.size());
        assertEquals(testProduct, result.get(0));
        verify(productRepository).findByQuantityLessThan(10);
    }

    @Test
    void findOutOfStockProducts() {
        var outOfStockProduct = new Product(2L, "Out of Stock", "Description", new BigDecimal("5.99"), 0);
        when(productRepository.findByQuantityEquals(0)).thenReturn(Arrays.asList(outOfStockProduct));

        var result = inventoryService.findOutOfStockProducts();

        assertEquals(1, result.size());
        assertEquals(0, result.get(0).getQuantity());
        verify(productRepository).findByQuantityEquals(0);
    }

    @Test
    void adjustStockSuccessfully() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));
        when(productRepository.save(any(Product.class))).thenReturn(testProduct);

        var result = inventoryService.adjustStock(1L, 10, "Restock");

        assertEquals(60, result.getQuantity());
        verify(productRepository).save(testProduct);
    }

    @Test
    void adjustStockProductNotFound() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class,
                () -> inventoryService.adjustStock(1L, 10, "Restock"));
    }

    @Test
    void adjustStockInsufficientStock() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));

        assertThrows(InsufficientStockException.class,
                () -> inventoryService.adjustStock(1L, -60, "Sale"));
    }

    @Test
    void restockProduct() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));
        when(productRepository.save(any(Product.class))).thenReturn(testProduct);

        var result = inventoryService.restockProduct(1L, 20);

        assertEquals(70, result.getQuantity());
        verify(productRepository).save(testProduct);
    }

    @Test
    void reserveStock() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));
        when(productRepository.save(any(Product.class))).thenReturn(testProduct);

        var result = inventoryService.reserveStock(1L, 10);

        assertEquals(40, result.getQuantity());
        verify(productRepository).save(testProduct);
    }
}