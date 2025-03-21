package com.erp.erp.domain.institute.common.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;
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

  @Min(value = 1)
  private int totalSeat;

  private LocalTime openTime;

  private LocalTime closeTime;

  private String createdId;

  private LocalDateTime createdAt;

  private String updatedId;

  private LocalDateTime updatedAt;


  public void changeTotalSpots(int totalSeat, String updatedId) {
    this.totalSeat = totalSeat;
    this.updatedId = updatedId;
    this.updatedAt = LocalDateTime.now();
  }

  public void updateName(String name, String updatedId) {
    this.name = name;
    this.updatedId = updatedId;
  }

  @Builder
  public Institute(String name, int totalSeat, LocalTime openTime, LocalTime closeTime, String createdId) {
    this.name = name;
    this.totalSeat = totalSeat;
    this.openTime = openTime;
    this.closeTime = closeTime;
    this.createdId = createdId;
    this.createdAt = LocalDateTime.now();
  }

  @PrePersist
  @PreUpdate
  private void truncateTimes() {
    this.openTime = truncateToMinutes(this.openTime);
    this.closeTime = truncateToMinutes(this.closeTime);
  }

  private LocalTime truncateToMinutes(LocalTime dateTime) {
    return dateTime != null ? dateTime.withSecond(0).withNano(0) : null;
  }
}
