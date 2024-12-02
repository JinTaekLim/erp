package com.erp.erp.domain.payments.common.entity;

import com.erp.erp.domain.customers.common.entity.Customers;
import com.erp.erp.domain.plans.common.entity.Plans;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Payments {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @NotNull
  private Plans plans;

  @ManyToOne
  @NotNull
  private Customers customers;

  @NotNull
  private boolean status;

  @NotNull
  private LocalDateTime registrationAt;

  @NotNull
  private PaymentsMethod paymentsMethod;

  private int discount;

  @Builder
  public Payments(Plans plans, Customers customers, boolean status,
      LocalDateTime registrationAt, PaymentsMethod paymentsMethod, int discount) {
    this.plans = plans;
    this.customers = customers;
    this.status = status;
    this.registrationAt = registrationAt;
    this.paymentsMethod = paymentsMethod;
    this.discount = discount;
  }
}
