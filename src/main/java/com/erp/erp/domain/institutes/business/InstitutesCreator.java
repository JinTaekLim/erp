package com.erp.erp.domain.institutes.business;


import com.erp.erp.domain.institutes.common.entity.Institutes;
import com.erp.erp.domain.institutes.repository.InstitutesRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class InstitutesCreator {
  private final InstitutesRepository institutesRepository;

  public Institutes save(Institutes institutes) {
    return institutesRepository.save(institutes);
  }
}
