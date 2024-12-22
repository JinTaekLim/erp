package com.erp.erp.domain.institutes.service;

import com.erp.erp.domain.admin.common.dto.AddInstituteDto;
import com.erp.erp.domain.auth.business.AuthProvider;
import com.erp.erp.domain.institutes.business.InstitutesCreator;
import com.erp.erp.domain.institutes.business.InstitutesUpdater;
import com.erp.erp.domain.institutes.common.dto.UpdateTotalSpotsDto;
import com.erp.erp.domain.institutes.common.entity.Institutes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class InstitutesService {

  private final AuthProvider authProvider;
  private final InstitutesCreator institutesCreator;
  private final InstitutesUpdater institutesUpdater;

  public UpdateTotalSpotsDto.Response updateTotalSpots(UpdateTotalSpotsDto.Request req) {
    Institutes institutes = authProvider.getCurrentInstitute();
    int num = req.getNum();
    institutesUpdater.updateSpotsNumber(institutes, num);

    return UpdateTotalSpotsDto.Response.builder()
        .id(institutes.getId())
        .name(institutes.getName())
        .num(institutes.getTotalSpots())
        .build();
  }

  public AddInstituteDto.Response addInstitute(AddInstituteDto.Request req) {
    Institutes institutes = req.toEntity();
    institutesCreator.save(institutes);
    return AddInstituteDto.Response.fromEntity(institutes);
  }
}
