package com.erp.erp.domain.auth.repository;

import com.erp.erp.domain.auth.common.entity.Token;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenRepository extends JpaRepository<Token, Long> {
  Optional<Token> findByRefreshToken(String refreshToken);
}
