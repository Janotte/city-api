package com.example.cityapi.mapper;

import com.example.cityapi.dto.request.CityDTO;
import com.example.cityapi.model.City;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CityMapper {

    CityMapper INSTANCE = Mappers.getMapper(CityMapper.class);

    CityDTO modelToDto(City city);

    @InheritInverseConfiguration
    City dtoToModel(CityDTO cityDTO);
}
