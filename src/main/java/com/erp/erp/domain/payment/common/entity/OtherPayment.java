package com.erp.erp.domain.payment.common.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "otherPayment")
public class OtherPayment {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotNull
  private boolean status;

  @NotNull
  private PaymentsMethod paymentsMethod;

  @NotNull
  private LocalDateTime registrationAt;

  @NotNull
  private String content;

  @NotNull
  private int price;

  @Builder
  public OtherPayment(boolean status, LocalDateTime registrationAt, PaymentsMethod paymentsMethod,
      String content, int price) {
    this.status = status;
    this.registrationAt = registrationAt;
    this.paymentsMethod = paymentsMethod;
    this.content = content;
    this.price = price;
  }
}
