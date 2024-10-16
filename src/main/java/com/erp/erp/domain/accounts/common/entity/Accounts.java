package com.erp.erp.domain.accounts.common.entity;


import com.erp.erp.domain.institutes.common.entity.Institutes;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Accounts {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "institute_id")
  private Institutes institute;

  private String accountId;

  private String password;

  private boolean isLocked;

}
