package br.com.sicredi.votacao.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class InvalidCpfException extends RuntimeException {

	private static final long serialVersionUID = -7328181350828951033L;
	
	private final String code;
	private final HttpStatus status;
	
	public InvalidCpfException() {
		this("CPF invalido.", HttpStatus.BAD_REQUEST);
	}
}
