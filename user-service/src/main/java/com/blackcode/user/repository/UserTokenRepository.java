package com.blackcode.user.repository;

import com.blackcode.user.model.UserToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserTokenRepository extends JpaRepository<UserToken, Long> {
    Optional<UserToken> findByToken(String token);

    @Query(value = "SELECT * FROM tb_token_user t WHERE t.user_id = :userId", nativeQuery = true)
    Optional<UserToken> findByUserIdNative(@Param("userId") Long userId);

    @Query(value = "SELECT * FROM tb_token_user t WHERE t.user_id = :userId AND t.token = :token", nativeQuery = true)
    Optional<UserToken> findByUserIdAndTokenNative(@Param("userId") Long userId, @Param("token") String token);

    void deleteByToken(String token);
}
