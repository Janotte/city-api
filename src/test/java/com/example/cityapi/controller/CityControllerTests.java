package com.example.cityapi.controller;

import com.example.cityapi.exception.CityNotFoundException;
import com.example.cityapi.dto.request.CityDTO;
import com.example.cityapi.dto.response.MessageResponseDTO;
import com.example.cityapi.model.City;
import com.example.cityapi.service.CityService;
import io.micrometer.core.instrument.MeterRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import java.util.Collections;
import java.util.List;

import static com.example.cityapi.utils.CityUtils.asJsonString;
import static com.example.cityapi.utils.CityUtils.createFakeDTO;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Teste unitário do REST Controller")
@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = CityController.class)
public class CityControllerTests {

    private static final String CITY_API_URL_PATH = "/api/v1/cities";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CityController cityController;

    @Autowired
    private MeterRegistry registry;

    @MockBean
    private CityService cityService;

    @BeforeEach
    void setUp() {
        cityController = new CityController(cityService, registry);
        mockMvc = MockMvcBuilders.standaloneSetup(cityController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .setViewResolvers((viewName, locale) -> new MappingJackson2JsonView())
                .build();
    }

    @Test
    @DisplayName("Quando o POST é chamado, uma cidade deve ser criada")
    void whenPOSTIsCalledThenACityShouldBeCreated() throws Exception {
        CityDTO expectedCityDTO = createFakeDTO();
        MessageResponseDTO expectedResponseMessage = createMessageResponse("City successfully created with ID ", 1L);

        when(cityService.createNewCity(expectedCityDTO)).thenReturn(expectedResponseMessage);

        mockMvc.perform(
                post(CITY_API_URL_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(expectedCityDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message", is(expectedResponseMessage.getMessage())));
    }

    @Test
    @DisplayName("Quando GET com Id válido é chamado, então uma cidade deve ser retornada")
    void whenGETWithValidIdIsCalledThenACityShouldBeReturned() throws Exception {
        var expectedValidId = 1L;
        CityDTO expectedCityDTO = createFakeDTO();
        expectedCityDTO.setId(expectedValidId);

        when(cityService.getCityById(expectedValidId)).thenReturn(expectedCityDTO);

        mockMvc.perform(
                get(CITY_API_URL_PATH + "/" + expectedValidId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Joinville")))
                .andExpect(jsonPath("$.state", is("SC")));
    }

    @Test
    @DisplayName("Quando GET com Name válido é chamado, então uma cidade deve ser retornada")
    void whenGETWithValidNameIsCalledThenACityShouldBeReturned() throws Exception {
        var expectedValidName = "Joinville";
        CityDTO expectedCityDTO = createFakeDTO();
        expectedCityDTO.setName("Joinville");

        when(cityService.getCityByName(expectedValidName)).thenReturn(expectedCityDTO);

        mockMvc.perform(
                        get(CITY_API_URL_PATH + "/name/" + expectedValidName)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Joinville")))
                .andExpect(jsonPath("$.state", is("SC")));
    }

    @Test
    @DisplayName("Quando GET com Id inválido é chamado, então uma mensagem de erro deve ser retornada")
    void whenGETWithInvalidIdIsCalledThenAnErrorMessageShouldBeReturned() throws Exception {
        var expectedInvalidId = 1L;
        CityDTO expectedCityDTO = createFakeDTO();
        expectedCityDTO.setId(expectedInvalidId);

        when(cityService.getCityById(expectedInvalidId)).thenThrow(
                new CityNotFoundException(String.format(
                        City.class.getName() + " not found with ID " + expectedInvalidId)));

        mockMvc.perform(
                get(CITY_API_URL_PATH + "/" + expectedInvalidId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Quando GET com Name inválido é chamado, então uma mensagem de erro deve ser retornada")
    void whenGETWithInvalidNameIsCalledThenAnErrorMessageShouldBeReturned() throws Exception {
        var expectedInvalidName = "Joinville";
        CityDTO expectedCityDTO = createFakeDTO();
        expectedCityDTO.setName(expectedInvalidName);

        when(cityService.getCityByName(expectedInvalidName)).thenThrow(
                new CityNotFoundException(String.format(
                        City.class.getName() + " not found with name " + expectedInvalidName)));

        mockMvc.perform(
                        get(CITY_API_URL_PATH + "/name/" + expectedInvalidName)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Quando GET é chamado, então uma lista de cidades deve ser retornada")
    void whenGETIsCalledThenAnCityListShouldBeReturned() throws Exception {
        var expectedValidId = 1L;
        CityDTO expectedCityDTO = createFakeDTO();
        expectedCityDTO.setId(expectedValidId);
        List<CityDTO> expectedCityDTOList = Collections.singletonList(expectedCityDTO);

        when(cityService.getAllCities()).thenReturn(expectedCityDTOList);

        mockMvc.perform(
                get(CITY_API_URL_PATH)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("Joinville")))
                .andExpect(jsonPath("$[0].state", is("SC")));
    }

    @Test
    @DisplayName("Quando DELETE com Id válido é chamado, então uma cidade deve ser excluída")
    void whenDELETEWithValidIdIsCalledThenACityShouldBeDeleted() throws Exception {
        var expectedValidId = 1L;

        mockMvc.perform(delete(CITY_API_URL_PATH + "/" + expectedValidId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Quando PUT é chamado, então uma cidade deve ser atualizada")
    void whenPUTIsCalledThenACityShouldBeUpdated() throws Exception {
        var expectedValidId = 1L;
        CityDTO expectedCityDTO = createFakeDTO();
        MessageResponseDTO expectedResponseMessage = createMessageResponse("City successfully updated with ID ", 1L);

        when(cityService.updateCityById(expectedValidId, expectedCityDTO)).thenReturn(expectedResponseMessage);

        mockMvc.perform(put(CITY_API_URL_PATH + "/" + expectedValidId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(expectedCityDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is(expectedResponseMessage.getMessage())));
    }

    @Test
    @DisplayName("Quando DELETE com Id inválido é chamado, então uma cidade deve ser excluída")
    void whenDELETEWithInvalidIdIsCalledThenACityShouldBeDeleted() throws Exception {
        var expectedValidId = 1L;

        when(cityService.deleteCityById(expectedValidId)).thenThrow(CityNotFoundException.class);

        mockMvc.perform(delete(CITY_API_URL_PATH + "/" + expectedValidId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    private MessageResponseDTO createMessageResponse(String message, Long id) {
    return MessageResponseDTO
            .builder()
            .message(message + id)
            .build();
    }

}
