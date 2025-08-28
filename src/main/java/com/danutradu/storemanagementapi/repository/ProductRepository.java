package com.danutradu.storemanagementapi.repository;

import com.danutradu.storemanagementapi.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Page<Product> findByNameContainingIgnoreCase(String name, Pageable pageable);
    List<Product> findByQuantityLessThan(Integer threshold);
    List<Product> findByQuantityEquals(Integer quantity);
}
