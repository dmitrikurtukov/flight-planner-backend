package com.flightplanner.mappers;

import com.flightplanner.dto.FlightDto;
import com.flightplanner.entity.FlightEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface FlightMapper {

    FlightDto toDto(FlightEntity entity);

    List<FlightDto> toDtoList(List<FlightEntity> entities);
}
