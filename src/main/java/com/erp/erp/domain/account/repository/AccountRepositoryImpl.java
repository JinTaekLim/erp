package com.erp.erp.domain.account.repository;

import com.erp.erp.domain.account.common.entity.QAccount;
import com.erp.erp.domain.institute.common.entity.Institute;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class AccountRepositoryImpl implements AccountRepositoryCustom{

  private final JPAQueryFactory queryFactory;

  @Override
  public Optional<Institute> findInstitutesByAccountId(@Param("accountId") Long accountId) {
    QAccount account = QAccount.account;

    return Optional.ofNullable(
        queryFactory
            .select(account.institute)
            .from(account)
            .where(account.id.eq(accountId))
            .fetchOne()
    );
  }
}
