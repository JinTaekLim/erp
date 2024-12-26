package com.erp.erp.domain.reservation.common.entity;

import com.erp.erp.domain.customer.common.entity.Customer;
import com.erp.erp.domain.institute.common.entity.Institute;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "reservations")
public class Reservation {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  private Institute institute;

  @ManyToOne
  private Customer customer;

  private LocalDateTime startTime;

  private LocalDateTime endTime;

  private String memo;

  private int seatNumber;

  @Builder
  public Reservation(Institute institute, Customer customer, LocalDateTime startTime,
      LocalDateTime endTime, String memo, int seatNumber) {
    this.institute = institute;
    this.customer = customer;
    this.startTime = startTime;
    this.endTime = endTime;
    this.memo = memo;
    this.seatNumber = seatNumber;
  }

  @PrePersist
  @PreUpdate
  private void truncateTimes() {
    this.startTime = truncateToMinutes(this.startTime);
    this.endTime = truncateToMinutes(this.endTime);
  }

  private LocalDateTime truncateToMinutes(LocalDateTime dateTime) {
    return dateTime != null ? dateTime.withSecond(0).withNano(0) : null;
  }


  public void updatedReservations(LocalDateTime startTime, LocalDateTime endTime, String memo,
      int seatNumber) {
    this.startTime = startTime;
    this.endTime = endTime;
    this.memo = memo;
    this.seatNumber = seatNumber;
  }

  public void updatedSeat(int seatNumber) {
    this.seatNumber = seatNumber;
  }

}
