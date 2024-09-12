package com.hanul.pis.authentication.infra.repo;

import com.hanul.pis.authentication.infra.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    UserEntity findByEmail(String email);
    UserEntity findByUserId(String userId);

    @Query("SELECT u.email FROM users u")
    List<String> getAllEmails();
}
