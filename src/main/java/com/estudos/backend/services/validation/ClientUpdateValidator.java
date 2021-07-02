package com.estudos.backend.services.validation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerMapping;

import com.estudos.backend.DTO.ClienteDTO;
import com.estudos.backend.domain.Cliente;
import com.estudos.backend.repositories.ClienteRepository;
import com.estudos.backend.resources.exception.CampoMensagem;

public class ClientUpdateValidator implements ConstraintValidator<ClientUpdate, ClienteDTO> {

	@Autowired
	private HttpServletRequest req;

	@Autowired
	private ClienteRepository repo;

	@Override
	public void initialize(ClientUpdate ann) {
	}

	@Override
	public boolean isValid(ClienteDTO objDto, ConstraintValidatorContext context) {

		@SuppressWarnings("unchecked")
		Map<String, String> map = (Map<String, String>) req
				.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
		Integer uriId = Integer.parseInt(map.get("id"));

		List<CampoMensagem> list = new ArrayList<>();

		Cliente aux = repo.findByEmail(objDto.getEmail());

		if (aux != null && !aux.getId().equals(uriId)) {
			list.add(new CampoMensagem("email", "Email já existente"));
		}

		for (CampoMensagem e : list) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(e.getMensagem()).addPropertyNode(e.getCampoNome())
					.addConstraintViolation();
		}
		return list.isEmpty();
	}
}