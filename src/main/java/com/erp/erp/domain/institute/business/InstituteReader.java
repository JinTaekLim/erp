package com.erp.erp.domain.institute.business;


import com.erp.erp.domain.institute.common.entity.Institute;
import com.erp.erp.domain.institute.common.exception.NotFoundInstituteException;
import com.erp.erp.domain.institute.repository.InstituteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class InstituteReader {
  private final InstituteRepository instituteRepository;

  public Institute findById(Long id) {
    return instituteRepository.findById(id).orElseThrow(NotFoundInstituteException::new);
  }
}
