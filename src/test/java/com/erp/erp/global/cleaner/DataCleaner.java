package com.erp.erp.global.cleaner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DataCleaner {

  @Autowired
  private MysqlCleaner mysqlCleaner;

  @Autowired
  private RedisCleaner redisCleaner;

  public void clearAll() {
    mysqlCleaner.clear();
    redisCleaner.clear();
  }
}
