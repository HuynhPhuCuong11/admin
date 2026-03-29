package com.example.trasua.repository;

import com.example.trasua.model.Coupon;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CouponRepository extends JpaRepository<Coupon, Long> {
    boolean existsByCode(String code);
    boolean existsByCodeAndIdNot(String code, Long id);

    @Query("SELECT c FROM Coupon c WHERE " +
           "(:kw IS NULL OR LOWER(c.code) LIKE LOWER(CONCAT('%',:kw,'%'))) " +
           "AND (:active IS NULL OR c.active = :active)")
    Page<Coupon> search(@Param("kw") String kw, @Param("active") Boolean active, Pageable pageable);
}
