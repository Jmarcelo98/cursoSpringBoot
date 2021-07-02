package com.estudos.backend.resources.exception;

import java.util.ArrayList;
import java.util.List;

public class ValidacaoError extends StandardError {
	private static final long serialVersionUID = 1L;

	private List<CampoMensagem> errors = new ArrayList<>();

	public ValidacaoError(Integer status, String msg, Long timeStamp) {
		super(status, msg, timeStamp);
		// TODO Auto-generated constructor stub
	}

	public List<CampoMensagem> getErrors() {
		return errors;
	}

	public void addError(String campoNome, String mensagem) {
		errors.add(new CampoMensagem(campoNome, mensagem));
	}

}
