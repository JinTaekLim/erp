package com.erp.erp.domain.progress.business;

import com.erp.erp.domain.progress.common.entity.Progress;
import com.erp.erp.domain.progress.common.exception.NotFoundProgressException;
import com.erp.erp.domain.progress.repository.ProgressRepository;
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
    progress.sort(Comparator.comparing(Progress::getDate).reversed());
    return progress;
  }

  public List<Progress> findByIdAndCustomerId(List<Long> ids, Long customerId) {
    List<Progress> progress = progressRepository.findAllByIdInAndCustomerId(ids, customerId);
    if (progress.size() != ids.size()) throw new NotFoundProgressException();
    return progress;
  }

}
