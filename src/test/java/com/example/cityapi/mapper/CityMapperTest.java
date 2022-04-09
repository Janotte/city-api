package com.example.cityapi.mapper;

import com.example.cityapi.dto.request.CityDTO;
import com.example.cityapi.model.City;
import com.example.cityapi.utils.CityUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@DisplayName("Teste do Mapeador de Cidades")
public class CityMapperTest {

    @Autowired
    private CityMapper cityMapper;

    @Test
    @DisplayName("Dado uma CityDTO retorna uma entidade City")
    void testGivenCityDTOThenReturnCityEntity() {
        CityDTO cityDTO = CityUtils.createFakeDTO();
        City city = cityMapper.dtoToModel(cityDTO);

        assertEquals(cityDTO.getName(), city.getName());
        assertEquals(cityDTO.getState(), city.getState());
    }

    @Test
    @DisplayName("Dado uma entidade City retorna uma CityDTO")
    void testGivenCityEntityThenReturnCityDTO() {
        City city = CityUtils.createFakeEntity();
        CityDTO cityDTO = cityMapper.modelToDto(city);

        assertEquals(city.getName(), cityDTO.getName());
        assertEquals(city.getState(), cityDTO.getState());
    }

}