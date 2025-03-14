package com.erp.erp.domain.customer.service;

import com.erp.erp.domain.customer.business.CustomerPhotoManger;
import com.erp.erp.domain.account.common.entity.Account;
import com.erp.erp.domain.auth.business.AuthProvider;
import com.erp.erp.domain.customer.business.CustomerCreator;
import com.erp.erp.domain.customer.business.CustomerReader;
import com.erp.erp.domain.customer.business.CustomerSender;
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
import com.erp.erp.domain.reservation.business.ReservationCacheManager;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
  private final CustomerPhotoManger customerPhotoManger;
  private final ProgressReader progressReader;
  private final CustomerMapper customerMapper;
  private final ProgressManger progressManger;
  private final CustomerSender customerSender;
  private final ReservationCacheManager reservationCacheManager;

  public void sendAddCustomerRequest(AddCustomerDto.Request req, MultipartFile file) {
    Account account = authProvider.getCurrentAccount();
    Plan plan = planReader.findById(req.getPlanId());
    customerSender.sendAddCustomer(account, plan, req, file);
  }

  @Transactional
  public void addCustomer(Account account, Plan plan, AddCustomerDto.Request req, byte[] file) {
    Institute institute = account.getInstitute();
    String photoUrl = file == null ? null : customerPhotoManger.upload(file);
    Customer customer = customerMapper.dtoToEntity(
        req, institute, plan, photoUrl, String.valueOf(account.getId())
    );
    customerCreator.save(customer);

    reservationCacheManager.save(customer);

    // 사진을 전달 받았지만 S3에 정상적으로 저장하지 못 한 경우 DB에 데이터 임시 저장
    if (photoUrl == null && file != null) {
      customerPhotoManger.saveTempImage(customer, file);
    }
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
    if (file != null) photoUrl = customerPhotoManger.update(customer, file, customer.getPhotoUrl());

    Customer updateCustomer = customerUpdater.updateCustomer(
        req, photoUrl, customer, String.valueOf(account.getId())
    );
    List<Progress> progresses = progressManger.add(
        customer, req.getProgressList(), String.valueOf(account.getId())
    );

    return customerMapper.entityToUpdateCustomerResponse(updateCustomer, progresses);
  }

  public List<GetCustomerDto.Response> getCustomers(Long lastId, CustomerStatus status) {
    Institute institute = authProvider.getCurrentInstitute();
    Long instituteId = institute.getId();

    // 첫 페이지가 아닐 경우 DB 에 접근해 데이터 반환
    if (lastId != null) {
      return customerReader.findAllAfterLastId(instituteId, lastId, status, PAGE_SIZE);
    }
    // 캐시가 존재할 경우 이를 활용해 데이터 조회
    List<GetCustomerDto.Response> response = new ArrayList<>(reservationCacheManager.getCustomers(instituteId));

    // 캐시 데이터가 충분한 경우 반환
    if (response.size() == PAGE_SIZE) return response;

    // 캐시 데이터가 존재하지 않으면 가장 최근에 저장된 고객의 ID 값을 조회
    if (response.isEmpty()) {lastId = customerReader.findTopIdByInstituteId(instituteId) + 1;}
    // 캐시 데이터가 존재하지만 최대 반환 수에 미치지 못 하는 경우 캐시 데이터 중 가장 작은 고객 ID 값을 조회
    else if (response.size() < PAGE_SIZE) {
      lastId = response.stream()
          .map(GetCustomerDto.Response::getCustomerId)
          .min(Long::compare)
          .orElse(lastId);
    }

    // 부족한 데이터 조회
    List<GetCustomerDto.Response> add = customerReader.findAllAfterLastId(instituteId, lastId, status, PAGE_SIZE-response.size());
    if (add != null) response.addAll(add);

    return response;
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
