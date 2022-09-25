package br.com.sicredi.votacao.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class VotoNotFoundException extends RuntimeException{
	private static final long serialVersionUID = 5026250705480437138L;
	
	private final String code;
	private final HttpStatus status;
	
	public VotoNotFoundException() {
		this("Voto não encontrado.", HttpStatus.NOT_FOUND);
	}
}
