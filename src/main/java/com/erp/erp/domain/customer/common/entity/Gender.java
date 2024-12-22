package com.erp.erp.domain.customer.common.entity;


import com.erp.erp.domain.customer.common.exception.InvalidGenderException;

public enum Gender {
  MALE,
  FEMALE;

  public static Gender getString(String gender) {
    return switch (gender.toUpperCase()) {
      case "M" -> MALE;
      case "W" -> FEMALE;
      default -> throw new InvalidGenderException();
    };
  }
}
