package com.erp.erp.domain.reservation.scheduler;

import com.erp.erp.domain.reservation.batch.ReservationBatch;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReservationScheduler {

  private final JobLauncher jobLauncher;
  private final ReservationBatch reservationBatch;

  // 매정각 DB 데이터와 캐시 데이터가 동일한지 확인 후 조치
  @Scheduled(cron = "0 0 0 * * ?")
  public void updateGetCustomer() throws Exception {
    jobLauncher.run(reservationBatch.reservationUpdateJob(), new JobParameters());
  }
}
