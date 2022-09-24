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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.sicredi.votacao.dto.PautaDTO;
import br.com.sicredi.votacao.model.Pauta;
import br.com.sicredi.votacao.service.PautaService;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api/v1/pautas")
public class PautaResource {
	
	@Autowired
	private PautaService pautaService;	
	@Autowired
    private ModelMapper modelMapper;
	
	
	@ApiOperation(value = "Criar Pauta")
    @PostMapping
    public ResponseEntity criarPauta(@Valid @RequestBody Pauta pauta) throws URISyntaxException {
        Pauta pautaCriada = pautaService.criar(pauta);
        return ResponseEntity.created(new URI(String.format("%d", pautaCriada.getId())))
                .build();
    }

	@ApiOperation(value = "Listar Pautas")
    @GetMapping
    @ResponseStatus(code = HttpStatus.OK)
    public List<PautaDTO> listarTodasPautas() {
        return pautaService.listarPautas().stream()
                .map(pauta -> modelMapper.map(pauta, PautaDTO.class))
                .collect(Collectors.toList());
    }

	@ApiOperation(value = "Recuperar Pauta")
    @GetMapping("/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public ResponseEntity recuperar(@PathVariable("id") Long id) {
        try {
            Pauta pauta = pautaService.recuperarPauta(id);
            return ResponseEntity.ok(pauta);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

	@ApiOperation(value = "Abrir Pauta")
    @PostMapping("/abrir/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public ResponseEntity abrirPauta(@PathVariable("id") Long idPauta,
                               @RequestParam(defaultValue = "1", required = false)
                               Integer qtdMinutos) {
        pautaService.abrirPauta(idPauta, qtdMinutos);
        return ResponseEntity.ok().build();
    }
    
	@ApiOperation(value = "Delete Pauta")
	@DeleteMapping("/{id}")
	@ResponseStatus(code = HttpStatus.OK)
	public void delete(@PathVariable Long id) {
		pautaService.delete(id);
	}

//    @PostMapping("/totais/{id}")
//    public ResponseEntity fecharPauta(@PathVariable("id") BigInteger idPauta) {
//        VotacaoDTO votacaoDto = pautaService.fecharPauta(idPauta);
//        return ResponseEntity.ok(votacaoDto);
//    }


}
