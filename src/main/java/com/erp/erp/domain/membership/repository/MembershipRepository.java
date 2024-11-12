package com.erp.erp.domain.membership.repository;

import com.erp.erp.domain.membership.common.entity.Membership;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MembershipRepository extends JpaRepository<Membership, Long> {

}
