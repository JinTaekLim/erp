package com.erp.erp.domain.institute.service;

import com.erp.erp.domain.auth.business.AuthProvider;
import com.erp.erp.domain.institute.business.InstituteUpdater;
import com.erp.erp.domain.institute.common.dto.GetInstituteInfoDto;
import com.erp.erp.domain.institute.common.dto.UpdateTotalSeatDto;
import com.erp.erp.domain.institute.common.entity.Institute;
import com.erp.erp.domain.institute.common.mapper.InstituteMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class InstituteService {

  private final AuthProvider authProvider;
  private final InstituteUpdater instituteUpdater;
  private final InstituteMapper instituteMapper;

  public UpdateTotalSeatDto.Response updateTotalSpots(UpdateTotalSeatDto.Request req) {
    Institute institute = authProvider.getCurrentInstitute();
    instituteUpdater.updateSpotsNumber(institute, req.getTotalSeat());
    return instituteMapper.entityToUpdateTotalSeatResponse(institute);
  }

  public GetInstituteInfoDto.Response getInfo() {
    Institute institute = authProvider.getCurrentInstitute();
    return instituteMapper.entityToGetInstituteInfoDtoResponse(institute);
  }
}
