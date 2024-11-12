package com.erp.erp.domain.membership.business;

import com.erp.erp.domain.membership.common.entity.Membership;
import com.erp.erp.domain.membership.repository.MembershipRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MembershipCreator {

  private final MembershipRepository membershipRepository;

  public Membership save(Membership membership) {
    return membershipRepository.save(membership);
  }
}
