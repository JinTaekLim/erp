package com.erp.erp.domain.reservation.common.mapper;

import com.erp.erp.domain.account.common.entity.Account;
import com.erp.erp.domain.customer.common.entity.Customer;
import com.erp.erp.domain.customer.common.entity.Progress;
import com.erp.erp.domain.reservation.common.dto.AddReservationDto;
import com.erp.erp.domain.reservation.common.dto.AddReservationMessageDto;
import com.erp.erp.domain.reservation.common.dto.PendingReservationDto;
import com.erp.erp.domain.reservation.common.dto.ReservationCache;
import com.erp.erp.domain.reservation.common.dto.UpdateReservationMessageDto;
import com.erp.erp.domain.reservation.common.entity.Reservation;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ReservationSenderMapper {

  AddReservationMessageDto toAddReservationMessageDto(Account account, Customer customer, PendingReservationDto pendingReservation, AddReservationDto.Request req);

  UpdateReservationMessageDto toUpdateReservationMessageDto(Reservation reservation, ReservationCache reservationCache, List<Progress> progress, PendingReservationDto pendingReservation);

}
