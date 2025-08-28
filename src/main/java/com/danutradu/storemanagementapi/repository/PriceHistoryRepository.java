package com.danutradu.storemanagementapi.repository;

import com.danutradu.storemanagementapi.entity.PriceHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PriceHistoryRepository extends JpaRepository<PriceHistory, Long> {
    List<PriceHistory> findByProductIdOrderByChangedAtDesc(Long productId);
}
