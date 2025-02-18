package com.erp.erp.domain.customer.service;

import com.erp.erp.domain.account.business.PhotoUtil;
import com.erp.erp.domain.account.common.entity.Account;
import com.erp.erp.domain.auth.business.AuthProvider;
import com.erp.erp.domain.customer.business.CustomerCreator;
import com.erp.erp.domain.customer.business.CustomerReader;
import com.erp.erp.domain.customer.business.CustomerUpdater;
import com.erp.erp.domain.customer.business.ProgressManger;
import com.erp.erp.domain.customer.business.ProgressReader;
import com.erp.erp.domain.customer.common.dto.AddCustomerDto;
import com.erp.erp.domain.customer.common.dto.GetAvailableCustomerNamesDto;
import com.erp.erp.domain.customer.common.dto.GetCustomerDetailDto;
import com.erp.erp.domain.customer.common.dto.GetCustomerDto;
import com.erp.erp.domain.customer.common.dto.SearchCustomerNameDto;
import com.erp.erp.domain.customer.common.dto.UpdateStatusDto;
import com.erp.erp.domain.customer.common.dto.UpdateCustomerDto;
import com.erp.erp.domain.customer.common.entity.CustomerStatus;
import com.erp.erp.domain.customer.common.entity.Customer;
import com.erp.erp.domain.customer.common.entity.Progress;
import com.erp.erp.domain.customer.common.mapper.CustomerMapper;
import com.erp.erp.domain.institute.common.entity.Institute;
import com.erp.erp.domain.plan.business.PlanReader;
import com.erp.erp.domain.plan.common.entity.Plan;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerService {

  int PAGE_SIZE = 20;

  private final AuthProvider authProvider;
  private final CustomerCreator customerCreator;
  private final CustomerReader customerReader;
  private final CustomerUpdater customerUpdater;
  private final PlanReader planReader;
  private final PhotoUtil photoUtil;
  private final ProgressReader progressReader;
  private final CustomerMapper customerMapper;
  private final ProgressManger progressManger;

  @Transactional
  public AddCustomerDto.Response addCustomer(AddCustomerDto.Request req, MultipartFile file) {
    Account account = authProvider.getCurrentAccount();
    Institute institute = account.getInstitute();
    Plan plan = planReader.findById(req.getPlanId());
    String photoUrl = photoUtil.upload(file);

    Customer customer = customerMapper.dtoToEntity(
        req, institute, plan, photoUrl, String.valueOf(account.getId())
    );
    customerCreator.save(customer);
    return customerMapper.entityToAddCustomerResponse(customer);
  }

  public CustomerStatus updateStatus(UpdateStatusDto.Request req) {
    Account account = authProvider.getCurrentAccount();
    Institute institute = account.getInstitute();
    Long customersId = req.getCustomerId();
    customerUpdater.updateStatus(customersId, req.getStatus(), String.valueOf(account.getId()));
    return customerReader.findByIdAndInstituteId(customersId, institute.getId()).getStatus();
  }

  @Transactional
  public UpdateCustomerDto.Response updateCustomer(UpdateCustomerDto.Request req, MultipartFile file) {
    Account account = authProvider.getCurrentAccount();
    Institute institute = account.getInstitute();
    Customer customer = customerReader.findByIdAndInstituteId(req.getCustomerId(),
        institute.getId());
    String photoUrl = customer.getPhotoUrl();
    if (file != null) photoUrl = photoUtil.upload(file);

    Customer updateCustomer = customerUpdater.updateCustomer(
        req, photoUrl, customer, String.valueOf(account.getId())
    );
    List<Progress> progresses = progressManger.add(customer, req.getProgressList());

    return customerMapper.entityToUpdateCustomerResponse(updateCustomer, progresses);
  }

  public List<GetCustomerDto.Response> getCustomers(GetCustomerDto.Request req) {
    Institute institute = authProvider.getCurrentInstitute();
    Long lastId = req.getLastId();

    if (lastId == null) {
      lastId = customerReader.findTopIdByInstituteId(institute.getId());
      // lastId 도 조회 결과에 포함 시키기 위해 +1
      lastId ++;
    }

    List<Customer> customers = customerReader.findAllAfterLastId(
        institute.getId(),
        lastId,
        req.getStatus(),
        PAGE_SIZE
    );

    return customerMapper.entityToGetCustomerResponse(customers);
  }

  public List<GetAvailableCustomerNamesDto.Response> getCurrentCustomers() {
    Institute institute = authProvider.getCurrentInstitute();
    List<Customer> customers = customerReader.findByInstitutesIdAndStatusActive(institute);
    return customerMapper.entityToGetAvailableCustomerNamesResponse(customers);
  }

  public List<SearchCustomerNameDto.Response> searchCustomerName(String keyword) {
    Institute instituteId = authProvider.getCurrentInstitute();
    List<Customer> customers = customerReader.findByInstitutesIdAndNameStartingWithAndStatusIn(
        instituteId,
        keyword
    );
    return customerMapper.entityToSearchCustomerNameResponse(customers);
  }

  public GetCustomerDetailDto.Response getCustomerDetail(Long customerId) {
    Institute institute = authProvider.getCurrentInstitute();
    Customer customer = customerReader.findByIdAndInstituteId(customerId, institute.getId());
    List<Progress> progress = progressReader.findByCustomerId(customerId);
    return customerMapper.entityToGetCustomerDetailResponse(customer, progress);
  }

  public List<GetCustomerDto.Response> searchCustomer(String customerName) {
    Institute instituteId = authProvider.getCurrentInstitute();
    List<Customer> customers = customerReader.findByInstitutesIdAndNameStartingWithAndStatusIn(
        instituteId,
        customerName
    );
    return customerMapper.entityToGetCustomerResponse(customers);
  }
}
