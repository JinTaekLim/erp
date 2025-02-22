package com.erp.erp.domain.customer.repository;

import com.erp.erp.domain.customer.common.dto.UpdateCustomerExpiredAtDto;
import com.erp.erp.domain.customer.common.entity.Customer;
import com.erp.erp.domain.customer.common.entity.CustomerStatus;
import com.erp.erp.domain.payment.common.entity.QPlanPayment;
import com.erp.erp.domain.plan.common.entity.QPlan;
import com.erp.erp.domain.reservation.common.entity.QReservation;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
      @Param("newStatus") CustomerStatus newStatus, String updatedId) {
    QCustomer customer = QCustomer.customer;

    queryFactory.update(customer)
        .set(customer.status, newStatus)
        .set(customer.updatedId, updatedId)
        .set(customer.updatedAt, LocalDateTime.now())
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

  @Override
  public List<UpdateCustomerExpiredAtDto> findCustomersCreatedAtOnDaysAgo(int days) {
    QCustomer qCustomer = QCustomer.customer;
    QReservation qReservation = QReservation.reservation;
    QPlanPayment qPlanPayment = QPlanPayment.planPayment;
    QPlan qPlan = QPlan.plan;

    LocalDate dateBefore = LocalDateTime.now().minusDays(days).toLocalDate();

    LocalDateTime startOfDay = dateBefore.atStartOfDay();
    LocalDateTime endOfDay = dateBefore.atTime(23, 59, 59, 999999999);

    return queryFactory
        .select(
            Projections.constructor(UpdateCustomerExpiredAtDto.class,
                qCustomer.id,
                JPAExpressions.select(qReservation.startTime.min())
                    .from(qReservation)
                    .where(qReservation.customer.id.eq(qCustomer.id)),
                qPlan.availablePeriod
            )
        )
        .from(qCustomer)
        .leftJoin(qCustomer.planPayment, qPlanPayment)
        .leftJoin(qPlanPayment.plan, qPlan)
        .where(qCustomer.createdAt.goe(startOfDay)
            .and(qCustomer.createdAt.loe(endOfDay)))
        .fetch();
  }


  public void updateExpiredAt(List<UpdateCustomerExpiredAtDto.Request> requests) {
    QCustomer qCustomer = QCustomer.customer;

    for (UpdateCustomerExpiredAtDto.Request request : requests) {
      queryFactory.update(qCustomer)
          .set(qCustomer.updatedAt, LocalDateTime.now())
          .set(qCustomer.updatedId, "SERVER")
          .set(qCustomer.expiredAt, request.getExpiredAt())
          .where(qCustomer.id.eq(request.getCustomerId()))
          .execute();
    }
  }

  @Override
  public List<Long> findIdsCreatedAtBeforeDaysAgo(LocalDate date) {
    QCustomer qCustomer = QCustomer.customer;

    return queryFactory
        .select(qCustomer.id)
        .from(qCustomer)
        .where(qCustomer.status.eq(CustomerStatus.ACTIVE))
        .where(qCustomer.expiredAt.lt(date))
        .fetch();

  }
}
