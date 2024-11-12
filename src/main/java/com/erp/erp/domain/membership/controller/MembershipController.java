package com.erp.erp.domain.membership.controller;


import com.erp.erp.domain.membership.common.dto.GetMembershipDto;
import com.erp.erp.domain.membership.common.entity.Membership;
import com.erp.erp.domain.membership.service.MembershipService;
import com.erp.erp.global.error.ApiResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/membership")
@Tag(name = "membership", description = "이용권 관리")
@RequiredArgsConstructor
@Slf4j
public class MembershipController {

  private final MembershipService membershipService;

  @Operation(summary = "이용권 전체 조회")
  @GetMapping("/getAllMemberships")
  public ApiResult<List<GetMembershipDto.Response>> getAllMemberships() {

    List<Membership> membershipList = membershipService.getAllMemberships();

    List<GetMembershipDto.Response> responses = membershipList.stream()
        .map(membership -> GetMembershipDto.Response.builder()
            .id(membership.getId())
            .name(membership.getName())
            .price(membership.getPrice())
            .build()
        ).toList();

    return ApiResult.success(responses);
  }

}
