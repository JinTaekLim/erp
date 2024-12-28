package com.erp.erp.domain.reservation.repository;

import com.erp.erp.domain.reservation.common.entity.Reservation;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<Reservation, Long>, ReservationRepositoryCustom{

  Optional<Reservation> findByIdAndInstituteId(Long id, Long instituteId);

}
