package br.com.sicredi.votacao.resource;

import java.math.BigInteger;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.sicredi.votacao.dto.PautaDTO;
import br.com.sicredi.votacao.dto.VotacaoDTO;
import br.com.sicredi.votacao.model.Pauta;
import br.com.sicredi.votacao.service.PautaService;

@RestController
@RequestMapping("/api/v1/pauta")
public class PautaResource {
	
	@Autowired
	private PautaService pautaService;	
	@Autowired
    private ModelMapper modelMapper;
	
	
    @PostMapping("/{descricaoPauta}")
    public ResponseEntity criarPauta(@PathVariable("descricaoPauta") String desPauta) throws URISyntaxException {
        Pauta pauta = pautaService.criar(desPauta);
        return ResponseEntity.created(new URI(String.format("%d", pauta.getId())))
                .build();
    }

    @GetMapping
    public List<PautaDTO> listarTodasPautas() {
        return pautaService.listarPautas().stream()
                .map(pauta -> modelMapper.map(pauta, PautaDTO.class))
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity recuperar(@PathVariable("id") BigInteger id) {
        try {
            Pauta pauta = pautaService.recuperarPauta(id);
            return ResponseEntity.ok(pauta);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/abrir/{id}")
    public ResponseEntity abrirPauta(@PathVariable("id") BigInteger idPauta,
                               @RequestParam(defaultValue = "1", required = false)
                               Integer qtdMinutos) {
        pautaService.abrirPauta(idPauta, qtdMinutos);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/totais/{id}")
    public ResponseEntity fecharPauta(@PathVariable("id") BigInteger idPauta) {
        VotacaoDTO votacaoDto = pautaService.fecharPauta(idPauta);
        return ResponseEntity.ok(votacaoDto);
    }


}
