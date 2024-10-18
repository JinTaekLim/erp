package com.erp.erp.domain.accounts.common.entity;


import com.erp.erp.domain.institutes.common.entity.Institutes;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Accounts {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "institute_id")
  @NotNull
  private Institutes institute;

  @NotNull
  private String accountId;

  @NotNull
  private String password;

  private boolean locked;


  @Builder
  public Accounts(Institutes institute, String accountId, String password, boolean locked) {
    this.institute = institute;
    this.accountId = accountId;
    this.password = password;
    this.locked = locked;
  }
}
