package com.estudos.backend.services;

import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;

import ch.qos.logback.classic.Logger;

public class MockEmailService extends AbstractEmailService{

	private static final Logger LOG = (Logger) LoggerFactory.getLogger(MockEmailService.class);
	
	@Override
	public void enviarEmail(SimpleMailMessage msg) {
		
		LOG.info("Simulando envio de email");
		LOG.info(msg.toString());
		LOG.info("Email enviado");
		
	}

}
