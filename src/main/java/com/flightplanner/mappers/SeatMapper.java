package com.flightplanner.mappers;

import com.flightplanner.dto.SeatDto;
import com.flightplanner.entity.SeatEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SeatMapper {

    @Mapping(source = "isReserved", target = "reserved")
    SeatDto toDto(SeatEntity entity);

    List<SeatDto> toDtoList(List<SeatEntity> entities);
}
