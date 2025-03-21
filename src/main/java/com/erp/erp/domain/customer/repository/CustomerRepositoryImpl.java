package com.erp.erp.domain.customer.repository;

import com.erp.erp.domain.customer.common.dto.GetCustomerDto;
import com.erp.erp.domain.customer.common.dto.GetCustomerDto.ReservationDto;
import com.erp.erp.domain.customer.common.dto.UpdateCustomerExpiredAtDto;
import com.erp.erp.domain.customer.common.entity.Customer;
import com.erp.erp.domain.customer.common.entity.CustomerStatus;
import com.erp.erp.domain.payment.common.entity.OtherPayment;
import com.erp.erp.domain.payment.common.entity.PlanPayment;
import com.erp.erp.domain.payment.common.entity.QOtherPayment;
import com.erp.erp.domain.payment.common.entity.QPlanPayment;
import com.erp.erp.domain.plan.common.entity.Plan;
import com.erp.erp.domain.plan.common.entity.QPlan;
import com.erp.erp.domain.reservation.common.dto.ReservationCache;
import com.erp.erp.domain.reservation.common.entity.AttendanceStatus;
import com.erp.erp.domain.reservation.common.entity.QReservation;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
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
  public List<GetCustomerDto.Response> findAllByInstituteBeforeIdAndStatus(
      Long instituteId, Long lastId, CustomerStatus status, int size) {

    QCustomer qCustomer = QCustomer.customer;
    QReservation qReservation = QReservation.reservation;
    QPlanPayment qPlanPayment = QPlanPayment.planPayment;
    QOtherPayment qOtherPayment = QOtherPayment.otherPayment;
    QPlan qPlan = QPlan.plan;

    return queryFactory
        .select(qCustomer)
        .from(qCustomer)
        .join(qCustomer.planPayment, qPlanPayment).fetchJoin()
        .leftJoin(qCustomer.otherPayments, QOtherPayment.otherPayment).fetchJoin()
        .join(qPlanPayment.plan, qPlan).fetchJoin()
        .where(qCustomer.id.lt(lastId))
        .where(qCustomer.institute.id.eq(instituteId))
        .where(qCustomer.status.eq(status))
        .orderBy(qCustomer.id.desc())
        .limit(size)
        .fetch()
        .stream()
        .map(c -> {
          PlanPayment planPayment = c.getPlanPayment();
          Plan plan = planPayment.getPlan();

          List<ReservationDto> reservationDtoList = queryFactory
              .select(Projections.fields(
                  ReservationDto.class,
                  qReservation.customer,
                  qReservation.reservationDate,
                  qReservation.startIndex,
                  qReservation.endIndex,
                  qReservation.attendanceStatus
              ))
              .from(qReservation)
              .where(qReservation.customer.eq(c))
              .fetch();

          double usedTime = getUsedTime(reservationDtoList);
          double remainingTime = plan.getAvailableTime() - usedTime;


          return GetCustomerDto.Response.builder()
              .customerId(c.getId())
              .status(c.getStatus())
              .photoUrl(c.getPhotoUrl())
              .name(c.getName())
              .gender(c.getGender())
              .phone(c.getPhone())
              .licenseType(plan.getLicenseType())
              .planName(plan.getName())
              .planType(plan.getPlanType())
              .courseType(plan.getCourseType())
              .remainingTime(remainingTime)
              .remainingPeriod(getRemainingPeriod(c))
              .usedTime(usedTime)
              .registrationDate(planPayment.getRegistrationAt())
              .lateCount(getAttendanceStatusCount(reservationDtoList, AttendanceStatus.LATE))
              .absenceCount(getAttendanceStatusCount(reservationDtoList, AttendanceStatus.ABSENT))
              .otherPaymentPrice(getOtherPaymentPrice(c.getOtherPayments()))
              .build();
        })
        .toList();
  }

  private double getUsedTime(List<ReservationDto> reservationDtoList) {
    return reservationDtoList.stream().mapToDouble(r -> {
      return (double) (r.getEndIndex() - r.getStartIndex()) / 2;
      }).sum();
  }

  private int getAttendanceStatusCount(
      List<ReservationDto> reservationDtoList, AttendanceStatus attendanceStatus
  ) {
    return (int) reservationDtoList.stream()
        .filter(reservation -> reservation.getAttendanceStatus().equals(
            attendanceStatus)).count();
  }



  private int getRemainingPeriod(Customer customer) {
    long remainingPeriod = ( customer.getExpiredAt() != null ) ?
        ChronoUnit.DAYS.between(customer.getExpiredAt(), LocalDateTime.now()) :
        ChronoUnit.DAYS.between(customer.getPlanPayment().getRegistrationAt(), LocalDateTime.now());
    return (int) remainingPeriod;
  }

  private int getOtherPaymentPrice(List<OtherPayment> otherPayments) {
    return otherPayments.stream()
        .mapToInt(OtherPayment::getPrice)
        .sum();
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
                JPAExpressions.select(qReservation.reservationDate.min())
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

  @Override
  public void updatePhotoUrl(Customer customer) {
    QCustomer qCustomer = QCustomer.customer;

    queryFactory.update(qCustomer)
        .set(qCustomer.photoUrl, customer.getPhotoUrl())
        .set(qCustomer.updatedId, customer.getUpdatedId())
        .set(qCustomer.updatedAt, customer.getUpdatedAt())
        .where(qCustomer.id.eq(customer.getId()))
        .execute();

  }

  @Override
  public List<GetCustomerDto.Response> findByReservationCache(List<ReservationCache> reservationCaches) {
    QCustomer qCustomer = QCustomer.customer;

    List<Long> customerIds = reservationCaches.stream()
        .map(ReservationCache::getCustomerId)
        .distinct()
        .toList();

    List<Customer> customers = queryFactory
        .select(qCustomer)
        .from(qCustomer)
        .where(qCustomer.id.in(customerIds))
        .orderBy(qCustomer.id.desc())
        .fetch();

    Map<Long, ReservationCache> reservationCacheMap = reservationCaches.stream()
        .collect(Collectors.toMap(ReservationCache::getCustomerId, cache -> cache));

    return customers.stream()
        .map(customer -> {
          ReservationCache reservationCache = reservationCacheMap.get(customer.getId());

          List<OtherPayment> otherPayment = customer.getOtherPayments();
          PlanPayment planPayment = customer.getPlanPayment();
          Plan plan = planPayment.getPlan();

          double remainingTime = plan.getAvailableTime() - reservationCache.getUsedTime();

          return GetCustomerDto.Response.builder()
              .customerId(customer.getId())
              .status(customer.getStatus())
              .photoUrl(customer.getPhotoUrl())
              .name(customer.getName())
              .gender(customer.getGender())
              .phone(customer.getPhone())
              .licenseType(plan.getLicenseType())
              .planName(plan.getName())
              .planType(plan.getPlanType())
              .courseType(plan.getCourseType())
              .remainingTime(remainingTime)
              .remainingPeriod(getRemainingPeriod(customer))
              .usedTime(reservationCache.getUsedTime())
              .registrationDate(planPayment.getRegistrationAt())
              .lateCount(reservationCache.getLateCount())
              .absenceCount(reservationCache.getAbsenceCount())
              .otherPaymentPrice(getOtherPaymentPrice(otherPayment))
              .build();
        })
        .toList();
  }

}
