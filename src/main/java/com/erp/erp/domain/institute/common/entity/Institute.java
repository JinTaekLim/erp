package com.erp.erp.domain.institute.common.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "institutes")
public class Institute {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotBlank
  private String name;

  @NotNull
  private int totalSeat;

  private LocalTime openTime;

  private LocalTime closeTime;



  public void changeTotalSpots(int totalSeat) {
    this.totalSeat = totalSeat;
  }

  @Builder
  public Institute(String name, int totalSeat, LocalTime openTime, LocalTime closeTime) {
    this.name = name;
    this.totalSeat = totalSeat;
    this.openTime = openTime;
    this.closeTime = closeTime;
  }
}
