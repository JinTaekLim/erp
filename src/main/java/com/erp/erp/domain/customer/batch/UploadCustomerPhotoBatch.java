package com.erp.erp.domain.customer.batch;

import com.erp.erp.domain.customer.common.entity.Customer;
import com.erp.erp.domain.customer.common.entity.CustomerPhoto;
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
public class UploadCustomerPhotoBatch {

  private final ItemReader<CustomerPhoto> itemReader;
  private final ItemProcessor<CustomerPhoto, Customer> itemProcessor;
  private final ItemWriter<Customer> itemWriter;

  private final JobRepository jobRepository;
  private final PlatformTransactionManager transactionManager;

  @Bean
  public Job uploadTemporaryPhotoJob() {
    return new JobBuilder("uploadTemporaryPhotoJob", jobRepository)
        .start(uploadTemporaryPhotoStep())
        .build();
  }

  @Bean
  public Step uploadTemporaryPhotoStep() {
    return new StepBuilder("uploadTemporaryPhotoStep", jobRepository)
        .<CustomerPhoto, Customer>chunk(3, transactionManager)
        .reader(itemReader)
        .processor(itemProcessor)
        .writer(itemWriter)
        .build();
  }
}
