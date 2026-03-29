package com.example.trasua.repository;

import com.example.trasua.model.ProductVariant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface ProductVariantRepository extends JpaRepository<ProductVariant, Long> {
    @Query("SELECT v FROM ProductVariant v JOIN FETCH v.size WHERE v.productId = :productId")
    List<ProductVariant> findByProductIdWithSize(@Param("productId") Long productId);
    void deleteByProductId(Long productId);
}
