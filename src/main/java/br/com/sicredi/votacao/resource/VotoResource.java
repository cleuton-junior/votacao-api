package br.com.sicredi.votacao.resource;

import java.net.URI;
import java.util.List;
import java.util.concurrent.TimeoutException;

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

import br.com.sicredi.votacao.dto.VotoDTO;
import br.com.sicredi.votacao.exception.InvalidCpfException;
import br.com.sicredi.votacao.exception.VotoExistsException;
import br.com.sicredi.votacao.exception.VotoNotFoundException;
import br.com.sicredi.votacao.model.Voto;
import br.com.sicredi.votacao.service.VotoService;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api/v1/votos")
public class VotoResource {
	
	@Autowired
	private VotoService votoService;
	
	@ApiOperation(value = "Listar Votos")
	@GetMapping
	@ResponseStatus(code = HttpStatus.OK)
	public List<VotoDTO> litarVotos() {
		return votoService.listarVotos();
	}

	@ApiOperation(value = "Criar Voto")
	@PostMapping("sessao/{idSessao}")
	public ResponseEntity<?> criarVoto(@PathVariable Long idSessao, @RequestBody VotoDTO votoDto) throws Exception {
		try {
			Voto voto = votoService.criarVoto(idSessao, votoDto);
			return ResponseEntity.created(new URI(String.format("%d", voto.getId()))).build();
		} catch (IllegalArgumentException | TimeoutException | InvalidCpfException | VotoExistsException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);

		}
	}
	
	@ApiOperation(value = "Retorna Voto")
	@GetMapping("/{id}")
	public ResponseEntity<?> retornaVotoById(@PathVariable Long id) {
		try {
            VotoDTO voto = votoService.retornaVotoById(id);
            return ResponseEntity.ok(voto);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
	}
	
	@ApiOperation(value = "Retorna Voto por Pauta")
	@GetMapping("/pautas/{id}")
	@ResponseStatus(code = HttpStatus.OK)
	public List<VotoDTO> retornaVotoBySessaoId(@PathVariable Long id) {
		return votoService.retornaVotosByPautaId(id);
	}

	
	@ApiOperation(value = "Excluir Voto")
	@DeleteMapping("/{id}")
	public ResponseEntity<?> excluirVoto(@PathVariable Long id){
		try {
			votoService.excluirVoto(id);
			return new ResponseEntity<>("Exclusão do voto realizado com sucesso", HttpStatus.OK);
		} catch (VotoNotFoundException e) {
			return new ResponseEntity<>(e.getCode(), e.getStatus());
		}
	}

}
