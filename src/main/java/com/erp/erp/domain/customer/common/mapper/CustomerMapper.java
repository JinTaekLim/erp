package com.erp.erp.domain.customer.common.mapper;

import com.erp.erp.domain.customer.common.dto.AddCustomerDto;
import com.erp.erp.domain.customer.common.dto.GetAvailableCustomerNamesDto;
import com.erp.erp.domain.customer.common.dto.GetCustomerDetailDto;
import com.erp.erp.domain.customer.common.dto.GetCustomerDetailDto.OtherPaymentResponse;
import com.erp.erp.domain.customer.common.dto.GetCustomerDetailDto.PlanPaymentResponse;
import com.erp.erp.domain.customer.common.dto.GetCustomerDto;
import com.erp.erp.domain.customer.common.dto.ProgressDto;
import com.erp.erp.domain.customer.common.dto.SearchCustomerNameDto;
import com.erp.erp.domain.customer.common.dto.UpdateCustomerDto;
import com.erp.erp.domain.customer.common.entity.Customer;
import com.erp.erp.domain.customer.common.entity.Progress;
import com.erp.erp.domain.institute.common.entity.Institute;
import com.erp.erp.domain.payment.common.entity.OtherPayment;
import com.erp.erp.domain.payment.common.entity.PlanPayment;
import com.erp.erp.domain.plan.common.entity.Plan;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CustomerMapper {

  @Mapping(target = "name", source = "req.name")
  @Mapping(target = "photoUrl", source = "photoUrl")
  @Mapping(target = "planPayment", expression = "java(planPaymentResponseToPlanPayment(req, plan))")
  @Mapping(target = "otherPayments", source = "req.otherPayment")
  @Mapping(target = "createdId", source = "createdId")
  Customer dtoToEntity(AddCustomerDto.Request req, Institute institute, Plan plan, String photoUrl, String createdId);

  @Mapping(target = "plan", source = "plan")
  @Mapping(target = "status", source = "req.planPayment.status")
  @Mapping(target = "registrationAt", source = "req.planPayment.registrationAt")
  @Mapping(target = "paymentsMethod", source = "req.planPayment.paymentsMethod")
  @Mapping(target = "discountName", source = "req.planPayment.discountName")
  @Mapping(target = "discountRate", source = "req.planPayment.discountRate")
  @Mapping(target = "otherPaymentMethod", source = "req.planPayment.otherPaymentMethod")
  PlanPayment planPaymentResponseToPlanPayment(AddCustomerDto.Request req, Plan plan);

  @Mapping(target = "customerId", source = "customer.id")
  @Mapping(target = "progressList", expression = "java(entityToProgressResponse(progressList))")
  @Mapping(target = "otherPayment", source = "customer.otherPayments")
  @Mapping(target = "planPaymentStatus", source = "customer.planPayment.status")
  UpdateCustomerDto.Response entityToUpdateCustomerResponse(Customer customer, List<Progress> progressList);

  @Mapping(target = "progressId", source = "id")
  @Mapping(target = "date", source = "date")
  @Mapping(target = "content", source = "content")
  ProgressDto.Response progressToProgressResponse(Progress progress);

  List<ProgressDto.Response> entityToProgressResponse(List<Progress> progress);

  List<GetCustomerDto.Response> entityToGetCustomerResponse(List<Customer> customers);

  @Mapping(target = "customerId", source = "customer.id")
  @Mapping(target = "licenseType", source = "customer.planPayment.plan.licenseType")
  @Mapping(target = "planName", source = "customer.planPayment.plan.name")
  @Mapping(target = "planType", source = "customer.planPayment.plan.planType")
  @Mapping(target = "courseType", source = "customer.planPayment.plan.courseType")
  @Mapping(target = "remainingTime", expression = "java(0)")
  @Mapping(target = "remainingPeriod", expression = "java(0)")
  @Mapping(target = "usedTime", expression = "java(0)")
  @Mapping(target = "registrationDate", source = "customer.planPayment.registrationAt")
  @Mapping(target = "lateCount", expression = "java(0L)")
  @Mapping(target = "absenceCount", expression = "java(0L)")
  @Mapping(target = "otherPaymentPrice", expression = "java(getOtherPaymentPrice(customer.getOtherPayments()))")
  GetCustomerDto.Response entityToGetCustomerResponse(Customer customer);

  default int getOtherPaymentPrice(List<OtherPayment> otherPaymentList) {
    return otherPaymentList.stream()
        .mapToInt(OtherPayment::getPrice)
        .sum();
  }

  List<GetAvailableCustomerNamesDto.Response> entityToGetAvailableCustomerNamesResponse(List<Customer> customers);

  List<SearchCustomerNameDto.Response> entityToSearchCustomerNameResponse(List<Customer> customers);

  @Mapping(target = "customerId", source = "id")
  SearchCustomerNameDto.Response entityToSearchCustomerNameResponse(Customer customers);


  @Mapping(target = "otherPayment", expression = "java(otherPaymentResponseToOtherPaymentResponse(customer.getOtherPayments()))")
  @Mapping(target = "progressList", expression = "java(entityToProgressResponse(progressList))")
  GetCustomerDetailDto.Response entityToGetCustomerDetailResponse(Customer customer, List<Progress> progressList);

  @Mapping(target = "licenseType", source = "planPayment.plan.licenseType")
  @Mapping(target = "planName", source = "planPayment.plan.name")
  @Mapping(target = "planType", source = "planPayment.plan.planType")
  @Mapping(target = "courseType", source = "planPayment.plan.courseType")
  @Mapping(target = "planPrice", source = "planPayment.plan.price")
  @Mapping(target = "discountPrice", expression = "java(calculateDiscountPrice(planPayment))")
  @Mapping(target = "paymentTotal", expression = "java(calculatePaymentTotal(planPayment))")
  PlanPaymentResponse entityToGetCustomerDetailResponse(PlanPayment planPayment);
  List<OtherPaymentResponse> otherPaymentResponseToOtherPaymentResponse(List<OtherPayment> otherPayments);

  default int calculateDiscountPrice(PlanPayment planPayment) {
    int planPrice = planPayment.getPlan().getPrice();
    double discountRate = planPayment.getDiscountRate() / 100.0;
    return (int) (planPrice * discountRate);
  }

  default int calculatePaymentTotal(PlanPayment planPayment) {
    int planPrice = planPayment.getPlan().getPrice();
    int discountPrice = calculateDiscountPrice(planPayment);
    return planPrice - discountPrice;
  }
}
