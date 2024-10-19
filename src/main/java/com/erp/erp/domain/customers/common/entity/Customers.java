package com.erp.erp.domain.customers.common.entity;

import com.erp.erp.domain.institutes.common.entity.Institutes;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Customers {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @NotNull
  private Institutes institutes;

  @NotNull
  private String name;

  @NotNull
  private Gender gender;

  @NotNull
  private String phone;

  @NotNull
  private String address;

  private String photoUrl;

  @NotNull
  private LocalDate birthDate;

  @NotNull
  private Boolean status;

  @Builder
  public Customers(Institutes institutes, String name, Gender gender, String phone, String address,
      String photoUrl, LocalDate birthDate) {
    this.institutes = institutes;
    this.name = name;
    this.gender = gender;
    this.phone = phone;
    this.address = address;
    this.photoUrl = photoUrl;
    this.birthDate = birthDate;
    this.status = true;
  }
}
