package com.erp.erp.domain.progress.business;

import com.erp.erp.domain.customer.common.dto.UpdateCustomerDto;
import com.erp.erp.domain.progress.common.entity.Progress;
import com.erp.erp.domain.progress.repository.ProgressRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProgressUpdater {

  private final ProgressRepository progressRepository;

  // note. 이후 로직 변경 예정
  public void updateProgress(List<UpdateCustomerDto.ProgressRequest> req, Long accountId) {

    req.forEach(p -> {
      Progress progress = progressRepository.findById(p.getProgressId()).orElse(null);
      progress.update(p.getContent(), accountId.toString());
      progressRepository.save(progress);
    });

  }
}
