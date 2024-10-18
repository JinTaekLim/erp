package com.erp.erp.domain.institutes.controller;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(InstitutesController.class)
class InstitutesControllerTest {

  @Autowired
  private MockMvc mockMvc;
  @Test
  void updateTotalSpots() {
  }
}