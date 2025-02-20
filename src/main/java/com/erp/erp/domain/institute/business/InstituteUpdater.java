package com.erp.erp.domain.institute.business;


import com.erp.erp.domain.institute.common.entity.Institute;
import com.erp.erp.domain.institute.repository.InstituteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Slf4j
@RequiredArgsConstructor
public class InstituteUpdater {
  private final InstituteRepository instituteRepository;

  @Transactional
  public Institute updateSpotsNumber(Institute institute, int num, String updatedId) {
    institute.changeTotalSpots(num, updatedId);
    instituteRepository.save(institute);
    return institute;
  }
}
