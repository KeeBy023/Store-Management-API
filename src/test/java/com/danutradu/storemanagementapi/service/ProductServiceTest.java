package com.danutradu.storemanagementapi.service;

import com.danutradu.storemanagementapi.dto.ProductDto;
import com.danutradu.storemanagementapi.entity.Product;
import com.danutradu.storemanagementapi.exception.ProductNotFoundException;
import com.danutradu.storemanagementapi.mapper.ProductMapper;
import com.danutradu.storemanagementapi.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private ProductService productService;

    private Product testProduct;
    private ProductDto testProductDto;

    @BeforeEach
    void setUp() {
        testProduct = new Product(1L, "Test Product", "Test Description", new BigDecimal("10.99"), 100);
        testProductDto = new ProductDto();
        testProductDto.setName(testProduct.getName());
        testProductDto.setDescription(testProduct.getDescription());
        testProductDto.setPrice(testProduct.getPrice());
        testProductDto.setQuantity(testProduct.getQuantity());
    }

    @Test
    void addProductSuccessfully() {
        when(productMapper.toEntity(testProductDto)).thenReturn(testProduct);
        when(productRepository.save(any(Product.class))).thenReturn(testProduct);

        var result = productService.addProduct(testProductDto);

        assertNotNull(result);
        assertEquals("Test Product", result.getName());
        assertEquals(new BigDecimal("10.99"), result.getPrice());
        verify(productRepository).save(any(Product.class));
    }

    @Test
    void findProductByIdSuccessfully() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));

        var result = productService.findProductById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Test Product", result.getName());
    }

    @Test
    void findProductByIdNotFound() {
        when(productRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> productService.findProductById(1L));
    }

    @Test
    void findProductsWithNameFiltered() {
        when(productRepository.findByNameContainingIgnoreCase(eq("Test"), any(Pageable.class)))
                .thenReturn(new PageImpl<>(Arrays.asList(testProduct)));

        var result = productService.findProducts("Test", 0, 10, "id");

        assertEquals(1, result.getContent().size());
        assertEquals("Test Product", result.getContent().get(0).getName());
    }

    @Test
    void updateProductSuccessfully() {
        var updates = new ProductDto();
        updates.setPrice(new BigDecimal("15.99"));
        updates.setQuantity(50);

        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));
        doAnswer(invocation -> {
            Product product = invocation.getArgument(1);
            product.setPrice(new BigDecimal("15.99"));
            product.setQuantity(50);
            return null;
        }).when(productMapper).updateEntityFromDto(any(ProductDto.class), any(Product.class));
        when(productRepository.save(any(Product.class))).thenReturn(testProduct);

        var result = productService.updateProduct(1L, updates);

        assertEquals(new BigDecimal("15.99"), result.getPrice());
        assertEquals(50, result.getQuantity());
        verify(productRepository).save(testProduct);
    }

    @Test
    void deleteProductSuccessfully() {
        when(productRepository.existsById(1L)).thenReturn(true);

        productService.deleteProduct(1L);

        verify(productRepository).deleteById(1L);
    }

    @Test
    void deleteProductNotFound() {
        when(productRepository.existsById(1L)).thenReturn(false);

        assertThrows(ProductNotFoundException.class, () -> productService.deleteProduct(1L));
        verify(productRepository, never()).deleteById(1L);
    }
}