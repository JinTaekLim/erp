package com.erp.erp.domain.customer.business;

import com.erp.erp.domain.customer.common.entity.Progress;
import com.erp.erp.domain.customer.repository.ProgressRepository;
import java.util.Comparator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProgressReader {

  private final ProgressRepository progressRepository;

  public List<Progress> findByCustomerIdAndDesc(Long id) {
    List<Progress> progress = progressRepository.findByCustomerId(id);
    progress.sort(Comparator.comparing(Progress::getId).reversed());
    return progress;
  }

}
