package com.erp.erp.domain.reservation.repository;

import com.erp.erp.domain.institute.common.entity.Institute;
import com.erp.erp.domain.reservation.common.entity.Reservation;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

  @Query("SELECT r FROM Reservation r WHERE CAST(r.startTime AS DATE) = :date AND r.institute = :institute")
  List<Reservation> findByInstitutesAndStartTimeOn(Institute institute, LocalDate date);

  @Query("SELECT r FROM Reservation r WHERE r.institute = :institutes AND r.startTime BETWEEN :startTime AND :endTime")
  List<Reservation> findByInstitutesAndTimeRange(Institute institute, LocalDateTime startTime, LocalDateTime endTime);

  @Query("SELECT r FROM Reservation r WHERE r.institute = :institutes AND r.startTime < :endTime AND r.endTime > :startTime")
  List<Reservation> findByInstitutesWithOverlappingTimeRange(Institute institute, LocalDateTime startTime, LocalDateTime endTime);

//  @Modifying
//  @Transactional
//  @Query("UPDATE Reservations r SET r.startTime = :startTime, r.endTime = :endTime, r.memo = :memo WHERE r.id = :reservationsId")
//  void updateReservations(Long reservationsId, LocalDateTime startTime, LocalDateTime endTime, String memo);

}
