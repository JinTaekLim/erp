package com.erp.erp.global.util.config.fixtureMonkey;

import com.navercorp.fixturemonkey.FixtureMonkey;
import com.navercorp.fixturemonkey.api.introspector.BuilderArbitraryIntrospector;
import com.navercorp.fixturemonkey.api.jqwik.JqwikPlugin;
import com.navercorp.fixturemonkey.jakarta.validation.plugin.JakartaValidationPlugin;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FixtureMonkeyConfig {

  @Bean
  public FixtureMonkey fixtureMonkey() {
    return FixtureMonkey.builder()
        .objectIntrospector(BuilderArbitraryIntrospector.INSTANCE)
        .plugin(new JakartaValidationPlugin())
        .plugin(new JqwikPlugin().javaTimeTypeArbitraryGenerator(new LocalDateTimeJqwikPlugin()))
        .build();
  }
}
