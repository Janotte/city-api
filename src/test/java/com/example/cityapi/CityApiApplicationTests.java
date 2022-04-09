package com.example.cityapi;

import com.example.cityapi.controller.CityController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DisplayName("Teste de Sanidade")
@ExtendWith(SpringExtension.class)
@SpringBootTest
class CityApiApplicationTests {

	@Autowired
	CityController cityController;

	@Test
	@DisplayName("Verificando se a aplicação carrega corretamente.")
	void contextLoads() {
		assertThat(cityController).isNotNull();
	}

}
