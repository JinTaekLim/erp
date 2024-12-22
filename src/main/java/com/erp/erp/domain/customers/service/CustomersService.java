package com.erp.erp.domain.customers.service;

import com.erp.erp.domain.accounts.business.PhotoUtil;
import com.erp.erp.domain.auth.business.AuthProvider;
import com.erp.erp.domain.customers.business.CustomersCreator;
import com.erp.erp.domain.customers.business.CustomersReader;
import com.erp.erp.domain.customers.business.CustomersUpdater;
import com.erp.erp.domain.customers.business.ProgressCreator;
import com.erp.erp.domain.customers.common.dto.AddCustomerDto;
import com.erp.erp.domain.customers.common.dto.SearchCustomerNameDto;
import com.erp.erp.domain.customers.common.dto.UpdateStatusDto;
import com.erp.erp.domain.customers.common.dto.UpdateCustomerDto;
import com.erp.erp.domain.customers.common.entity.CustomerStatus;
import com.erp.erp.domain.customers.common.entity.Customers;
import com.erp.erp.domain.customers.common.entity.Progress;
import com.erp.erp.domain.institutes.common.entity.Institutes;
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
public class CustomersService {

  private final AuthProvider authProvider;
  private final CustomersCreator customersCreator;
  private final CustomersReader customersReader;
  private final CustomersUpdater customersUpdater;
  private final ProgressCreator progressCreator;
  private final PlanReader planReader;
  private final PhotoUtil photoUtil;

  @Transactional
  public AddCustomerDto.Response addCustomer(AddCustomerDto.Request req) {
    Institutes institutes = authProvider.getCurrentInstitute();
    Plan plan = planReader.findById(req.getPlansId());
    MultipartFile photo = null;
    String photoUrl = (photo == null) ? null : photoUtil.upload(photo);

    Customers customers = req.toCustomers(institutes, plan, photoUrl);
    customersCreator.save(customers);

    return AddCustomerDto.Response.fromEntity(customers);
  };


  // note. 본인 매장의 고객 정보만 변경할 수 있도록 별도의 처리 필요
  public CustomerStatus updateStatus(UpdateStatusDto.Request req) {
    Long customersId = req.getCustomersId();
    CustomerStatus status = req.getStatus();
    customersUpdater.updateStatus(customersId, status);
    return customersReader.findById(customersId).getStatus();
  }

  @Transactional
  public UpdateCustomerDto.Response updateCustomer(UpdateCustomerDto.Request req) {
    Customers customers = customersReader.findById(req.getCustomerId());
    Customers updateCustomers = req.updatedCustomers(customers);
    customersCreator.save(updateCustomers);
    Progress progress = req.toProgress();
    progressCreator.save(progress);

    return UpdateCustomerDto.Response.fromEntity(updateCustomers, progress);
  }

  public List<Customers> getCurrentCustomers(int page) {
    Institutes institutes = authProvider.getCurrentInstitute();
    Pageable pageable = PageRequest.of(page, 4);
    Page<Customers> customersPage = customersReader.findByInstitutesIdAndStatusActive(
        institutes,
        pageable
    );
    return customersPage.getContent();
  }

  public List<Customers> getExpiredCustomers(int page) {
    Institutes institutes = authProvider.getCurrentInstitute();
    Pageable pageable = PageRequest.of(page, 4);
    Page<Customers> customersPage = customersReader.findByInstitutesIdAndStatusInactive(
        institutes,
        pageable
    );
    return customersPage.getContent();
  }

  public List<Customers> getCurrentCustomers(){
    Institutes institutes = authProvider.getCurrentInstitute();
    return customersReader.findByInstitutesIdAndStatusActive(institutes);
  }

  public List<SearchCustomerNameDto.Response> searchCustomerName(String keyword) {
    Institutes institutesId = authProvider.getCurrentInstitute();
    List<Customers> customers = customersReader.findByInstitutesIdAndNameStartingWithAndStatusIn(
        institutesId,
        keyword
    );
    return customers.stream()
        .map(SearchCustomerNameDto.Response::fromEntity)
        .toList();
  }
}
