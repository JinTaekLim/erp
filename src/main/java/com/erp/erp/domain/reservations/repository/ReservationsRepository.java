package com.erp.erp.domain.reservations.repository;

import com.erp.erp.domain.institutes.common.entity.Institutes;
import com.erp.erp.domain.reservations.common.entity.Reservations;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ReservationsRepository extends JpaRepository<Reservations, Long> {

  @Query("SELECT r FROM Reservations r WHERE CAST(r.startTime AS DATE) = :date AND r.institutes = :institute")
  List<Reservations> findByInstitutesAndStartTimeOn(Institutes institute, LocalDate date);

  @Query("SELECT r FROM Reservations r WHERE r.institutes = :institutes AND r.startTime BETWEEN :startTime AND :endTime")
  List<Reservations> findByInstitutesAndTimeRange(Institutes institutes, LocalDateTime startTime, LocalDateTime endTime);

  @Query("SELECT r FROM Reservations r WHERE r.institutes = :institutes AND r.startTime < :endTime AND r.endTime > :startTime")
  List<Reservations> findByInstitutesWithOverlappingTimeRange(Institutes institutes, LocalDateTime startTime, LocalDateTime endTime);

//  @Modifying
//  @Transactional
//  @Query("UPDATE Reservations r SET r.startTime = :startTime, r.endTime = :endTime, r.memo = :memo WHERE r.id = :reservationsId")
//  void updateReservations(Long reservationsId, LocalDateTime startTime, LocalDateTime endTime, String memo);

}
