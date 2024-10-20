package com.erp.erp.domain.customers.service;

import com.erp.erp.domain.accounts.business.PhotoUtil;
import com.erp.erp.domain.customers.common.dto.GetCustomerDto;
import com.erp.erp.domain.customers.common.entity.Gender;
import com.erp.erp.domain.customers.business.CustomersCreator;
import com.erp.erp.domain.customers.business.CustomersReader;
import com.erp.erp.domain.customers.business.CustomersUpdater;
import com.erp.erp.domain.customers.common.dto.AddCustomerDto;
import com.erp.erp.domain.customers.common.dto.UpdateStatusDto;
import com.erp.erp.domain.customers.common.entity.Customers;
import com.erp.erp.domain.institutes.common.entity.Institutes;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomersService {

  private final CustomersCreator customersCreator;
  private final CustomersReader customersReader;
  private final CustomersUpdater customersUpdater;
  private final PhotoUtil photoUtil;

  public Customers addCustomer(
      Institutes institutes ,
      AddCustomerDto.Request req
  ) {

    Gender gender = Gender.getString(req.getGender());

    MultipartFile photo = null;
    String photoUrl = (photo == null) ? null : photoUtil.upload(photo);

    Customers customers = Customers.builder()
        .institutes(institutes)
        .name(req.getName())
        .gender(gender)
        .phone(req.getPhone())
        .address(req.getAddress())
        .photoUrl(photoUrl)
        .birthDate(req.getBirthDate())
        .build();

    return customersCreator.save(customers);
  };


  // note. 본인 매장의 고객 정보만 변경할 수 있도록 별도의 처리 필요
  public Boolean updateStatus(UpdateStatusDto.Request req) {
    Long customersId = req.getCustomersId();
    Boolean status = req.getStatus();
    customersUpdater.updateStatus(customersId, status);
    return customersReader.findById(customersId).getStatus();
  }


  public List<Customers> getCurrentCustomers(Long institutesId, int page) {
    Pageable pageable = PageRequest.of(page, 4);
    Page<Customers> customersPage = customersReader.findByInstitutesIdAndStatusTrue(
        institutesId,
        pageable
    );
    return customersPage.getContent();
  }

  public List<Customers> getExpiredCustomers(Long institutesId, int page) {
    Pageable pageable = PageRequest.of(page, 4);
    Page<Customers> customersPage = customersReader.findByInstitutesIdAndStatusFalse(
        institutesId,
        pageable
    );
    return customersPage.getContent();
  }

}
