package com.erp.erp.domain.institute.service;

import com.erp.erp.domain.admin.common.dto.AddInstituteDto;
import com.erp.erp.domain.auth.business.AuthProvider;
import com.erp.erp.domain.institute.business.InstituteCreator;
import com.erp.erp.domain.institute.business.InstituteUpdater;
import com.erp.erp.domain.institute.common.dto.GetInstituteInfoDto;
import com.erp.erp.domain.institute.common.dto.UpdateTotalSeatDto;
import com.erp.erp.domain.institute.common.entity.Institute;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class InstituteService {

  private final AuthProvider authProvider;
  private final InstituteCreator instituteCreator;
  private final InstituteUpdater instituteUpdater;

  public UpdateTotalSeatDto.Response updateTotalSpots(UpdateTotalSeatDto.Request req) {
    Institute institute = authProvider.getCurrentInstitute();
    instituteUpdater.updateSpotsNumber(institute, req.getTotalSeat());
    return UpdateTotalSeatDto.Response.fromEntity(institute);
  }

  public AddInstituteDto.Response addInstitute(AddInstituteDto.Request req) {
    Institute institute = req.toEntity();
    instituteCreator.save(institute);
    return AddInstituteDto.Response.fromEntity(institute);
  }

  public GetInstituteInfoDto.Response getInfo() {
    Institute institute = authProvider.getCurrentInstitute();
    return GetInstituteInfoDto.Response.fromEntity(institute);
  }
}
