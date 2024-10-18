package com.erp.erp.domain.institutes.business;


import com.erp.erp.domain.institutes.common.entity.Institutes;
import com.erp.erp.domain.institutes.repository.InstitutesRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class InstitutesReader {
  private final InstitutesRepository institutesRepository;
//
//  public Institutes findById(Long id) {
//    return institutesRepository.findById(id).orElseThrow();
//  }
}
