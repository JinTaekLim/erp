package com.erp.erp.domain.plan.common.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
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
  private CourseType courseType;

  @NotNull
  private String name;

  @NotNull
  private int price;

  @PositiveOrZero
  private int availableTime;

  @NotNull
  @PositiveOrZero
  private int availablePeriod;

  @Builder
  public Plan(PlanType planType, LicenseType licenseType, CourseType courseType, String name,
      int price, int availableTime, int availablePeriod) {
    this.planType = planType;
    this.licenseType = licenseType;
    this.courseType = courseType;
    this.name = name;
    this.price = price;
    this.availableTime = availableTime;
    this.availablePeriod = availablePeriod;
  }
}
