package com.erp.erp.domain.batch.service;

import com.erp.erp.domain.batch.common.dto.UploadCustomerPhotoMessage;
import com.erp.erp.domain.batch.manager.BatchManager;
import com.erp.erp.domain.batch.manager.JobManager;
import com.erp.erp.domain.customer.business.CustomerReader;
import com.erp.erp.domain.customer.business.CustomerUpdater;
import com.erp.erp.domain.customer.common.dto.UpdateCustomerExpiredAtDto;
import com.erp.erp.domain.customer.common.entity.CustomerStatus;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BatchConsumerService {

  private final JobManager jobManager;
  private final BatchManager batchManager;

  public void uploadCustomerPhotoBatch(UploadCustomerPhotoMessage dto) {
    JobParameters params = jobManager.getJobParameters(dto.getTime());
    Job job = batchManager.getUploadCustomerPhotoBatch();
    jobManager.runJob(job, params);
  }


  // note. 이후 메세지 큐 기반 배치로 리펙토링 예정
  private final CustomerReader customerReader;
  private final CustomerUpdater customerUpdater;

  private final int BEFORE_DAY = 7;
  private final String UPDATE_ID = "SERVER";

  public void updateStatus() {
    List<Long> ids = customerReader.findIdsCreatedAtBeforeDaysAgo(LocalDate.now());

    for (Long id : ids) {
      customerUpdater.updateStatus(id, CustomerStatus.ACTIVE, UPDATE_ID);
    }
  }

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
        .orElse(LocalDate.now());

    LocalDate expiredAt = start.plusDays(customer.getAvailablePeriod());

    return UpdateCustomerExpiredAtDto.Request.builder()
        .customerId(customer.getId())
        .expiredAt(expiredAt)
        .build();
  }

}
