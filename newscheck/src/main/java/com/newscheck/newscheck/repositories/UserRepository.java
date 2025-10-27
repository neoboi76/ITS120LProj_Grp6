package com.newscheck.newscheck.repositories;

import com.newscheck.newscheck.models.UserModel;
import com.newscheck.newscheck.models.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserModel, Long>, JpaSpecificationExecutor<UserModel> {

    Optional<UserModel> findByEmail(String email);

    Optional<UserModel> findById(long id);

    @Query("SELECT u.userId FROM UserModel u WHERE u.email = :email")
    Long getUserIdByEmail(@Param("email") String email);

    // New methods for admin operations
    long countByRole(Role role);

    long countByCreatedAtAfter(LocalDateTime date);
}