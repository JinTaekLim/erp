package com.erp.erp.global.test;


import com.erp.erp.global.config.QuerydslConfig;
import com.erp.erp.global.util.config.fixtureMonkey.FixtureMonkeyConfig;
import com.navercorp.fixturemonkey.FixtureMonkey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
@Import({QuerydslConfig.class, FixtureMonkeyConfig.class})
public class JpaTest {

  @Autowired
  protected TestEntityManager entityManager;

  @Autowired
  protected FixtureMonkey fixtureMonkey;

}
