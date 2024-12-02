package com.erp.erp.domain.admins;

import com.erp.erp.domain.plans.common.dto.AddPlansDto;
import com.erp.erp.domain.plans.common.entity.LicenseType;
import com.erp.erp.domain.plans.repository.PlansRepository;
import com.erp.erp.global.error.ApiResult;
import com.erp.erp.global.util.randomValue.RandomValue;
import com.erp.erp.global.util.test.IntegrationTest;
import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class adminsTest extends IntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;


    @Test
    void addPlans() {
        //given
        AddPlansDto.Request request = fixtureMonkey.giveMeBuilder(AddPlansDto.Request.class)
                .set("licenseType", RandomValue.getRandomEnum(LicenseType.class))
                .sample();

        String url = "http://localhost:" + port + "/api/admin/addPlans";

        //when
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(
                url,
                request,
                String.class
        );

        ApiResult<AddPlansDto.Response> apiResponse = gson.fromJson(
                responseEntity.getBody(),
                new TypeToken<ApiResult<AddPlansDto.Response>>(){}
        );

        //then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertNotNull(apiResponse);
        assertEquals(apiResponse.getData().getLicenseType(), request.getLicenseType());
        assertEquals(apiResponse.getData().getPrice(), request.getPrice());
        assertEquals(apiResponse.getData().getName(), request.getName());
    }
}