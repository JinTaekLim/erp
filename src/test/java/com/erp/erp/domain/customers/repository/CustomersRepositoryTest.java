package com.erp.erp.domain.customers.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.erp.erp.domain.customers.common.entity.Customers;
import com.erp.erp.domain.institutes.common.entity.Institutes;
import com.erp.erp.domain.institutes.repository.InstitutesRepository;
import com.erp.erp.global.util.test.JpaTest;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class CustomersRepositoryTest extends JpaTest {

  @Autowired
  private CustomersRepository customersRepository;
  @Autowired
  private InstitutesRepository institutesRepository;


  private Institutes getInstitutes() {
    return fixtureMonkey.giveMeBuilder(Institutes.class)
        .setNull("id")
        .sample();
  }

  private Institutes createInstitutes() {
    return institutesRepository.save(getInstitutes());
  }

  private Customers getCustomers() {
    Institutes institutes = createInstitutes();

    return fixtureMonkey.giveMeBuilder(Customers.class)
        .setNull("id")
        .set("institutes", institutes)
        .sample();
  }

  @Test
  void findAll() {
    // Given
    int randomInt = faker.random().nextInt(0, 100);
    List<Customers> customersToSave = new ArrayList<>();

    for (int i = 0; i < randomInt; i++) {
      customersToSave.add(getCustomers());
    }

    customersRepository.saveAll(customersToSave);

    // When
    List<Customers> customersList = customersRepository.findAll();

    // Then
    assertThat(customersList).hasSize(randomInt);
  }

  @Test
  void save() {
    // Given
    Customers customers = getCustomers();
    customersRepository.save(customers);

    // Then
    Long customersId = customers.getId();
    Customers testCustomers = customersRepository.findById(customersId)
        .orElseThrow(AssertionError::new);

    // When
    assertThat(testCustomers)
        .usingRecursiveComparison()
        .isEqualTo(customers);
  }
  @Test
  void updateStatusById() {
    // Given
    Customers customers = getCustomers();
    customersRepository.save(customers);

    long customersId = customers.getId();
    boolean status = !customers.getStatus();

    // Then
    customersRepository.updateStatusById(customersId, status);

    Customers testCustomers = customersRepository.findById(customersId)
        .orElseThrow(AssertionError::new);

    boolean testStatus = testCustomers.getStatus();

    // When
    assertThat(status).isNotEqualTo(testStatus);
  }
}