package com.estudos.backend.services;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.estudos.backend.domain.Cliente;
import com.estudos.backend.repositories.ClienteRepository;
import com.estudos.backend.services.exception.ObjectNotFoundException;

@Service
public class AuthService {

	@Autowired
	private ClienteRepository clienteRepository;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	private Random random = new Random();

	@Autowired
	private EmailService emailService;

	public void enviarNovaSeha(String email) {

		Cliente cliente = clienteRepository.findByEmail(email);

		if (cliente == null) {
			throw new ObjectNotFoundException("Email não encontrado");
		}

		String novaSenha = novaSenha();
		cliente.setSenha(bCryptPasswordEncoder.encode(novaSenha));

		clienteRepository.save(cliente);
		emailService.enviarNovaSenhaEmail(cliente, novaSenha);
	}

	private String novaSenha() {

		char[] vet = new char[10];

		for (int i = 0; i < 10; i++) {
			vet[i] = caracterAleatorio();
		}
		return new String(vet);
	}

	private char caracterAleatorio() {

		int opt = random.nextInt(3);

		if (opt == 0) { // gera um digito
			return (char) (random.nextInt(10) + 48);
		} else if (opt == 1) { // gera letra maiuscula
			return (char) (random.nextInt(26) + 65);
		} else { // gera letra minuscula
			return (char) (random.nextInt(26) + 97);
		}

	}

}
