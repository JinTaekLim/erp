package com.erp.erp.domain.reservations.repository;

import com.erp.erp.domain.institute.common.entity.Institute;
import com.erp.erp.domain.reservations.common.entity.Reservations;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ReservationsRepository extends JpaRepository<Reservations, Long> {

  @Query("SELECT r FROM Reservations r WHERE CAST(r.startTime AS DATE) = :date AND r.institute = :institute")
  List<Reservations> findByInstitutesAndStartTimeOn(Institute institute, LocalDate date);

  @Query("SELECT r FROM Reservations r WHERE r.institute = :institutes AND r.startTime BETWEEN :startTime AND :endTime")
  List<Reservations> findByInstitutesAndTimeRange(Institute institute, LocalDateTime startTime, LocalDateTime endTime);

  @Query("SELECT r FROM Reservations r WHERE r.institute = :institutes AND r.startTime < :endTime AND r.endTime > :startTime")
  List<Reservations> findByInstitutesWithOverlappingTimeRange(Institute institute, LocalDateTime startTime, LocalDateTime endTime);

//  @Modifying
//  @Transactional
//  @Query("UPDATE Reservations r SET r.startTime = :startTime, r.endTime = :endTime, r.memo = :memo WHERE r.id = :reservationsId")
//  void updateReservations(Long reservationsId, LocalDateTime startTime, LocalDateTime endTime, String memo);

}
