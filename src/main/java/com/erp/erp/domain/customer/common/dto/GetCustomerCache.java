package com.erp.erp.domain.customer.common.dto;

import com.erp.erp.domain.customer.common.entity.CustomerStatus;
import com.erp.erp.domain.customer.common.entity.Gender;
import com.erp.erp.domain.plan.common.entity.CourseType;
import com.erp.erp.domain.plan.common.entity.LicenseType;
import com.erp.erp.domain.plan.common.entity.PlanType;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetCustomerCache {

  private List<GetCustomers> getCustomers;
  private long updatedTime;


  @Getter
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class GetCustomers {

    private Long customerId;
    private CustomerStatus status;
    private String photoUrl;
    private String name;
    private Gender gender;
    private String phone;
    private LicenseType licenseType;
    private String planName;
    private PlanType planType;
    private CourseType courseType;
    private double remainingTime;
    private int remainingPeriod;
    private double usedTime;
    private LocalDateTime registrationDate;
    private int lateCount;
    private int absenceCount;
    private int otherPaymentPrice;

  }
}
