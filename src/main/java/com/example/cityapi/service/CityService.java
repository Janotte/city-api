package com.example.cityapi.service;

import com.example.cityapi.dto.request.CityDTO;
import com.example.cityapi.dto.response.MessageResponseDTO;
import com.example.cityapi.model.City;
import com.example.cityapi.exception.CityNotFoundException;
import com.example.cityapi.mapper.CityMapper;
import com.example.cityapi.repository.CityRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CityService {

    private final CityMapper cityMapper = CityMapper.INSTANCE;

    @Autowired
    private CityRepository cityRepository;

    public CityService(CityRepository cityRepository) {
        this.cityRepository = cityRepository;
    }

    public MessageResponseDTO createNewCity(CityDTO cityDTO) {

        log.info("Creating a new city");

        City cityToSave = cityMapper.dtoToModel(cityDTO);

        City savedCity = cityRepository.save(cityToSave);

        return createMessageResponse("City successfully created with ID ", savedCity.getId());
    }

    public List<CityDTO> getAllCities() {

        log.info("Fetching all cities");

        List<City> allCities = cityRepository.findAll();

        return allCities
                .stream()
                .map(cityMapper::modelToDto)
                .collect(Collectors.toList());
    }

    public CityDTO getCityById(Long id) throws CityNotFoundException {

        log.info("Searching city id {} ", id);

        City city = cityRepository.findById(id).orElseThrow(() -> new CityNotFoundException(
                City.class.getName() + " not found with ID " + id));

        return cityMapper.modelToDto(city);
    }

    public CityDTO getCityByName(String name) throws CityNotFoundException {

        log.info("Searching city name {} ", name);

        City city = cityRepository.findByName(name)
                .orElseThrow(() -> new CityNotFoundException(
                        City.class.getName() + " not found with name " + name));

        return cityMapper.modelToDto(city);
    }

    public MessageResponseDTO  updateCityById(Long id, CityDTO cityDTO) throws CityNotFoundException {

        log.info("Updating city with id {} ", id);

        verifyIfExists(id);

        City cityToUpdate = cityMapper.dtoToModel(cityDTO);

        City updatedCity = cityRepository.save(cityToUpdate);

        return createMessageResponse("City successfully updated with ID ", updatedCity.getId());

    }

    public MessageResponseDTO deleteCityById(Long id) throws CityNotFoundException {

        log.info("Deleting city with id {} ", id);

        verifyIfExists(id);

        cityRepository.deleteById(id);

        return createMessageResponse("City successfully deleted with ID ", id);
    }

    private void verifyIfExists(Long id) throws CityNotFoundException {

        cityRepository.findById(id)
                .orElseThrow(() -> new CityNotFoundException(
                        (City.class.getName() + " not found with ID " + id)));
    }

    private MessageResponseDTO createMessageResponse(String message, Long id) {
        return MessageResponseDTO
                .builder()
                .message(message + id)
                .build();
    }

}