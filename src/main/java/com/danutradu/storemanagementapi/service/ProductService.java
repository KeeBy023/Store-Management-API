package com.danutradu.storemanagementapi.service;

import com.danutradu.storemanagementapi.dto.ProductDto;
import com.danutradu.storemanagementapi.entity.Product;
import com.danutradu.storemanagementapi.exception.ProductNotFoundException;
import com.danutradu.storemanagementapi.mapper.ProductMapper;
import com.danutradu.storemanagementapi.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public Product findProductById(Long id) {
        log.info("Searching product by id: {}", id);
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + id));
    }

    public Page<Product> findProducts(String name, int pageNum, int pageSize, String sortBy) {
        log.info("Searching products with name: {}, page: {}, size {}", name, pageNum, pageSize);
        var pageable = PageRequest.of(pageNum, pageSize, Sort.by(sortBy));
        if (name != null && !name.isBlank()) {
            return productRepository.findByNameContainingIgnoreCase(name, pageable);
        }
        return productRepository.findAll(pageable);
    }

    public Product addProduct(ProductDto productDto) {
        log.info("Adding new product: {}", productDto.getName());
        var product = productMapper.toEntity(productDto);
        return productRepository.save(product);
    }

    public Product updateProduct(Long id, ProductDto updates) {
        log.info("Updating product with id: {}", id);
        var product = findProductById(id);
        productMapper.updateEntityFromDto(updates, product);
        return productRepository.save(product);
    }

    public void deleteProduct(Long id) {
        log.info("Deleting product with id: {}", id);
        if (!productRepository.existsById(id)) {
            throw new ProductNotFoundException("Product not found with id: " + id);
        }
        productRepository.deleteById(id);
    }
}
