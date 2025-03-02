package com.flightplanner.mappers;

import com.flightplanner.dto.FlightDto;
import com.flightplanner.entity.FlightEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FlightMapper {

    FlightDto toDto(FlightEntity entity);

    FlightEntity toEntity(FlightDto dto);
}
