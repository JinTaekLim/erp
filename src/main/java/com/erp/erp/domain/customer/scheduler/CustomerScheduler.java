package com.erp.erp.domain.customer.scheduler;

import com.erp.erp.domain.batch.service.BatchBrokerService;
import com.erp.erp.domain.batch.service.BatchConsumerService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomerScheduler {

  private final BatchBrokerService batchBrokerService;
  private final BatchConsumerService batchConsumerService;

  // 매일 3시 임시 저장된 사진 업로드
  @Scheduled(cron = "0 0 3 * * ?")
  public void uploadTemporaryPhoto() {
    batchBrokerService.sendUploadTemporaryPhoto();
  }


  // 매정각 만료일자가 지난 회원의 상태 값을 변경
  @Scheduled(cron = "0 0 0 * * ?")
  public void updateStatus() {
    batchConsumerService.updateStatus();
  }

  // 매정각 1주일 전 등록한 모든 회원의 정보를 가져오고, 첫 예약 날짜부터 혹은 현재 날짜 기준으로 만료 기간 설정
  @Scheduled(cron = "0 0 0 * * ?")
  public void updateExpiredAt() {
    batchConsumerService.updateExpiredAt();
  }

}
