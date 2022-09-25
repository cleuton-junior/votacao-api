package br.com.sicredi.votacao.resource;

import java.net.URI;
import java.net.URISyntaxException;
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

import br.com.sicredi.votacao.dto.PautaDTO;
import br.com.sicredi.votacao.exception.PautaNotFoundException;
import br.com.sicredi.votacao.model.Pauta;
import br.com.sicredi.votacao.service.PautaService;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api/v1/pautas")
public class PautaResource {
	
	@Autowired
	private PautaService pautaService;	
		
	@ApiOperation(value = "Criar Pauta")
    @PostMapping
    public ResponseEntity<?> criarPauta(@Valid @RequestBody PautaDTO pautaDTO) throws URISyntaxException {
        Pauta pautaCriada = pautaService.criar(pautaDTO);
        return ResponseEntity.created(new URI(String.format("%d", pautaCriada.getId())))
                .build();
    }

	@ApiOperation(value = "Listar Pautas")
    @GetMapping
    @ResponseStatus(code = HttpStatus.OK)
    public List<PautaDTO> listarTodasPautas() {
        return pautaService.listarPautas();
    }

	@ApiOperation(value = "Retorna Pauta")
    @GetMapping("/{id}")
    public ResponseEntity<?> retornaPauta(@PathVariable("id") Long id) {
        try {
            PautaDTO pauta = pautaService.retornaPauta(id);
            return ResponseEntity.ok(pauta);
        } catch (PautaNotFoundException e) {
        	return new ResponseEntity<>(e.getCode(), e.getStatus());
        }
    }
	
	@ApiOperation(value = "Excluir Pauta")
	@DeleteMapping("v1/pautas/{id}")
	public ResponseEntity<?> delete(@PathVariable Long id) {
		try {
			pautaService.delete(id);
			return new ResponseEntity<>("Exclusão da pauta realizado com sucesso", HttpStatus.OK);
		} catch (PautaNotFoundException e) {
			return new ResponseEntity<>(e.getCode(), e.getStatus());
		}
		
	}

}
