package com.erp.erp.domain.customer.scheduler;

import com.erp.erp.domain.customer.business.CustomerPhotoDeleter;
import com.erp.erp.domain.customer.business.CustomerPhotoReader;
import com.erp.erp.domain.customer.business.CustomerReader;
import com.erp.erp.domain.customer.business.CustomerUpdater;
import com.erp.erp.domain.customer.common.dto.UpdateCustomerExpiredAtDto;
import com.erp.erp.domain.customer.common.entity.CustomerPhoto;
import com.erp.erp.domain.customer.common.entity.CustomerStatus;
import com.erp.erp.global.error.exception.ServerException;
import com.erp.erp.global.util.S3Manager;
import java.io.ByteArrayInputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomerScheduler {

  private final CustomerReader customerReader;
  private final CustomerUpdater customerUpdater;
  private final S3Manager s3Manager;
  private final CustomerPhotoReader customerPhotoReader;
  private final CustomerPhotoDeleter customerPhotoDeleter;

  private final int BEFORE_DAY = 7;
  private final String UPDATE_ID = "SERVER";


  // 매일 3시 임시 저장된 사진 업로드
  @Scheduled(cron = "0 0 3 * * ?")
  public void uploadPhoto() {
    s3Manager.bucketExists();
    List<CustomerPhoto> customerPhotos = customerPhotoReader.findAll();

    for (CustomerPhoto customerPhoto : customerPhotos) {

      try {
        ByteArrayInputStream file = new ByteArrayInputStream(customerPhoto.getData());
        s3Manager.upload(file);
        customerPhotoDeleter.delete(customerPhoto);
      } catch (Exception e) {
        throw new ServerException("업로드 실패 [ID:" + customerPhoto.getId() + "]" + e.getMessage());
      }

    }

  }

  // 매정각 만료일자가 지난 회원의 상태 값을 변경
  @Scheduled(cron = "0 0 0 * * ?")
  public void updateStatus() {
    List<Long> ids = customerReader.findIdsCreatedAtBeforeDaysAgo(LocalDate.now());

    for (Long id : ids) {
      customerUpdater.updateStatus(id, CustomerStatus.ACTIVE, UPDATE_ID);
    }
  }

  // 매정각 1주일 전 등록한 모든 회원의 정보를 가져오고, 첫 예약 날짜부터 혹은 현재 날짜 기준으로 만료 기간 설정
  @Scheduled(cron = "0 0 0 * * ?")
  public void updateExpiredAt() {
    List<UpdateCustomerExpiredAtDto> customers = customerReader.findCustomersCreatedAtOnDaysAgo(BEFORE_DAY);
    List<UpdateCustomerExpiredAtDto.Request> requests = createExpiredRequests(customers);

    if (!requests.isEmpty()) {
      customerUpdater.updateExpiredAt(requests);
    }
  }

  private List<UpdateCustomerExpiredAtDto.Request> createExpiredRequests(List<UpdateCustomerExpiredAtDto> customers) {
    return customers.stream()
        .map(this::createRequest)
        .toList();
  }

  private UpdateCustomerExpiredAtDto.Request createRequest(UpdateCustomerExpiredAtDto customer) {
    LocalDate start = Optional.ofNullable(customer.getFirstReservationDate())
        .map(LocalDateTime::toLocalDate)
        .orElse(LocalDate.now());

    LocalDate expiredAt = start.plusDays(customer.getAvailablePeriod());

    return UpdateCustomerExpiredAtDto.Request.builder()
        .customerId(customer.getId())
        .expiredAt(expiredAt)
        .build();
  }

}
