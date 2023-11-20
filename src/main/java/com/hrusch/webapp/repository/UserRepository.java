package com.hrusch.webapp.repository;

import com.hrusch.webapp.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    @Query(value = "SELECT * FROM users WHERE username = ?", nativeQuery = true)
    Optional<UserEntity> findByUsername(String username);

    Optional<UserEntity> findByUserId(String userId);
}
