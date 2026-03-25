package com.example.trasua.repository;

import com.example.trasua.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductRepository extends JpaRepository<Product, Long> {
    boolean existsBySlug(String slug);

    @Query("SELECT p FROM Product p LEFT JOIN FETCH p.category WHERE " +
           "(:keyword IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%',:keyword,'%'))) " +
           "AND (:categoryId IS NULL OR p.categoryId = :categoryId) " +
           "AND (:status IS NULL OR p.status = :status)")
    Page<Product> search(@Param("keyword") String keyword,
                         @Param("categoryId") Long categoryId,
                         @Param("status") String status,
                         Pageable pageable);

    long countByStatus(String status);
}
