package com.estudos.backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import com.estudos.backend.domain.PagamentoComBoleto;
import com.estudos.backend.domain.PagamentoComCartao;
import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
public class JacksonConfig {

	public Jackson2ObjectMapperBuilder objectMapperBuilder() {

		Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder() {

			public void configure(ObjectMapper objectMapper) {
				objectMapper.registerSubtypes(PagamentoComBoleto.class);
				objectMapper.registerSubtypes(PagamentoComCartao.class);
				super.configure(objectMapper);
			}

		};
		return builder;
	}
}
