package com.erp.erp.domain.customer.repository;

import com.erp.erp.domain.customer.common.entity.Customer;
import com.erp.erp.domain.customer.common.entity.CustomerStatus;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
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

}
