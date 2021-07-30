package com.estudos.backend.services;

import java.util.Date;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.estudos.backend.domain.Pedido;

public abstract class AbstractEmailService implements EmailService {

	@Value("${default.sender}")
	private String sender;

	@Autowired
	private TemplateEngine templateEngine;

	@Autowired
	private JavaMailSender javaMailSender;

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

	protected String htmlApartirDoTemplatePedido(Pedido obj) {

		Context context = new Context();
		context.setVariable("pedido", obj);
		return templateEngine.process("email/confirmacaoPedido", context);

	}

	@Override
	public void enviarPedidoConfirmacaoHtmlEmail(Pedido obj) {
		try {
			MimeMessage mm = prepararMimeMessageDePedido(obj);
			enviarHtmlEmail(mm);
		} catch (MessagingException e) {
			enviarConfirmacaoEmail(obj);
		}

	}

	private MimeMessage prepararMimeMessageDePedido(Pedido obj) throws MessagingException {

		MimeMessage mimeMessage = javaMailSender.createMimeMessage();
		MimeMessageHelper mmh = new MimeMessageHelper(mimeMessage, true);
		mmh.setTo(obj.getCliente().getEmail());
		mmh.setFrom(sender);
		mmh.setSubject("Pedido confirmado! Código: " + obj.getId());
		mmh.setSentDate(new Date(System.currentTimeMillis()));
		mmh.setText(htmlApartirDoTemplatePedido(obj), true);

		return mimeMessage;
	}

}
