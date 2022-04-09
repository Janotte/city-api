package com.example.cityapi.utils;

import com.example.cityapi.dto.request.CityDTO;
import com.example.cityapi.model.City;
import com.example.cityapi.enums.State;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class CityUtils {

    private static Long CITY_ID = 1L;
    private static final String NAME = "Joinville";
    private static final State STATE = State.SC;

    public static CityDTO createFakeDTO() {
        return CityDTO
                .builder()
                .id(CITY_ID)
                .name(NAME)
                .state(STATE)
                .build();
    }

    public static City createFakeEntity() {
        return City
                .builder()
                .id(CITY_ID)
                .name(NAME)
                .state(STATE)
                .build();
    }

    public static String asJsonString(CityDTO cityDTO) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
            objectMapper.registerModules(new JavaTimeModule());

            return objectMapper.writeValueAsString(cityDTO);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}