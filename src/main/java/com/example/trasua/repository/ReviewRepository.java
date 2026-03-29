package com.example.trasua.repository;

import com.example.trasua.model.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    @Query("SELECT r FROM Review r LEFT JOIN FETCH r.product LEFT JOIN FETCH r.user WHERE " +
           "(:kw IS NULL OR LOWER(r.body) LIKE LOWER(CONCAT('%',:kw,'%')) " +
           "OR LOWER(r.product.name) LIKE LOWER(CONCAT('%',:kw,'%'))) " +
           "AND (:status IS NULL OR r.status = :status) " +
           "AND (:rating IS NULL OR r.rating = :rating)")
    Page<Review> search(@Param("kw") String kw, @Param("status") String status,
                        @Param("rating") Integer rating, Pageable pageable);

    long countByStatus(String status);
}
