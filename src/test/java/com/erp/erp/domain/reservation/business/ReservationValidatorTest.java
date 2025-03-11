package com.erp.erp.domain.reservation.business;

import com.erp.erp.domain.reservation.repository.ReservationRepository;
import com.erp.erp.global.test.ServiceTest;
import org.mockito.InjectMocks;
import org.mockito.Mock;

class ReservationValidatorTest extends ServiceTest {

  @InjectMocks
  private ReservationValidator reservationValidator;

  @Mock
  private ReservationRepository reservationRepository;

}