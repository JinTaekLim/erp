package com.erp.erp.domain.customer.common.entity;

import java.time.LocalDate;
import java.util.List;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "progress")
@Getter
@NoArgsConstructor
public class Progress {

  @Id
  private Long customerId;

  private List<ProgressItem> progressList;

  @Getter
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class ProgressItem {

    @NotNull
    private LocalDate date;
    @NotNull
    private String content;
  }

  @Builder
  public Progress(Long customerId, List<ProgressItem> progressList) {
    this.customerId = customerId;
    this.progressList = progressList;
  }
}
