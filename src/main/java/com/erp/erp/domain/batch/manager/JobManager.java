package com.erp.erp.domain.batch.manager;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JobManager {

  private final JobLauncher jobLauncher;

  public JobParameters getJobParameters(LocalDateTime localDateTime) {
    return new JobParametersBuilder()
        .addLocalDateTime("localDateTime", localDateTime)
        .toJobParameters();
  }

  public void runJob(Job job, JobParameters jobParameters) {
    try {
      jobLauncher.run(job, jobParameters);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

}
