package com.example.trasua.repository;

import com.example.trasua.model.Topping;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ToppingRepository extends JpaRepository<Topping, Long> {
    @Query("SELECT t FROM Topping t WHERE " +
           "(:keyword IS NULL OR LOWER(t.name) LIKE LOWER(CONCAT('%',:keyword,'%'))) " +
           "AND (:status IS NULL OR t.status = :status)")
    Page<Topping> search(@Param("keyword") String keyword,
                         @Param("status") String status,
                         Pageable pageable);
}
