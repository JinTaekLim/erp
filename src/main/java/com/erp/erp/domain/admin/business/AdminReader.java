package com.erp.erp.domain.admin.business;

import com.erp.erp.domain.admin.common.dto.LoginDto;
import com.erp.erp.domain.admin.common.entity.Admin;
import com.erp.erp.domain.admin.repository.AdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminReader {

  private final AdminRepository adminRepository;

  public Admin findByIdentifierAndPassword(LoginDto.Request req) {
    return adminRepository.findByIdentifierAndPassword(req.getIdentifier(), req.getPassword())
        .orElseThrow(IllegalArgumentException::new);
  }

}
