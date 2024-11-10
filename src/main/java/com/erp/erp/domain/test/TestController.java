package com.erp.erp.domain.test;

import com.erp.erp.global.error.ApiResult;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
@RequiredArgsConstructor
@Slf4j
public class TestController {

  @GetMapping("/{status}")
  @Schema(name = "/테스트")
  public ApiResult<String> test(@PathVariable String status) {
    if (status.equals("success")) return ApiResult.success("success");
    return ApiResult.fail("코드","메세지","데이터");
  }

  @GetMapping("/exception/{name}")
  public void exception(){
    throw new RuntimeException("default");
  }

}
