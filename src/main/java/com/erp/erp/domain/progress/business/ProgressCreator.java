package com.erp.erp.domain.progress.business;

import com.erp.erp.domain.progress.common.entity.Progress;
import com.erp.erp.domain.progress.repository.ProgressRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProgressCreator {

  private final ProgressRepository progressRepository;

  public List<Progress> saveAll(List<Progress> progress) {
    return progressRepository.saveAll(progress);
  }

}
