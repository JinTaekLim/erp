package com.erp.erp.global.cleaner;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class MysqlCleaner {

  private final JdbcTemplate jdbcTemplate;
  private List<String> tableNames;

  @PersistenceContext
  private EntityManager entityManager;

  public MysqlCleaner(final JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  @PostConstruct
  public void init() {
    tableNames = jdbcTemplate.queryForList(
        "SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = 'PUBLIC'",
        String.class);

    this.clear();
  }

  @Transactional
  public void clear() {
    clearJpaCache();

    jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY FALSE");
    for (String tableName : tableNames) {
      jdbcTemplate.execute("TRUNCATE TABLE " + tableName);
    }
    jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY TRUE");
  }

  private void clearJpaCache() {
    entityManager.clear();
  }
}
