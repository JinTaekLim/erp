package com.erp.erp.domain.reservations.common.entity;

import com.erp.erp.domain.customer.common.entity.Customer;
import com.erp.erp.domain.institutes.common.entity.Institutes;
import jakarta.persistence.*;

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
  private Customer customer;

  private LocalDateTime startTime;

  private LocalDateTime endTime;

  private String memo;

  private int seatNumber;

  @Builder
  public Reservations(Institutes institutes, Customer customer, LocalDateTime startTime,
      LocalDateTime endTime, String memo) {
    this.institutes = institutes;
    this.customer = customer;
    this.startTime = startTime;
    this.endTime = endTime;
    this.memo = memo;
  }

  @PrePersist
  @PreUpdate
  private void truncateTimes() {
    this.startTime = truncateToMinutes(this.startTime);
    this.endTime = truncateToMinutes(this.endTime);
  }

  private LocalDateTime truncateToMinutes(LocalDateTime dateTime) {
    return dateTime != null ? dateTime.withSecond(0) : null;
  }


  public void updatedReservations(LocalDateTime startTime, LocalDateTime endTime, String memo) {
    this.startTime = startTime;
    this.endTime = endTime;
    this.memo = memo;
  }

  public void updatedSeat(int seatNumber) {
    this.seatNumber = seatNumber;
  }

}
