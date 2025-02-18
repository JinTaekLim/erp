package com.erp.erp.domain.customer.common.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "progress")
public class Progress {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  private Customer customer;

  @NotNull
  private LocalDate date;

  @NotNull
  private String content;

  private String createdId;

  private LocalDateTime createdAt;

  private String updatedId;

  private LocalDateTime updatedAt;

  @Builder
  public Progress(Customer customer, LocalDate date, String content, String createdId) {
    this.customer = customer;
    this.date = date;
    this.content = content;
    this.createdId = createdId;
    this.createdAt = LocalDateTime.now();
  }

  public Progress update(LocalDate date, String content, String updatedId) {
    this.date = date;
    this.content = content;
    this.updatedId = updatedId;
    this.updatedAt = LocalDateTime.now();
    return this;
  }
}
