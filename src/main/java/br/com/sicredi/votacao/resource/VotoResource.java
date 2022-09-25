package br.com.sicredi.votacao.resource;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.sicredi.votacao.dto.VotoDTO;
import br.com.sicredi.votacao.exception.InvalidCpfException;
import br.com.sicredi.votacao.exception.VotoExistsException;
import br.com.sicredi.votacao.model.Voto;
import br.com.sicredi.votacao.service.VotoService;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api/v1/votos")
public class VotoResource {
	
	@Autowired
	private VotoService votoService;

	@ApiOperation(value = "Criar Voto")
    @PostMapping
    public ResponseEntity create(@RequestBody VotoDTO votoDto) throws Exception {
        Voto voto;
		try {
			voto = votoService.create(votoDto);
			return ResponseEntity.created(new URI(String.format("%d", voto.getId())))
	                .build();
		} catch (VotoExistsException e) {
			return new ResponseEntity<>(e.getCode(), e.getStatus());
		} catch (InvalidCpfException e) {
			return new ResponseEntity<>(e.getCode(), e.getStatus());		
		} catch (Exception e) {
			 return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

		}
    }
	
	@ApiOperation(value = "Deletar Voto")
	@DeleteMapping("/{id}")
	@ResponseStatus(code = HttpStatus.OK)
	public ResponseEntity delete(@PathVariable Long id) throws Exception {
		return votoService.delete(id);
	}

}
