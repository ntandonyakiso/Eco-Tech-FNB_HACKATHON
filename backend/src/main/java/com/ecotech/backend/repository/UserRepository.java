package com.ecotech.backend.repository;

import com.ecotech.backend.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByEmail(String email);

    Optional<User> findByPhone(String phone);

    @Query("SELECT u FROM User u WHERE u.wardId = :wardId")
    java.util.List<User> findByWardId(@Param("wardId") UUID wardId);

    boolean existsByEmail(String email);

    boolean existsByPhone(String phone);
}
