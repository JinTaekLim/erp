package com.erp.erp.domain.reservation.repository;

import com.erp.erp.domain.institute.common.entity.Institute;
import com.erp.erp.domain.reservation.common.entity.Reservation;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

  Optional<Reservation> findByIdAndInstituteId(Long id, Long instituteId);

  @Query("SELECT r FROM Reservation r WHERE CAST(r.startTime AS DATE) = :date AND r.institute = :institute")
  List<Reservation> findByInstituteAndStartTimeOn(@Param("institute") Institute institute, @Param("date") LocalDate date);

  @Query("SELECT r FROM Reservation r WHERE r.institute = :institute AND r.startTime >= :startTime AND r.startTime < :endTime")
  List<Reservation> findByInstituteAndTimeRange(@Param("institute") Institute institute, @Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);

  @Query("SELECT r FROM Reservation r WHERE r.institute = :institute AND r.startTime < :endTime AND r.endTime > :startTime")
  List<Reservation> findByInstituteWithOverlappingTimeRange(@Param("institute") Institute institute, @Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);

//  @Modifying
//  @Transactional
//  @Query("UPDATE Reservations r SET r.startTime = :startTime, r.endTime = :endTime, r.memo = :memo WHERE r.id = :reservationsId")
//  void updateReservations(Long reservationsId, LocalDateTime startTime, LocalDateTime endTime, String memo);

}
