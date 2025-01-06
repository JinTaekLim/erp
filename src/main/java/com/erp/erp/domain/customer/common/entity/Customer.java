package com.erp.erp.domain.customer.common.entity;

import com.erp.erp.domain.institute.common.entity.Institute;
import com.erp.erp.domain.payment.common.entity.OtherPayment;
import com.erp.erp.domain.payment.common.entity.PlanPayment;
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
@Table(name = "customers")
public class Customer {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @NotNull
  private Institute institute;

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
  private List<OtherPayment> otherPayments;


  @Builder
  public Customer(Institute institute, String name, Gender gender, String phone, String address, String photoUrl,
                   String memo, LocalDate birthDate, PlanPayment planPayment, List<OtherPayment> otherPayments) {
    this.institute = institute;
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


  public Customer update(String name, Gender gender, String phone, String address, String photoUrl, String memo,
                          LocalDate birthDate, boolean planPaymentStatus, List<OtherPayment> otherPayments) {
    this.name = name;
    this.gender = gender;
    this.phone = phone;
    this.address = address;
    this.photoUrl = photoUrl;
    this.memo = memo;
    this.birthDate = birthDate;
    this.planPayment.updateStatus(planPaymentStatus);

    this.otherPayments.clear();
    if (otherPayments != null) { this.otherPayments.addAll(new ArrayList<>(otherPayments)); }

    return this;
  }

}
