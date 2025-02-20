package com.erp.erp.domain.plan.common.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
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

  private String createdId;

  private LocalDateTime createdAt;

  private String updatedId;

  private LocalDateTime updatedAt;

  @Builder
  public Plan(PlanType planType, LicenseType licenseType, CourseType courseType, String name,
      int price, int availableTime, int availablePeriod, String createdId) {
    this.planType = planType;
    this.licenseType = licenseType;
    this.courseType = courseType;
    this.name = name;
    this.price = price;
    this.availableTime = availableTime;
    this.availablePeriod = availablePeriod;
    this.createdId = createdId;
    this.createdAt = LocalDateTime.now();
  }
}
