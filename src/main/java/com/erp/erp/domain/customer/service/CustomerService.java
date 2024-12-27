package com.erp.erp.domain.customer.service;

import com.erp.erp.domain.account.business.PhotoUtil;
import com.erp.erp.domain.auth.business.AuthProvider;
import com.erp.erp.domain.customer.business.CustomerCreator;
import com.erp.erp.domain.customer.business.CustomerReader;
import com.erp.erp.domain.customer.business.CustomerUpdater;
import com.erp.erp.domain.customer.business.ProgressCreator;
import com.erp.erp.domain.customer.business.ProgressDeleter;
import com.erp.erp.domain.customer.business.ProgressReader;
import com.erp.erp.domain.customer.common.dto.AddCustomerDto;
import com.erp.erp.domain.customer.common.dto.GetCustomerDetailDto;
import com.erp.erp.domain.customer.common.dto.SearchCustomerNameDto;
import com.erp.erp.domain.customer.common.dto.UpdateStatusDto;
import com.erp.erp.domain.customer.common.dto.UpdateCustomerDto;
import com.erp.erp.domain.customer.common.entity.CustomerStatus;
import com.erp.erp.domain.customer.common.entity.Customer;
import com.erp.erp.domain.customer.common.entity.Progress;
import com.erp.erp.domain.institute.common.entity.Institute;
import com.erp.erp.domain.plan.business.PlanReader;
import com.erp.erp.domain.plan.common.entity.Plan;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerService {

  private final AuthProvider authProvider;
  private final CustomerCreator customerCreator;
  private final CustomerReader customerReader;
  private final CustomerUpdater customerUpdater;
  private final ProgressCreator progressCreator;
  private final PlanReader planReader;
  private final PhotoUtil photoUtil;
  private final ProgressReader progressReader;
  private final ProgressDeleter progressDeleter;

  @Transactional
  public AddCustomerDto.Response addCustomer(AddCustomerDto.Request req) {
    Institute institute = authProvider.getCurrentInstitute();
    Plan plan = planReader.findById(req.getPlanId());
    MultipartFile photo = null;
    String photoUrl = (photo == null) ? null : photoUtil.upload(photo);

    Customer customer = req.toCustomers(institute, plan, photoUrl);
    customerCreator.save(customer);

    return AddCustomerDto.Response.fromEntity(customer);
  }


  // note. 본인 매장의 고객 정보만 변경할 수 있도록 별도의 처리 필요
  public CustomerStatus updateStatus(UpdateStatusDto.Request req) {
    Institute institute = authProvider.getCurrentInstitute();
    Long customersId = req.getCustomerId();
    CustomerStatus status = req.getStatus();
    customerUpdater.updateStatus(customersId, status);
    return customerReader.findByIdAndInstituteId(institute.getId(), customersId).getStatus();
  }

  @Transactional
  public UpdateCustomerDto.Response updateCustomer(UpdateCustomerDto.Request req) {
    Institute institute = authProvider.getCurrentInstitute();
    Customer customer = customerReader.findByIdAndInstituteId(institute.getId(),
        req.getCustomerId());

    Customer updateCustomer = req.updatedCustomers(customer);
    customerCreator.save(updateCustomer);

    List<Progress> progresses = req.toProgressList(customer);
    progressDeleter.deleteAllByCustomerId(customer.getId());
    progressCreator.saveAll(progresses);

    return UpdateCustomerDto.Response.fromEntity(updateCustomer, progresses);
  }

  public List<Customer> getCurrentCustomers(int page) {
    Institute institute = authProvider.getCurrentInstitute();
    Pageable pageable = PageRequest.of(page, 4);
    Page<Customer> customersPage = customerReader.findByInstitutesIdAndStatusActive(
        institute,
        pageable
    );
    return customersPage.getContent();
  }

  public List<Customer> getExpiredCustomers(int page) {
    Institute institute = authProvider.getCurrentInstitute();
    Pageable pageable = PageRequest.of(page, 4);
    Page<Customer> customersPage = customerReader.findByInstitutesIdAndStatusInactive(
        institute,
        pageable
    );
    return customersPage.getContent();
  }

  public List<Customer> getCurrentCustomers() {
    Institute institute = authProvider.getCurrentInstitute();
    return customerReader.findByInstitutesIdAndStatusActive(institute);
  }

  public List<SearchCustomerNameDto.Response> searchCustomerName(String keyword) {
    Institute instituteId = authProvider.getCurrentInstitute();
    List<Customer> customers = customerReader.findByInstitutesIdAndNameStartingWithAndStatusIn(
        instituteId,
        keyword
    );
    return customers.stream()
        .map(SearchCustomerNameDto.Response::fromEntity)
        .toList();
  }

  public GetCustomerDetailDto.Response getCustomerDetail(Long customerId) {
    Institute institute = authProvider.getCurrentInstitute();
    Customer customer = customerReader.findByIdAndInstituteId(customerId, institute.getId());
    List<Progress> progress = progressReader.findByCustomerId(customerId);

    return GetCustomerDetailDto.Response.fromEntity(customer, progress);
  }
}
