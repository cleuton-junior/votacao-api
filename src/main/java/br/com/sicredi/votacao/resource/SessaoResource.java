package br.com.sicredi.votacao.resource;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.sicredi.votacao.dto.SessaoDTO;
import br.com.sicredi.votacao.exception.PautaNotFoundException;
import br.com.sicredi.votacao.exception.SessaoNotFoundException;
import br.com.sicredi.votacao.service.SessaoService;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api/v1/sessoes")
public class SessaoResource {
	
	@Autowired
	private SessaoService sessaoService;
	@Autowired
    private ModelMapper modelMapper;

	@ApiOperation(value = "Listar Sessoes")
	@GetMapping
	@ResponseStatus(code = HttpStatus.OK)
	public List<SessaoDTO> listarSessoes() {
		return sessaoService.listarSessoes().stream()
				.map(sessao -> modelMapper.map(sessao, SessaoDTO.class))
				.collect(Collectors.toList());
	}
	
	@ApiOperation(value = "Criar Sessao")
	@PostMapping
	public ResponseEntity<?> criarSessao(@Valid @RequestBody SessaoDTO sessaoDTO) {		
		try {
			sessaoService.criarSessao(sessaoDTO);
			return new ResponseEntity<>("Sessão criada com sucesso.", HttpStatus.CREATED);
		} catch (PautaNotFoundException e) {
			return new ResponseEntity<>(e.getCode(), e.getStatus());
		}

	}
	
	@ApiOperation(value = "Retorna Sessao")
	@GetMapping("/{id}")
	public ResponseEntity<?> retornaSessaoById(@PathVariable Long id) {
		try {
			SessaoDTO sessao = sessaoService.retornaSessao(id);
			return new ResponseEntity<>(sessao, HttpStatus.OK);
		} catch (SessaoNotFoundException e) {
			return new ResponseEntity<>(e.getCode(), e.getStatus());
		}

	}
	
	@ApiOperation(value = "Retorna Sessao por Pauta")
	@GetMapping("/{idSessao}/pautas/{idPauta}")
	public ResponseEntity<?> retornaSessaoPorPautaId(@PathVariable Long idSessao, @PathVariable Long idPauta) {
		try {
			SessaoDTO sessoes = sessaoService.retornaSessaoPorPautaId(idSessao, idPauta);
			return ResponseEntity.ok(sessoes);
		} catch (SessaoNotFoundException e) {
			return new ResponseEntity<>(e.getCode(), e.getStatus());
		}
	}	

	@ApiOperation(value = "Excluir Sessao")
	@DeleteMapping("/{id}")
	public ResponseEntity<?> excluirSessao(@PathVariable Long id) {
		try {
			sessaoService.excluir(id);
			return new ResponseEntity<>("Exclusão da sessão realizado com sucesso", HttpStatus.OK);
		} catch (SessaoNotFoundException e) {
			return new ResponseEntity<>(e.getCode(), e.getStatus());
		}
	}
}
