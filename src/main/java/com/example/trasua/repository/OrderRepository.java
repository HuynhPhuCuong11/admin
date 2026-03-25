package com.example.trasua.repository;

import com.example.trasua.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("SELECT o FROM Order o WHERE " +
           "(:keyword IS NULL OR LOWER(o.orderNo) LIKE LOWER(CONCAT('%',:keyword,'%')) " +
           "OR LOWER(o.customerName) LIKE LOWER(CONCAT('%',:keyword,'%')) " +
           "OR LOWER(o.customerPhone) LIKE LOWER(CONCAT('%',:keyword,'%'))) " +
           "AND (:orderStatus IS NULL OR o.orderStatus = :orderStatus) " +
           "AND (:payStatus IS NULL OR o.payStatus = :payStatus)")
    Page<Order> search(@Param("keyword") String keyword,
                       @Param("orderStatus") String orderStatus,
                       @Param("payStatus") String payStatus,
                       Pageable pageable);

    long countByOrderStatus(String orderStatus);

    @Query("SELECT COALESCE(SUM(o.totalAmount), 0) FROM Order o WHERE o.payStatus = 'paid'")
    BigDecimal sumRevenue();

    @Query("SELECT COALESCE(SUM(o.totalAmount), 0) FROM Order o WHERE o.payStatus = 'paid' AND o.createdAt >= :from")
    BigDecimal sumRevenueFrom(@Param("from") LocalDateTime from);

    @Query("SELECT COUNT(o) FROM Order o WHERE o.createdAt >= :from")
    long countOrdersFrom(@Param("from") LocalDateTime from);
}
