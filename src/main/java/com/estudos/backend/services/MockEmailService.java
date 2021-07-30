package com.estudos.backend.services;

import javax.mail.internet.MimeMessage;

import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;

import ch.qos.logback.classic.Logger;

public class MockEmailService extends AbstractEmailService {

	private static final Logger LOG = (Logger) LoggerFactory.getLogger(MockEmailService.class);

	@Override
	public void enviarEmail(SimpleMailMessage msg) {

		LOG.info("Simulando envio de email");
		LOG.info(msg.toString());
		LOG.info("Email enviado");

	}

	@Override
	public void enviarHtmlEmail(MimeMessage msg) {

		LOG.info("Simulando envio de email html");
		LOG.info(msg.toString());
		LOG.info("Email enviado");

	}

}
