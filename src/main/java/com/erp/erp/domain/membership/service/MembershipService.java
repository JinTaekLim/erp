package com.erp.erp.domain.membership.service;

import com.erp.erp.domain.membership.business.MembershipCreator;
import com.erp.erp.domain.membership.business.MembershipReader;
import com.erp.erp.domain.membership.common.dto.AddMembershipDto;
import com.erp.erp.domain.membership.common.entity.Membership;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class MembershipService {

  private final MembershipCreator membershipCreator;
  private final MembershipReader membershipReader;

  public Membership addMembership(AddMembershipDto.Request request) {

    Membership membership = Membership.builder()
        .name(request.getName())
        .price(request.getPrice())
        .build();

    return membershipCreator.save(membership);
  }

  public List<Membership> getAllMemberships() {
    return membershipReader.findAll();
  }
}
