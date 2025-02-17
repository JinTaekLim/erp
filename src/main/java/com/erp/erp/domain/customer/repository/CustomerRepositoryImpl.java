package com.erp.erp.domain.customer.repository;

import com.erp.erp.domain.customer.common.entity.Customer;
import com.erp.erp.domain.customer.common.entity.CustomerStatus;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import com.erp.erp.domain.customer.common.entity.QCustomer;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Repository
public class CustomerRepositoryImpl implements CustomerRepositoryCustom {

  private final JPAQueryFactory queryFactory;


  @Override
  public List<Customer> findByInstituteIdAndNameStartingWithAndStatusIn(Long instituteId,
      String name, List<CustomerStatus> statuses) {
    QCustomer customer = QCustomer.customer;

    return queryFactory
        .selectFrom(customer)
        .where(
            customer.institute.id.eq(instituteId),
            customer.name.startsWith(name),
            customer.status.in(statuses)
        )
        .fetch();
  }


  @Override
  @Transactional
  public void updateStatusById(@Param("customerId") Long customerId,
      @Param("newStatus") CustomerStatus newStatus) {
    QCustomer customer = QCustomer.customer;

    queryFactory.update(customer)
        .set(customer.status, newStatus)
        .where(customer.id.eq(customerId))
        .execute();
  }

  @Override
  public Optional<Long> findTopIdByInstituteId(Long instituteId) {
    QCustomer qCustomer = QCustomer.customer;

    return Optional.ofNullable(
        queryFactory
        .select(qCustomer.id)
        .from(qCustomer)
        .where(qCustomer.institute.id.eq(instituteId))
        .orderBy(qCustomer.id.desc())
        .fetchFirst()
    );
  }


  @Override
  public List<Customer> findAllByInstituteBeforeIdAndStatus(
      Long instituteId, Long lastId, CustomerStatus status, int size
  ) {
    QCustomer qCustomer = QCustomer.customer;

    return queryFactory
        .selectFrom(qCustomer)
        .where(qCustomer.id.lt(lastId))
        .where(qCustomer.institute.id.eq(instituteId))
        .where(qCustomer.status.eq(status))
        .orderBy(qCustomer.id.desc())
        .limit(size)
        .fetch();
  }
}
