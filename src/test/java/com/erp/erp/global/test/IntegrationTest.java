package com.erp.erp.global.test;

import com.erp.erp.global.fixtureMonkey.LocalDateTimeJqwikPlugin;
import com.erp.erp.global.gson.LocalDateSerializer;
import com.erp.erp.global.gson.LocalDateTimeAdapter;
import com.erp.erp.global.gson.LocalTimeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.navercorp.fixturemonkey.FixtureMonkey;
import com.navercorp.fixturemonkey.api.introspector.BuilderArbitraryIntrospector;
import com.navercorp.fixturemonkey.api.jqwik.JqwikPlugin;
import com.navercorp.fixturemonkey.jakarta.validation.plugin.JakartaValidationPlugin;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
abstract public class IntegrationTest {

  @LocalServerPort
  protected int port;

  @Autowired
  protected TestRestTemplate restTemplate;

  protected MockMvc mvc;

  @Autowired
  private WebApplicationContext context;

  @Autowired
  private DatabaseCleaner databaseCleaner;

  protected Gson gson = new GsonBuilder()
      .registerTypeAdapter(LocalDate.class, new LocalDateSerializer())
      .registerTypeAdapter(LocalTime.class, new LocalTimeAdapter())
      .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
      .setPrettyPrinting()
      .create();

  protected FixtureMonkey fixtureMonkey = FixtureMonkey.builder()
      .objectIntrospector(BuilderArbitraryIntrospector.INSTANCE)
      .plugin(new JakartaValidationPlugin())
      .plugin(new JqwikPlugin().javaTimeTypeArbitraryGenerator(new LocalDateTimeJqwikPlugin()))
      .build();

  @BeforeEach
  void setUp() {mvc = MockMvcBuilders.webAppContextSetup(context).build();}

  @AfterEach
  void tearDown() {
    databaseCleaner.execute();
  }

  @BeforeAll
  static void setUpAll() throws IOException {
    EmbeddedServer.startRedis();
  }

  @AfterAll
  static void tearDownAll() throws IOException {
    EmbeddedServer.stopRedis();
  }
}
