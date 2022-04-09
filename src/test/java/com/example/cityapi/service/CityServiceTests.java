package com.example.cityapi.service;

import com.example.cityapi.exception.CityNotFoundException;
import com.example.cityapi.dto.request.CityDTO;
import com.example.cityapi.dto.response.MessageResponseDTO;
import com.example.cityapi.enums.State;
import com.example.cityapi.model.City;
import com.example.cityapi.repository.CityRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.example.cityapi.utils.CityUtils.createFakeDTO;
import static com.example.cityapi.utils.CityUtils.createFakeEntity;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@DisplayName("Teste unitário da camada de Serviço")
@ExtendWith(MockitoExtension.class)
public class CityServiceTests {

    @Mock
    private CityRepository mockedRepository;

    @InjectMocks
    private CityService cityService;

    @Test
    @DisplayName("Dado uma CityDTO então retorna sucesso na criação")
    void givenACityDTOThenReturnSuccessOnCreate() {
        CityDTO cityDTO = createFakeDTO();
        City expectedSavedCity = createFakeEntity();

        when(mockedRepository.save(any(City.class))).thenReturn(expectedSavedCity);

        MessageResponseDTO successMessage = cityService.createNewCity(cityDTO);

        assertEquals("City successfully created with ID 1", successMessage.getMessage());
    }

    @Test
    @DisplayName("Dado um ID de cidade válido, então retorna a cidade")
    void givenAnValidCityIdThenReturnThisCity() throws CityNotFoundException {
        CityDTO expectedCityDTO = createFakeDTO();
        City expectedSavedCity = createFakeEntity();
        expectedCityDTO.setId(expectedSavedCity.getId());

        when(mockedRepository.findById(expectedSavedCity.getId())).thenReturn(Optional.of(expectedSavedCity));

        CityDTO cityDTO = cityService.getCityById(expectedSavedCity.getId());

        assertEquals(expectedCityDTO, cityDTO);
        assertEquals(expectedSavedCity.getId(), cityDTO.getId());
        assertEquals(expectedSavedCity.getName(), cityDTO.getName());
        assertEquals(expectedSavedCity.getState(), cityDTO.getState());
    }

    @Test
    @DisplayName("Dado um ID de cidade inválido, então lança exceção")
    void givenAnInvalidCityIdThenThrowException() {
        var invalidCityId = 1L;

        when( mockedRepository.findById(invalidCityId)).thenReturn(Optional.ofNullable(any(City.class)));

        assertThrows(CityNotFoundException.class, () -> cityService.getCityById(invalidCityId));
    }


    @Test
    @DisplayName("Sem dados então retorna todas as cidades cadastradas")
    void givenNoDataThenReturnAllCitiesRegistered() {
        List<City> expectedRegisteredCities = Collections.singletonList(createFakeEntity());
        CityDTO cityDTO = createFakeDTO();

        when(mockedRepository.findAll()).thenReturn(expectedRegisteredCities);

        List<CityDTO> expectedCityDTOList = cityService.getAllCities();

        assertFalse(expectedCityDTOList.isEmpty());
        assertEquals(expectedCityDTOList.get(0).getId(), cityDTO.getId());
    }

    @Test
    @DisplayName("Dado um ID da cidade válido e informações de atualização, então retorna sucesso na atualização")
    void givenAnValidCityIdAndUpdateInfoThenReturnSuccessOnUpdate() throws CityNotFoundException {
        var updatedCityId = 2L;

        CityDTO updateCityDTORequest = createFakeDTO();
        updateCityDTORequest.setId(updatedCityId);
        updateCityDTORequest.setName("Porto Alegre");
        updateCityDTORequest.setState(State.RS);

        City expectedCityToUpdate = createFakeEntity();
        expectedCityToUpdate.setId(updatedCityId);

        City expectedCityUpdated = createFakeEntity();
        expectedCityUpdated.setId(updatedCityId);
        expectedCityToUpdate.setName(updateCityDTORequest.getName());
        expectedCityToUpdate.setState(updateCityDTORequest.getState());

        when(mockedRepository.findById(updatedCityId)).thenReturn(Optional.of(expectedCityUpdated));
        when(mockedRepository.save(any(City.class))).thenReturn(expectedCityUpdated);

        MessageResponseDTO successMessage = cityService.updateCityById(updatedCityId, updateCityDTORequest);

        assertEquals("City successfully updated with ID 2", successMessage.getMessage());
    }

    @Test
    @DisplayName("Dado um ID de cidade inválido e informações de atualização, então lança exceção na atualização")
    void givenInvalidCityIdAndUpdateInfoThenThrowExceptionOnUpdate() {
        var invalidCityId = 1L;

        CityDTO updateCityDTORequest = createFakeDTO();
        updateCityDTORequest.setId(invalidCityId);
        updateCityDTORequest.setName("Joinville Atualizada");

        when(mockedRepository.findById(invalidCityId)).thenReturn(Optional.ofNullable(any(City.class)));

        assertThrows(CityNotFoundException.class,
                () -> cityService.updateCityById(invalidCityId, updateCityDTORequest));
    }

    @Test
    @DisplayName("Dado um ID de cidade válido, então retorne sucesso ao excluir")
    void testGivenValidCityIdThenReturnSuccessOnDelete() throws CityNotFoundException {
        var deletedCityId = 1L;
        City expectedCityToDelete = createFakeEntity();

        when(mockedRepository.findById(deletedCityId)).thenReturn(Optional.of(expectedCityToDelete));
        cityService.deleteCityById(deletedCityId);

        verify(mockedRepository, times(1)).deleteById(deletedCityId);
    }
}
