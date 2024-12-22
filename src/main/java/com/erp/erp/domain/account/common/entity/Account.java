package com.erp.erp.domain.account.common.entity;


import com.erp.erp.domain.institute.common.entity.Institute;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "accounts")
public class Account {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @OneToOne
  @NotNull
  private Institute institute;

  @NotNull
  private String account;

  @NotNull
  private String password;

  private boolean locked;


  @Builder
  public Account(Institute institute, String account, String password) {
    this.institute = institute;
    this.account = account;
    this.password = password;
  }
}
