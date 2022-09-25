package br.com.sicredi.votacao.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class VotoExistsException extends RuntimeException{

	private static final long serialVersionUID = 3987125938865046003L;

	private final String code;
	private final HttpStatus status;
	
	public VotoExistsException() {
		this("Já existe voto registrado para esta Pauta e para este CPF.", HttpStatus.ALREADY_REPORTED);
	}
}
