package com.erp.erp.domain.customer.common.mapper;

import com.erp.erp.domain.customer.common.dto.AddCustomerDto;
import com.erp.erp.domain.customer.common.dto.GetAvailableCustomerNamesDto;
import com.erp.erp.domain.customer.common.dto.GetCustomerDetailDto;
import com.erp.erp.domain.customer.common.dto.GetCustomerDetailDto.OtherPaymentResponse;
import com.erp.erp.domain.customer.common.dto.GetCustomerDetailDto.PlanPaymentResponse;
import com.erp.erp.domain.customer.common.dto.GetCustomerDetailDto.ProgressResponse;
import com.erp.erp.domain.customer.common.dto.GetCustomerDto;
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
  Customer dtoToEntity(AddCustomerDto.Request req, Institute institute, Plan plan, String photoUrl);

  @Mapping(target = "plan", source = "plan")
  @Mapping(target = "status", source = "req.planPayment.status")
  @Mapping(target = "registrationAt", source = "req.planPayment.registrationAt")
  @Mapping(target = "paymentsMethod", source = "req.paymentsMethod")
  @Mapping(target = "discountRate", source = "req.planPayment.discountRate")
  PlanPayment planPaymentResponseToPlanPayment(AddCustomerDto.Request req, Plan plan);

  @Mapping(target = "planName", source = "customer.planPayment.plan.name")
  AddCustomerDto.Response entityToAddCustomerResponse(Customer customer);

  @Mapping(target = "customerId", source = "customer.id")
  @Mapping(target = "progress", source = "progresses")
  @Mapping(target = "otherPayment", source = "customer.otherPayments")
  UpdateCustomerDto.Response entityToUpdateCustomerResponse(Customer customer, List<Progress> progresses);


  List<GetCustomerDto.Response> entityToGetCustomerResponse(List<Customer> customers);

  @Mapping(target = "licenseType", source = "customer.planPayment.plan.licenseType")
  @Mapping(target = "planName", source = "customer.planPayment.plan.name")
  @Mapping(target = "planType", source = "customer.planPayment.plan.planType")
  @Mapping(target = "remainingTime", expression = "java(0)")
  @Mapping(target = "remainingPeriod", expression = "java(0)")
  @Mapping(target = "usedTime", expression = "java(0)")
  @Mapping(target = "registrationDate", source = "customer.planPayment.registrationAt")
  @Mapping(target = "tardinessCount", expression = "java(0)")
  @Mapping(target = "absenceCount", expression = "java(0)")
  GetCustomerDto.Response entityToGetCustomerResponse(Customer customer);

  List<GetAvailableCustomerNamesDto.Response> entityToGetAvailableCustomerNamesResponse(List<Customer> customers);

  List<SearchCustomerNameDto.Response> entityToSearchCustomerNameResponse(List<Customer> customers);

  @Mapping(target = "customerId", source = "id")
  SearchCustomerNameDto.Response entityToSearchCustomerNameResponse(Customer customers);


  @Mapping(target = "visitPath", expression = "java(null)")
  @Mapping(target = "progressList", expression = "java(planPaymentResponseToProgressResponse(progress))")
  @Mapping(target = "otherPayment", expression = "java(otherPaymentResponseToOtherPaymentResponse(customer.getOtherPayments()))")
  GetCustomerDetailDto.Response entityToGetCustomerDetailResponse(Customer customer, List<Progress> progress);
  @Mapping(target = "licenseType", source = "planPayment.plan.licenseType")
  @Mapping(target = "planName", source = "planPayment.plan.name")
  @Mapping(target = "planPrice", source = "planPayment.plan.price")
  @Mapping(target = "discountPrice", expression = "java(calculateDiscountPrice(planPayment))")
  @Mapping(target = "paymentTotal", expression = "java(calculatePaymentTotal(planPayment))")
  PlanPaymentResponse entityToGetCustomerDetailResponse(PlanPayment planPayment);
  List<ProgressResponse> planPaymentResponseToProgressResponse(List<Progress> progresses);
  List<OtherPaymentResponse> otherPaymentResponseToOtherPaymentResponse(List<OtherPayment> otherPayments);

  default int calculateDiscountPrice(PlanPayment planPayment) {
    int planPrice = planPayment.getPlan().getPrice();
    return (int) (planPrice * planPayment.getDiscountRate());
  }

  default int calculatePaymentTotal(PlanPayment planPayment) {
    int planPrice = planPayment.getPlan().getPrice();
    int discountPrice = calculateDiscountPrice(planPayment);
    return planPrice - discountPrice;
  }
}
