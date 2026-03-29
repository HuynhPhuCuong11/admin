package com.example.trasua.repository;

import com.example.trasua.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    long countByRoles(String roles);
    long countByStatus(String status);

    @Query("SELECT u FROM User u WHERE " +
           "(:kw IS NULL OR LOWER(u.fullName) LIKE LOWER(CONCAT('%',:kw,'%')) OR LOWER(u.email) LIKE LOWER(CONCAT('%',:kw,'%'))) " +
           "AND (:status IS NULL OR u.status = :status) " +
           "AND (:role IS NULL OR u.roles = :role)")
    Page<User> search(@Param("kw") String kw, @Param("status") String status,
                      @Param("role") String role, Pageable pageable);
}
