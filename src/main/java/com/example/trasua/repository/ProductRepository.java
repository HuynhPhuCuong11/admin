package com.example.trasua.repository;

import com.example.trasua.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductRepository extends JpaRepository<Product, Long> {
    boolean existsBySlug(String slug);
    long countByStatus(String status);

    @Query("SELECT p FROM Product p LEFT JOIN FETCH p.category WHERE " +
           "(:kw IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%',:kw,'%'))) " +
           "AND (:categoryId IS NULL OR p.categoryId = :categoryId) " +
           "AND (:status IS NULL OR p.status = :status)")
    Page<Product> search(@Param("kw") String kw, @Param("categoryId") Long categoryId,
                         @Param("status") String status, Pageable pageable);
}
