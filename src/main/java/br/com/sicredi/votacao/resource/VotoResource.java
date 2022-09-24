package br.com.sicredi.votacao.resource;

import java.net.URI;
import java.net.URISyntaxException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.sicredi.votacao.dto.VotoDTO;
import br.com.sicredi.votacao.model.Voto;
import br.com.sicredi.votacao.service.VotoService;

@RestController
@RequestMapping("/api/v1/voto")
public class VotoResource {
	
	@Autowired
	private VotoService votoService;

    @PostMapping
    public ResponseEntity create(@RequestBody VotoDTO votoDto) throws URISyntaxException {
        Voto voto;
		try {
			voto = votoService.create(votoDto);
			return ResponseEntity.created(new URI(String.format("%d", voto.getId())))
	                .build();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        
    }

}
