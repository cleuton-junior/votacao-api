package br.com.sicredi.votacao.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.sicredi.votacao.dto.VotacaoDTO;
import br.com.sicredi.votacao.service.VotacaoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(value = "Votacao")
public class VotacaoResource {
	
	@Autowired
	private VotacaoService votacaoService;

	@ApiOperation(value = "Get Resultado Votação")
	@GetMapping("v1/pautas/{id}/votacao")
	@ResponseStatus(code = HttpStatus.OK)
	public ResponseEntity<?> findVotosByPautaId(@PathVariable Long id) {
		VotacaoDTO votacao =  votacaoService.getResultVotacao(id);
		if (votacao == null)
			return new ResponseEntity<>("Votação não encontrada", HttpStatus.NOT_FOUND);
		return new ResponseEntity<>(votacao, HttpStatus.OK);
	}
	
}
