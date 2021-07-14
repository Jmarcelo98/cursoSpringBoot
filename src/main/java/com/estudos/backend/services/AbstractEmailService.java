package com.estudos.backend.services;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;

import com.estudos.backend.domain.Pedido;

public abstract class AbstractEmailService implements EmailService {

	@Value("${default.sender}")
	private String sender;

	@Override
	public void enviarConfirmacaoEmail(Pedido obj) {
		SimpleMailMessage sm = prepararSimpleMailMessageDePedido(obj);
		enviarEmail(sm);
	}

	protected SimpleMailMessage prepararSimpleMailMessageDePedido(Pedido obj) {

		SimpleMailMessage sm = new SimpleMailMessage();
		sm.setTo(obj.getCliente().getEmail());
		sm.setFrom(sender);
		sm.setSubject("Pedido confirmado! Código: " + obj.getId());
		sm.setSentDate(new Date(System.currentTimeMillis()));
		sm.setText(obj.toString());
		return sm;
	}

}
