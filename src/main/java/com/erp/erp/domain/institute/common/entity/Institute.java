package com.erp.erp.domain.institute.common.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "Institutes")
public class Institute {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotNull
  private String name;

  private int totalSeat;


  public void changeTotalSpots(int totalSeat) {
    this.totalSeat = totalSeat;
  }

  @Builder
  public Institute(String name, int totalSeat) {
    this.name = name;
    this.totalSeat = totalSeat;
  }
}
