package com.erp.erp.domain.customer.common.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToOne;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class CustomerPhoto {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @OneToOne
  @JoinColumn(unique = true)
  private Customer customer;

  @Lob
  private byte[] data;

  @Builder
  public CustomerPhoto(Customer customer, byte[] data) {
    this.customer = customer;
    this.data = data;
  }

  public void updateData(byte[] data) {
    this.data = data;
  }
}
