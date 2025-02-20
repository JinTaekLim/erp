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

  private AttendanceStatus attendanceStatus;

  private String createdId;

  private LocalDateTime createdAt;

  private String updatedId;

  private LocalDateTime updatedAt;

  @Builder
  public Reservation(Institute institute, Customer customer, LocalDateTime startTime,
      LocalDateTime endTime, String memo, int seatNumber, String createdId) {
    this.institute = institute;
    this.customer = customer;
    this.startTime = startTime;
    this.endTime = endTime;
    this.memo = memo;
    this.seatNumber = seatNumber;
    attendanceStatus = AttendanceStatus.NORMAL;
    this.createdId = createdId;
    this.createdAt = LocalDateTime.now();
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
      int seatNumber, AttendanceStatus attendanceStatus, String updatedId) {
    this.startTime = startTime;
    this.endTime = endTime;
    this.memo = memo;
    this.seatNumber = seatNumber;
    this.attendanceStatus = attendanceStatus;
    this.updatedId = updatedId;
    this.updatedAt = LocalDateTime.now();
  }

  public void updatedSeat(int seatNumber, String updatedId) {
    this.seatNumber = seatNumber;
    this.updatedId = updatedId;
    this.updatedAt = LocalDateTime.now();
  }

}
