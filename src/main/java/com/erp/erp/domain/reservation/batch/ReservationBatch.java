package com.erp.erp.domain.reservation.batch;

import com.erp.erp.domain.reservation.common.dto.ComparisonGetCustomerDto;
import com.erp.erp.domain.reservation.common.dto.ReservationCache;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
public class ReservationBatch {

  private final ItemReader<ComparisonGetCustomerDto> itemReader;
  private final ItemProcessor<ComparisonGetCustomerDto, List<ReservationCache>> itemProcessor;
  private final ItemWriter<List<ReservationCache>> itemWriter;

  private final JobRepository jobRepository;
  private final PlatformTransactionManager transactionManager;

  @Bean
  public Job reservationUpdateJob() {
    return new JobBuilder("reservationUpdateJob", jobRepository)
        .start(reservationUpdateStep())
        .build();
  }

  @Bean
  public Step reservationUpdateStep() {
    return new StepBuilder("reservationUpdateJob", jobRepository)
        .<ComparisonGetCustomerDto, List<ReservationCache>>chunk(3, transactionManager)
        .reader(itemReader)
        .processor(itemProcessor)
        .writer(itemWriter)
        .build();
  }
}
