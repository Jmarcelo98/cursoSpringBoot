package com.estudos.backend.services.validation;

import java.util.ArrayList;
import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.estudos.backend.DTO.ClienteNovoDTO;
import com.estudos.backend.domain.enums.TipoCliente;
import com.estudos.backend.resources.exception.CampoMensagem;
import com.estudos.backend.services.validation.utils.BR;

public class ClientInsertValidator implements ConstraintValidator<ClientInsert, ClienteNovoDTO> {

	@Override
	public void initialize(ClientInsert ann) {
	}

	@Override
	public boolean isValid(ClienteNovoDTO objDto, ConstraintValidatorContext context) {
		List<CampoMensagem> list = new ArrayList<>();

		if (objDto.getTipoCliente().equals(TipoCliente.PESSOAFISICA.getCodigo())
				&& !BR.isValidCPF(objDto.getCpfOuCnpj())) {
			list.add(new CampoMensagem("cpfOuCnpj", "CPF inválido"));
		}
		
		if (objDto.getTipoCliente().equals(TipoCliente.PESSOAJURIDICA.getCodigo())
				&& !BR.isValidCNPJ(objDto.getCpfOuCnpj())) {
			list.add(new CampoMensagem("cpfOuCnpj", "CNPJ inválido"));
		}


		for (CampoMensagem e : list) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(e.getMensagem()).addPropertyNode(e.getCampoNome())
					.addConstraintViolation();
		}
		return list.isEmpty();
	}
}