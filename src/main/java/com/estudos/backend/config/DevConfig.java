package com.estudos.backend.config;

import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.estudos.backend.services.DBService;
import com.estudos.backend.services.EmailService;
import com.estudos.backend.services.SmtpEmailService;

@Configuration
@Profile("dev")
public class DevConfig {

	@Autowired
	private DBService dbService;

	@Value("${spring.jpa.hibernate.ddl-auto}")
	private String estrategia;

	@Bean
	public boolean instaciarBancoDeDados() throws ParseException {

		if (!"create".equals(estrategia)) {
			return false;
		}
		dbService.instaciarTesteBancoDeDados();

		return true;
	}

	@Bean
	public EmailService emailService() {
		return new SmtpEmailService();
	}
	
}
