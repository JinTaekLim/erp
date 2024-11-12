package com.erp.erp.domain.admins.controller;


import com.erp.erp.domain.membership.common.dto.AddMembershipDto;
import com.erp.erp.domain.membership.common.entity.Membership;
import com.erp.erp.domain.membership.service.MembershipService;
import com.erp.erp.global.error.ApiResult;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
@Tag(name = "admin", description = "관리자용")
@RequiredArgsConstructor
@Slf4j
public class adminsController {

  private final MembershipService membershipService;

  @PostMapping("/addMembership")
  public ApiResult<AddMembershipDto.Response> addMembership(
      @RequestBody @Valid AddMembershipDto.Request request
  ) {
    Membership membership = membershipService.addMembership(request);

    AddMembershipDto.Response response = AddMembershipDto.Response.builder()
        .name(membership.getName())
        .price(membership.getPrice())
        .build();

    return ApiResult.success(response);
  }

}
