package com.newscheck.newscheck.repositories;

import com.newscheck.newscheck.models.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository

public interface UserRepository extends JpaRepository<UserModel, Long> {

    Optional<UserModel> findByEmail(String email);
    Optional<UserModel> findById(long id);
}
