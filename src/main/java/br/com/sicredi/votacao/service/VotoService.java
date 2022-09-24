package br.com.sicredi.votacao.service;

import java.util.Collections;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import br.com.sicredi.votacao.config.RestTemplateConfig;
import br.com.sicredi.votacao.dto.StatusCpfDTO;
import br.com.sicredi.votacao.dto.VotoDTO;
import br.com.sicredi.votacao.model.Voto;
import br.com.sicredi.votacao.repository.VotoRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class VotoService {
	
	@Autowired
	private VotoRepository votoRepository;
	@Autowired
    private ModelMapper modelMapper;
    @Autowired   
    RestTemplateConfig restTemplate;
	
	private static final String CPF_UNABLE_TO_VOTE = "UNABLE_TO_VOTE";
	
	@Value("${app.integracao.cpf.url}")
	private String urlCpfValidator = "";

	public Voto create(VotoDTO votoDto) throws Exception {
        log.debug("Inicio criar voto.");
               
        verificaCpfVoto(votoDto);

        Voto voto = modelMapper.map(votoDto, Voto.class);
        log.debug("Gerando voto idPauta:[" + voto.getIdPauta() + "] voto=[" + voto.getIndicadorVotoSim()+"]");
        return votoRepository.save(voto);
    }
    
	protected void verificaCpfVoto(VotoDTO votoDto) throws Exception {
		log.debug("Validacao externa do CPF...");

		ResponseEntity<StatusCpfDTO> cpfValidation = validaCpf(votoDto);
		if (HttpStatus.OK.equals(cpfValidation.getStatusCode())) {
			if (CPF_UNABLE_TO_VOTE.equalsIgnoreCase(cpfValidation.getBody().getStatus())) {
				throw new Exception();
			}
		} else {
			throw new Exception();
		}

		log.debug("Fim da Validacao externa do CPF...");
	}
    
    private ResponseEntity<StatusCpfDTO> validaCpf(VotoDTO voto) {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		HttpEntity<String> entity = new HttpEntity<>(headers);
		return restTemplate.exchange(urlCpfValidator.concat("/").concat(voto.getNumeroCpf().toString()), HttpMethod.GET, entity,
				StatusCpfDTO.class);
	}
}
