package br.com.sicredi.votacao.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class SessaoNotFoundException extends RuntimeException{
	private static final long serialVersionUID = 8070540710723120457L;
	
	private final String code;
	private final HttpStatus status;
	
	public SessaoNotFoundException() {
		this("Sessão não encontrada.", HttpStatus.NOT_FOUND);
	}
}
