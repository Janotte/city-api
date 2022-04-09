package com.example.cityapi;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;

@Slf4j
@SpringBootApplication
@EnableCaching
public class CityApiApplication {

	@Bean
	public MeterRegistry registry() {
		return new SimpleMeterRegistry();
	}

	public static void main(String[] args) {
		SpringApplication.run(CityApiApplication.class, args);
		log.info("Spring Boot City API application has been loaded");
	}
}
