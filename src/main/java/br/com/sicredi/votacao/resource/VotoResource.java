package br.com.sicredi.votacao.resource;

import java.net.URI;
import java.util.List;

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
	@PostMapping
	public ResponseEntity<?> criarVoto(@RequestBody VotoDTO votoDto) throws Exception {
		try {
			Voto voto = votoService.criarVoto(votoDto);
			return ResponseEntity.created(new URI(String.format("%d", voto.getId()))).build();
		} catch (VotoExistsException e) {
			return new ResponseEntity<>(e.getCode(), e.getStatus());
		} catch (InvalidCpfException e) {
			return new ResponseEntity<>(e.getCode(), e.getStatus());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

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
	
	@ApiOperation(value = "Retorna Voto por sessao")
	@GetMapping("/pautas/{id}")
	@ResponseStatus(code = HttpStatus.OK)
	public List<VotoDTO> findVotoBySessaoId(@PathVariable Long id) {
		return votoService.findVotosByPautaId(id);
	}

	
	@ApiOperation(value = "Excluir Voto")
	@DeleteMapping("/{id}")
	public ResponseEntity<?> delete(@PathVariable Long id){
		try {
			votoService.delete(id);
			return new ResponseEntity<>("Exclusão do voto realizado com sucesso", HttpStatus.OK);
		} catch (VotoNotFoundException e) {
			return new ResponseEntity<>(e.getCode(), e.getStatus());
		}
	}

}
