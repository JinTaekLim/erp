package com.erp.erp.domain.plans.common.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Plans {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

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
  public Plans(LicenseType licenseType, String name, int price, int availableTime, int availablePeriod) {
    this.licenseType = licenseType;
    this.name = name;
    this.price = price;
    this.availableTime = availableTime;
    this.availablePeriod = availablePeriod;
  }
}
