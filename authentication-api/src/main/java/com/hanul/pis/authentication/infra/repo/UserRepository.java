package com.hanul.pis.authentication.infra.repo;

import com.hanul.pis.authentication.infra.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    UserEntity findByEmail(String email);
    UserEntity findByUserId(String userId);
    UserEntity findByEmailVerificationToken(String token);
    UserEntity findByPasswordResetToken(String token);

    @Query(value="select * from users u where u.email_verification_status=true", nativeQuery = true)
    Page<UserEntity> findAllUsersWithConfirmedEmailAddress(Pageable pageableRequest);

    @Query("SELECT u.email FROM users u")
    List<String> getAllEmails();

    @Query(value = "select * from users u where u.first_name = ?1 and u.last_name= ?2", nativeQuery = true)
    List<UserEntity> findUserByName(String firstName, String lastName);

    @Query(value = "select * from users u where u.first_name like %?1% or u.last_name like %?1%", nativeQuery = true)
    List<UserEntity> findUserByKeyword(String keyword);

    @Query(value = "select u.first_name, u.last_name from users u where u.first_name like %?1% or u.last_name like %?1%", nativeQuery = true)
    List<Object[]> findUserByKeywordLessInfo(String keyword);

//    @Transactional
//    @Modifying
//    @Query(value = "update users u set u.email_verification_status=?1 where u.user_id=?2")
//    void updateUserEmailVerificationStatus(boolean emailVerificationStatus, String userId);

    // JPQL
//    @Query("select user from UserEntity user where user.userId = :userId")
//    UserEntity findUserEntityByUserId(@Param("userId") String userId);
}
