package com.erp.erp.domain.institutes.business;


import com.erp.erp.domain.institutes.common.entity.Institutes;
import com.erp.erp.domain.institutes.repository.InstitutesRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Slf4j
@RequiredArgsConstructor
public class InstitutesUpdater {
  private final InstitutesRepository institutesRepository;

  @Transactional
  public Institutes updateSpotsNumber(Institutes institutes, int num) {
    institutes.changeTotalSpots(num);
    return institutes;
  }
}
