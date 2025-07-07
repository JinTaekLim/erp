package com.erp.erp.domain.customer.service;

import com.erp.erp.domain.customer.business.CustomerPhotoCreator;
import com.erp.erp.domain.customer.business.CustomerPhotoManger;
import com.erp.erp.domain.account.common.entity.Account;
import com.erp.erp.domain.auth.business.AuthProvider;
import com.erp.erp.domain.customer.business.CustomerCreator;
import com.erp.erp.domain.customer.business.CustomerReader;
import com.erp.erp.domain.customer.business.CustomerUpdater;
import com.erp.erp.domain.customer.business.GetCustomerCacheManager;
import com.erp.erp.domain.customer.common.dto.UpdateCustomersCacheEvent;
import com.erp.erp.domain.customer.common.dto.UpdateCustomersCacheMessageDto;
import com.erp.erp.domain.customer.common.entity.CustomerPhoto;
import com.erp.erp.domain.customer.common.mapper.CustomerParser;
import com.erp.erp.domain.customer.common.mapper.CustomerPhotoMapper;
import com.erp.erp.domain.customer.common.projection.GetCustomersProjection;
import com.erp.erp.domain.customer.support.CustomerExtractor;
import com.erp.erp.domain.progress.business.ProgressExtractor;
import com.erp.erp.domain.progress.business.ProgressReader;
import com.erp.erp.domain.customer.common.dto.AddCustomerDto;
import com.erp.erp.domain.customer.common.dto.GetAvailableCustomerNamesDto;
import com.erp.erp.domain.customer.common.dto.GetCustomerDetailDto;
import com.erp.erp.domain.customer.common.dto.GetCustomerDto;
import com.erp.erp.domain.customer.common.dto.SearchCustomerNameDto;
import com.erp.erp.domain.customer.common.dto.UpdateStatusDto;
import com.erp.erp.domain.customer.common.dto.UpdateCustomerDto;
import com.erp.erp.domain.customer.common.entity.CustomerStatus;
import com.erp.erp.domain.customer.common.entity.Customer;
import com.erp.erp.domain.progress.business.ProgressUpdater;
import com.erp.erp.domain.progress.common.entity.Progress;
import com.erp.erp.domain.customer.common.mapper.CustomerMapper;
import com.erp.erp.domain.institute.common.entity.Institute;
import com.erp.erp.domain.plan.business.PlanReader;
import com.erp.erp.domain.plan.common.entity.Plan;
import com.erp.erp.domain.reservation.business.ReservationCacheManager;
import com.erp.erp.domain.reservation.business.ReservationReader;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerService {

  private final ApplicationEventPublisher applicationEventPublisher;

  int PAGE_SIZE = 20;

  private final AuthProvider authProvider;
  private final CustomerCreator customerCreator;
  private final CustomerReader customerReader;
  private final CustomerUpdater customerUpdater;
  private final PlanReader planReader;
  private final CustomerPhotoManger customerPhotoManger;
  private final ProgressReader progressReader;
  private final CustomerMapper customerMapper;
  private final ReservationCacheManager reservationCacheManager;
  private final ProgressUpdater progressUpdater;
  private final CustomerPhotoMapper customerPhotoMapper;
  private final CustomerPhotoCreator customerPhotoCreator;
  private final GetCustomerCacheManager getCustomerCacheManager;
  private final ReservationReader reservationReader;


  private final CustomerParser customerParser = new CustomerParser();
  private final ProgressExtractor progressExtractor = new ProgressExtractor();
  private final CustomerExtractor customerExtractor = new CustomerExtractor();

  @Transactional
  public void sendAddCustomerRequest(AddCustomerDto.Request req, MultipartFile file) {
    Account account = authProvider.getCurrentAccount();
    Institute institute = account.getInstitute();
    Long instituteId = institute.getId();

    // 이용권 조회
    Plan plan = planReader.findById(req.getPlanId());

    // 파일 업로드
    String photoUrl = (file == null) ? null : customerPhotoManger.uploadOrNull(file);

    // 회원 저장
    Customer customer = customerMapper.dtoToEntity(
        req, institute, plan, photoUrl, String.valueOf(account.getId())
    );
    customerCreator.save(customer);


    // 사진을 전달 받았지만 S3에 정상적으로 저장하지 못 한 경우
    if (photoUrl == null && file != null) {
      CustomerPhoto customerPhoto = customerPhotoMapper.toCustomerPhoto(customer, file);
      customerPhotoCreator.save(customerPhoto);
    }

    // 캐시 삭제
    LocalDateTime date = LocalDateTime.now();
    getCustomerCacheManager.deleteCache(instituteId, date);

    // 트랜잭션 종료 이후 캐시 갱신 메세지 큐 발행
    UpdateCustomersCacheEvent event = customerMapper.toUpdateCustomersCacheEvent(instituteId, date);
    applicationEventPublisher.publishEvent(event);

  }

  @Transactional
  public CustomerStatus updateStatus(UpdateStatusDto.Request req) {
    Account account = authProvider.getCurrentAccount();
    Long instituteId = account.getInstitute().getId();
    Long customersId = req.getCustomerId();

    customerUpdater.updateStatus(customersId, req.getStatus(), String.valueOf(account.getId()));

    // 캐시 삭제
    LocalDateTime date = LocalDateTime.now();
    getCustomerCacheManager.deleteCache(instituteId, date);

    // 트랜잭션 종료 이후 캐시 갱신 메세지 큐 발행
    UpdateCustomersCacheEvent event = customerMapper.toUpdateCustomersCacheEvent(instituteId, date);
    applicationEventPublisher.publishEvent(event);

    return customerReader.findByIdAndInstituteId(customersId, instituteId).getStatus();
  }

  @Transactional
  public UpdateCustomerDto.Response updateCustomer(UpdateCustomerDto.Request req, MultipartFile file) {

    Long accountId = authProvider.getCurrentAccountId();
    Long instituteId = authProvider.getCurrentInstituteId();

    // 회원 조회
    Customer customer = customerReader.findByIdAndInstituteId(req.getCustomerId(), instituteId);

    // 사진 데이터가 존재한다면, 이를 업로드하고 URL 을 반환 받음
    String photoUrl = customer.getPhotoUrl();
    if (file != null) photoUrl = customerPhotoManger.update(customer, file);

    // 회원 정보 수정
    Customer updateCustomer = customerUpdater.updateCustomer(req, photoUrl, customer, accountId);

    // 요청 값의 진도표 검증
    List<Long> ids = progressExtractor.extractProgressIds(req.getProgressList());
    progressReader.findByIdAndCustomerId(ids, customer.getId());

    // 진도표 수정
    progressUpdater.updateProgress(req.getProgressList(), accountId);

    // 진도표 전체 조회
    List<Progress> updateProgress = progressReader.findByCustomerIdAndDesc(customer.getId());

    // 캐시 삭제
    LocalDateTime date = LocalDateTime.now();
    getCustomerCacheManager.deleteCache(instituteId, date);

    // 트랜잭션 종료 이후 캐시 갱신 메세지 큐 발행
    UpdateCustomersCacheEvent event = customerMapper.toUpdateCustomersCacheEvent(instituteId, date);
    applicationEventPublisher.publishEvent(event);

    return customerMapper.entityToUpdateCustomerResponse(updateCustomer, updateProgress);
  }

  // note. 2중 if문 처리 필요
  public List<GetCustomerDto.Response> getCustomers(Long lastId, CustomerStatus status) {
    Long instituteId = authProvider.getCurrentInstituteId();

    // 첫 번째 페이지 요청이며 매장의 캐시가 존재할시, 이를 반환
    if (lastId == null) {
      List<GetCustomerDto.Response> cache = getCustomerCacheManager.findByInstituteId(instituteId);
      if (!cache.isEmpty()) return cache;
    }

    // 고객 정보 조회
    List<GetCustomersProjection.Customer> customers = customerReader.findCustomersAfter(
        instituteId, lastId, status, PAGE_SIZE
    );

    // CustomerID 추출
    List<Long> customerIds = customerExtractor.getIds(customers);

    // 예약 정보 조회
    List<GetCustomersProjection.Reservation> reservations = reservationReader.findByCustomerIds(
        customerIds
    );

    return customerParser.getCustomers(customers, reservations);
  }
//  public List<GetCustomerDto.Response> getCustomers(Long lastId, CustomerStatus status) {
//    Institute institute = authProvider.getCurrentInstitute();
//    Long instituteId = institute.getId();
//
//    // 첫 페이지가 아닐 경우 DB 에 접근해 데이터 반환
//    if (lastId != null) {
//      return customerReader.findAllAfterLastId(instituteId, lastId, status, PAGE_SIZE);
//    }
//    // 캐시가 존재할 경우 이를 활용해 데이터 조회
//    List<GetCustomerDto.Response> response = new ArrayList<>(reservationCacheManager.getCustomers(instituteId));
//
//    // 캐시 데이터가 충분한 경우 반환
//    if (response.size() == PAGE_SIZE) return response;
//
//    // 캐시 데이터가 존재하지 않으면 가장 최근에 저장된 고객의 ID 값을 조회
//    if (response.isEmpty()) {lastId = customerReader.findTopIdByInstituteId(instituteId) + 1;}
//    // 캐시 데이터가 존재하지만 최대 반환 수에 미치지 못 하는 경우 캐시 데이터 중 가장 작은 고객 ID 값을 조회
//    else if (response.size() < PAGE_SIZE) {
//      lastId = response.stream()
//          .map(GetCustomerDto.Response::getCustomerId)
//          .min(Long::compare)
//          .orElse(lastId);
//    }
//
//    // 부족한 데이터 조회
//    List<GetCustomerDto.Response> add = customerReader.findAllAfterLastId(instituteId, lastId, status, PAGE_SIZE-response.size());
//    if (add != null) response.addAll(add);
//
//    return response;
//  }


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
    List<Progress> progress = progressReader.findByCustomerIdAndDesc(customerId);
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

  public void updateCustomersCache(UpdateCustomersCacheMessageDto dto) {
    Long instituteId = dto.getInstituteId();

    // 고객 정보 조회
    List<GetCustomersProjection.Customer> customers = customerReader.findCustomersAfter(
        instituteId, null, CustomerStatus.ACTIVE, PAGE_SIZE
    );

    // CustomerID 추출
    List<Long> customerIds = customerExtractor.getIds(customers);

    // 예약 정보 조회
    List<GetCustomersProjection.Reservation> reservations = reservationReader.findByCustomerIds(
        customerIds
    );

    List<GetCustomerDto.Response> getCustomers =  customerParser.getCustomers(
        customers, reservations
    );

    // 저장 되어있는 캐시의 타임 스탬프보다 이후 데이터면 갱신
    getCustomerCacheManager.updateCacheWithTime(instituteId, getCustomers, dto.getTime());
  }
}
