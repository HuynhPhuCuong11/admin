package com.example.trasua.repository;

import com.example.trasua.model.Coupon;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CouponRepository extends JpaRepository<Coupon, Long> {

    Optional<Coupon> findByCode(String code);

    boolean existsByCode(String code);

    @Query("SELECT c FROM Coupon c WHERE " +
           "(:keyword IS NULL OR LOWER(c.code) LIKE LOWER(CONCAT('%',:keyword,'%')) " +
           "OR LOWER(c.description) LIKE LOWER(CONCAT('%',:keyword,'%'))) " +
           "AND (:status IS NULL OR c.status = :status)")
    Page<Coupon> search(@Param("keyword") String keyword,
                        @Param("status") String status,
                        Pageable pageable);

    long countByStatus(String status);
}
