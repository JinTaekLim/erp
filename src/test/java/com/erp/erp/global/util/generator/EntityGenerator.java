package com.erp.erp.global.util.generator;

import com.erp.erp.global.util.config.fixtureMonkey.LocalDateTimeJqwikPlugin;
import com.navercorp.fixturemonkey.FixtureMonkey;
import com.navercorp.fixturemonkey.api.introspector.FieldReflectionArbitraryIntrospector;
import com.navercorp.fixturemonkey.api.jqwik.JqwikPlugin;
import com.navercorp.fixturemonkey.jakarta.validation.plugin.JakartaValidationPlugin;

public class EntityGenerator {

  protected static final FixtureMonkey fixtureMonkey = FixtureMonkey.builder()
      .objectIntrospector(FieldReflectionArbitraryIntrospector.INSTANCE)
      .plugin(new JakartaValidationPlugin())
      .plugin(new JqwikPlugin().javaTimeTypeArbitraryGenerator(new LocalDateTimeJqwikPlugin()))
      .build();
}
