package com.erp.erp.domain.reservations.common.entity;

import com.erp.erp.domain.customers.common.entity.Customers;
import com.erp.erp.domain.institutes.common.entity.Institutes;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Reservations {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  private Institutes institutes;

  @ManyToOne
  private Customers customers;

  private LocalDateTime startTime;

  private LocalDateTime endTime;

  private String memo;

  private int seatNumber;

  @Builder
  public Reservations(Institutes institutes, Customers customers, LocalDateTime startTime,
      LocalDateTime endTime, String memo) {
    this.institutes = institutes;
    this.customers = customers;
    this.startTime = startTime;
    this.endTime = endTime;
    this.memo = memo;
  }


  public Reservations update(LocalDateTime startTime, LocalDateTime endTime, String memo) {
    this.startTime = startTime;
    this.endTime = endTime;
    this.memo = memo;
    return this;
  }
}
