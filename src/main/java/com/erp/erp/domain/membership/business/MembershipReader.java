package com.erp.erp.domain.membership.business;

import com.erp.erp.domain.membership.common.entity.Membership;
import com.erp.erp.domain.membership.repository.MembershipRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MembershipReader {

  private final MembershipRepository membershipRepository;

  public List<Membership> findAll() {
    return membershipRepository.findAll();
  }
}
