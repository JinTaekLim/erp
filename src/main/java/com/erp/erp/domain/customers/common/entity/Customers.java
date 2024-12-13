package com.erp.erp.domain.customers.common.entity;

import com.erp.erp.domain.institutes.common.entity.Institutes;
import com.erp.erp.domain.payments.common.entity.OtherPayments;
import com.erp.erp.domain.payments.common.entity.PlanPayment;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Customers {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @NotNull
  private Institutes institutes;

  @NotNull
  private String name;

  @NotNull
  private Gender gender;

  @NotNull
  private String phone;

  @NotNull
  private String address;

  private String photoUrl;

  private String memo;

  @NotNull
  private LocalDate birthDate;

  @NotNull
  private CustomerStatus status;

  @OneToOne(cascade = CascadeType.ALL)
  @NotNull
  private PlanPayment planPayment;

  @OneToMany(cascade = CascadeType.ALL)
  @Valid
  private List<OtherPayments> otherPayments;


  @Builder
  public Customers(Institutes institutes, String name, Gender gender, String phone, String address, String photoUrl,
                   String memo, LocalDate birthDate, PlanPayment planPayment, List<OtherPayments> otherPayments) {
    this.institutes = institutes;
    this.name = name;
    this.gender = gender;
    this.phone = phone;
    this.address = address;
    this.photoUrl = photoUrl;
    this.memo = memo;
    this.birthDate = birthDate;
    this.status = CustomerStatus.ACTIVE;
    this.planPayment = planPayment;
    this.otherPayments = otherPayments;
  }


  public Customers update(String name, Gender gender, String phone, String address, String photoUrl, String memo,
                          LocalDate birthDate, List<OtherPayments> otherPayments) {
    this.name = name;
    this.gender = gender;
    this.phone = phone;
    this.address = address;
    this.photoUrl = photoUrl;
    this.memo = memo;
    this.birthDate = birthDate;

    this.otherPayments.clear();
    if (otherPayments != null) { this.otherPayments.addAll(new ArrayList<>(otherPayments)); }

    return this;
  }

}
