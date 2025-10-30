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

/*
    Developed by Group 6:
        Ken Aliling
        Anicia Kaela Bonayao
        Carl Norbi Felonia
        Cedrick Miguel Kaneko
        Dino Alfred T. Timbol (Group Leader)
 */

//User repository class, extending JpaRepository, containing
//db methods related to the users table

@Repository
public interface UserRepository extends JpaRepository<UserModel, Long>, JpaSpecificationExecutor<UserModel> {

    Optional<UserModel> findByEmail(String email);

    Optional<UserModel> findById(long id);

    @Query("SELECT u.userId FROM UserModel u WHERE u.email = :email")
    Long getUserIdByEmail(@Param("email") String email);

    long countByRole(Role role);

    long countByCreatedAtAfter(LocalDateTime date);

    boolean existsByEmail(String email);
}