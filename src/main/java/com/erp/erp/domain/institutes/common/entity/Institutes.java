package com.erp.erp.domain.institutes.common.entity;

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
public class Institutes {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotNull
  private String name;

  private int totalSpots;


  public void changeTotalSpots(int totalSpots) {
    this.totalSpots = totalSpots;
  }

  @Builder
  public Institutes(String name, int totalSpots) {
    this.name = name;
    this.totalSpots = totalSpots;
  }
}
