package com.erp.erp.global.util.test;

import com.erp.erp.global.gson.LocalDateSerializer;
import com.erp.erp.global.gson.LocalDateTimeAdapter;
import com.erp.erp.global.gson.LocalDateTimeSerializer;
import com.erp.erp.global.gson.LocalTimeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.navercorp.fixturemonkey.FixtureMonkey;
import com.navercorp.fixturemonkey.api.introspector.FieldReflectionArbitraryIntrospector;
import com.navercorp.fixturemonkey.jakarta.validation.plugin.JakartaValidationPlugin;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
abstract public class IntegrationTest {

  protected MockMvc mvc;

  protected Gson gson = new GsonBuilder()
      .registerTypeAdapter(LocalDate.class, new LocalDateSerializer())
      .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeSerializer())
      .registerTypeAdapter(LocalTime.class, new LocalTimeAdapter())
      .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
      .setPrettyPrinting()
      .create();

  protected FixtureMonkey fixtureMonkey = FixtureMonkey.builder()
      .objectIntrospector(FieldReflectionArbitraryIntrospector.INSTANCE)
      .plugin(new JakartaValidationPlugin())
      .build();

  @Autowired
  private WebApplicationContext context;

  @BeforeEach
  void setUp() {
    mvc = MockMvcBuilders.webAppContextSetup(context).build();
  }

}
