package com.erp.erp.domain.payments.common.entity;

import com.erp.erp.domain.plan.common.entity.Plan;
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
public class PlanPayment {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @NotNull
  private Plan plan;

  @NotNull
  private boolean status;

  @NotNull
  private LocalDateTime registrationAt;

  @NotNull
  private PaymentsMethod paymentsMethod;

  private int discount;

  @Builder
  public PlanPayment(boolean status, Plan plan,
                     LocalDateTime registrationAt, PaymentsMethod paymentsMethod, int discount) {
    this.status = status;
    this.plan = plan;
    this.registrationAt = registrationAt;
    this.paymentsMethod = paymentsMethod;
    this.discount = discount;
  }
}
