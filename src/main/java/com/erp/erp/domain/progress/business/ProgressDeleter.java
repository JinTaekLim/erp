package com.erp.erp.domain.progress.business;

import com.erp.erp.domain.progress.common.entity.Progress;
import com.erp.erp.domain.progress.repository.ProgressRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProgressDeleter {

  private final ProgressRepository progressRepository;

  public void deleteAllByCustomerId(Long customerId) {
    progressRepository.deleteAllByCustomerId(customerId);
  }

  public void deleteByReservationId(Long reservationId) {
    progressRepository.deleteByReservationId(reservationId);
  }

  public void deleteAll(List<Progress> progressList) {
    progressRepository.deleteAll(progressList);
  }

  public void deleteAllById(List<Long> ids) {
    progressRepository.deleteAllById(ids);
  }

}
