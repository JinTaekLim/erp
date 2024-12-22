package com.erp.erp.domain.plan.common.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "plans")
public class Plan {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotNull
  private PlanType planType;

  @NotNull
  private LicenseType licenseType;

  @NotNull
  private String name;

  @NotNull
  private int price;

  private int availableTime;

  @NotNull
  private int availablePeriod;

  @Builder
  public Plan(PlanType planType, LicenseType licenseType, String name, int price, int availableTime,
      int availablePeriod) {
    this.planType = planType;
    this.licenseType = licenseType;
    this.name = name;
    this.price = price;
    this.availableTime = availableTime;
    this.availablePeriod = availablePeriod;
  }
}
