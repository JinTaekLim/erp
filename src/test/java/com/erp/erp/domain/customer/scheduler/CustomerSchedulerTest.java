package com.erp.erp.domain.customer.scheduler;

import com.erp.erp.domain.customer.common.entity.Customer;
import com.erp.erp.domain.customer.common.entity.CustomerPhoto;
import com.erp.erp.domain.customer.repository.CustomerPhotoRepository;
import com.erp.erp.domain.customer.repository.CustomerRepository;
import com.erp.erp.domain.institute.common.entity.Institute;
import com.erp.erp.domain.institute.repository.InstituteRepository;
import com.erp.erp.domain.plan.common.entity.Plan;
import com.erp.erp.domain.plan.repository.PlanRepository;
import com.erp.erp.global.test.IntegrationTest;
import com.erp.erp.global.util.ReflectionUtil;
import com.erp.erp.global.util.S3Manager;
import com.erp.erp.global.util.generator.CustomerGenerator;
import com.erp.erp.global.util.generator.CustomerPhotoGenerator;
import com.erp.erp.global.util.generator.InstituteGenerator;
import com.erp.erp.global.util.generator.PlanGenerator;
import com.erp.erp.global.util.randomValue.RandomValue;
import java.io.InputStream;
import java.util.List;
import java.util.stream.IntStream;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

// note. updateStatus / updateExpiredAt 테스트 코드 필요
public class CustomerSchedulerTest extends IntegrationTest {

  @Autowired
  private CustomerScheduler customerScheduler;
  @Autowired
  private InstituteRepository instituteRepository;
  @Autowired
  private PlanRepository planRepository;
  @Autowired
  private CustomerRepository customerRepository;
  @Autowired
  private CustomerPhotoRepository customerPhotoRepository;
  @MockBean
  private S3Manager s3Manager;

  private Institute createInstitute() {
    return instituteRepository.save(InstituteGenerator.get());
  }

  private Plan createPlan() {
    return planRepository.save(PlanGenerator.get());
  }


  private Customer createCustomer(Plan plan, Institute institute) {
    return customerRepository.save(CustomerGenerator.get(plan, institute));
  }

  private CustomerPhoto createCustomerPhoto(Customer customer) {
    return customerPhotoRepository.save(CustomerPhotoGenerator.get(customer));
  }

  @Test
  void uploadTemporaryPhotoJob() throws Exception {
    // given
    Institute institute = createInstitute();
    Plan plan = createPlan();

    int customerSize = RandomValue.getInt(1,5);
    List<Customer> customerList = IntStream.range(0, customerSize)
        .mapToObj(i -> {
          Customer customer = createCustomer(plan, institute);
          ReflectionUtil.setFieldValue(customer, "photoUrl", null);
          return customer;
        }).toList();

    customerList.stream().map(this::createCustomerPhoto).toList();

    String photoUrl = "url";

    // when
    Mockito.when(s3Manager.upload((InputStream) any())).thenReturn(photoUrl);
    customerScheduler.uploadTemporaryPhotoJob();

    List<Customer> customers = customerRepository.findAll();
    List<CustomerPhoto> customerPhotoList = customerPhotoRepository.findAll();

    // then
    assertThat(customerPhotoList.size()).isEqualTo(0);

    for(Customer customer : customers) {
      assertThat(customer.getPhotoUrl()).isEqualTo(photoUrl);
    }
  }
}
