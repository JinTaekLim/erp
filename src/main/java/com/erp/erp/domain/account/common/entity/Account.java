package com.erp.erp.domain.account.common.entity;


import com.erp.erp.domain.institute.common.entity.Institute;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
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

  @ManyToOne
  @NotNull
  private Institute institute;

  @NotNull
  @Column(unique = true)
  private String identifier;

  @NotNull
  private String password;

  private boolean locked;


  @Builder
  public Account(Institute institute, String identifier, String password) {
    this.institute = institute;
    this.identifier = identifier;
    this.password = password;
  }

  public Account updateIdentifierAndPassword(String identifier, String password) {
    this.identifier = identifier;
    this.password = password;
    return this;
  }

  public void updateLocked(boolean locked) {
    this.locked = locked;
  }
}
