package com.erp.erp.domain.reservation.common.mapper;

import com.erp.erp.domain.reservation.business.PushUpdateReservationEvent;
import com.erp.erp.domain.reservation.common.dto.PushEventUpdateReservationMessage;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ReservationEventMapper {

 PushEventUpdateReservationMessage updateReservationMessage(PushUpdateReservationEvent event);

}
