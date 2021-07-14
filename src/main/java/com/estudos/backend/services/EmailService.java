package com.estudos.backend.services;

import org.springframework.mail.SimpleMailMessage;

import com.estudos.backend.domain.Pedido;

public interface EmailService {

	void enviarConfirmacaoEmail(Pedido obj);
	
	void enviarEmail(SimpleMailMessage msg); 
	
}
