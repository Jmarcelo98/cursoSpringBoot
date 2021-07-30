package com.estudos.backend.services;

import javax.mail.internet.MimeMessage;

import org.springframework.mail.SimpleMailMessage;

import com.estudos.backend.domain.Pedido;

public interface EmailService {

	void enviarConfirmacaoEmail(Pedido obj);
	
	void enviarEmail(SimpleMailMessage msg); 
	
	void enviarPedidoConfirmacaoHtmlEmail(Pedido obj);
	
	void enviarHtmlEmail(MimeMessage msg);
}
