package br.com.sicredi.votacao.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class PautaNotFoundException extends RuntimeException{
	private static final long serialVersionUID = 3771256709826062509L;
	
	private final String code;
	private final HttpStatus status;
	
	public PautaNotFoundException() {
		this("Pauta não encontrada.", HttpStatus.NOT_FOUND);
	}
}
