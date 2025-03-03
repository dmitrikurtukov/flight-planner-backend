package com.flightplanner.mappers;

import com.flightplanner.dto.SeatDto;
import com.flightplanner.entity.SeatEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SeatMapper {

    SeatDto toDto(SeatEntity entity);

    List<SeatDto> toDtoList(List<SeatEntity> entities);
}
