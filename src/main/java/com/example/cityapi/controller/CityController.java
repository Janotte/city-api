package com.example.cityapi.controller;

import com.example.cityapi.dto.request.CityDTO;
import com.example.cityapi.dto.response.MessageResponseDTO;
import com.example.cityapi.exception.CityNotFoundException;
import com.example.cityapi.service.CityService;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/cities")
public class CityController {

    @Autowired
    private CityService cityService;

    @Autowired
    private MeterRegistry registry;

    Timer postCityTimer;
    Counter postCityCounter;
    Timer getCitiesTimer;
    Counter getCitiesCounter;

    public CityController(CityService cityService, MeterRegistry registry) {
        this.cityService = cityService;
        this.registry = registry;
        this.postCityTimer = Timer.builder("cities_post_timer").register(registry);
        this.postCityCounter = Counter.builder("cities_post_counter").register(registry);
        this.getCitiesTimer = Timer.builder("cities_get_all_timer").register(registry);
        this.getCitiesCounter = Counter.builder("cities_get_all_counter").register(registry);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MessageResponseDTO createNewCity(@RequestBody @Valid CityDTO cityDTO) {

        return postCityTimer.record(()  -> {
                var messageResponseDTO = cityService.createNewCity(cityDTO);
                if (messageResponseDTO.getMessage().contains("City successfully created")) {
                    postCityCounter.increment();
                }
                return messageResponseDTO;
        });
    }

    @GetMapping
    public List<CityDTO> getAllCities() {

        return getCitiesTimer.record(() -> {
            getCitiesCounter.increment();
            return cityService.getAllCities();
        });
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CityDTO getCityById(@PathVariable(value = "id") Long id) throws CityNotFoundException {

        return cityService.getCityById(id);
    }

    @GetMapping("/name/{name}")
    @ResponseStatus(HttpStatus.OK)
    public CityDTO getCityByName(@PathVariable(value = "name") String name) throws CityNotFoundException {

        return cityService.getCityByName(name);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public MessageResponseDTO deleteCityById(@PathVariable("id") Long id) throws CityNotFoundException {

        return cityService.deleteCityById(id);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public MessageResponseDTO updateCityById(@PathVariable("id") Long id, @Valid @RequestBody CityDTO cityDTO)
            throws CityNotFoundException {

        return cityService.updateCityById(id, cityDTO);
    }
}