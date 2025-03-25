package com.erp.erp.global.test;

import com.erp.erp.global.cleaner.DataCleaner;
import com.erp.erp.global.container.RedisContainer;
import com.google.gson.Gson;
import com.navercorp.fixturemonkey.FixtureMonkey;
import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Import(RedisContainer.class)
abstract public class IntegrationTest {

  @LocalServerPort
  protected int port;

  @Autowired
  protected TestRestTemplate restTemplate;

  @Autowired
  private DataCleaner dataCleaner;

  @Autowired
  protected Gson gson;

  @Autowired
  protected FixtureMonkey fixtureMonkey;

  @AfterEach
  void tearDown() {
    dataCleaner.clearAll();
  }
}
