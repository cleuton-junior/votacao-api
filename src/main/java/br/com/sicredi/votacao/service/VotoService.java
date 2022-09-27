package br.com.sicredi.votacao.service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

import javax.naming.directory.InvalidAttributesException;

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
import org.springframework.util.Assert;

import br.com.sicredi.votacao.config.RestTemplateConfig;
import br.com.sicredi.votacao.dto.SessaoDTO;
import br.com.sicredi.votacao.dto.StatusCpfDTO;
import br.com.sicredi.votacao.dto.VotoDTO;
import br.com.sicredi.votacao.exception.InvalidCpfException;
import br.com.sicredi.votacao.exception.VotoExistsException;
import br.com.sicredi.votacao.exception.VotoNotFoundException;
import br.com.sicredi.votacao.model.Voto;
import br.com.sicredi.votacao.repository.VotoRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class VotoService {
	private static final String CPF_UNABLE_TO_VOTE = "UNABLE_TO_VOTE";
	
	@Value("${app.integracao.cpf.url}")
	private String urlCpfValidator = "";
	
	private VotoRepository votoRepository;
    private ModelMapper modelMapper;  
    private RestTemplateConfig restTemplate;
	private SessaoService sessaoService;
    
	@Autowired
	public VotoService(RestTemplateConfig restTemplate, VotoRepository votoRepository, SessaoService sessaoService, ModelMapper modelMapper) {
		this.restTemplate = restTemplate;
		this.votoRepository = votoRepository;
		this.sessaoService = sessaoService;
		this.modelMapper = modelMapper;
	}
			
	public VotoDTO retornaVotoById(Long id) {
		Optional<Voto> votoId = votoRepository.findById(id);		
		VotoDTO dto = modelMapper.map(votoId.get(), VotoDTO.class);
		return dto;
	}

	public Voto criarVoto(Long idSessao, VotoDTO votoDto) throws TimeoutException {
		SessaoDTO sessaoDto = sessaoService.retornaSessaoPorPautaId(idSessao, votoDto.getIdPauta());
		if (!votoDto.getIdPauta().equals(sessaoDto.getIdPauta())) {
			throw new IllegalArgumentException("Sessão inválida para a Pauta.");
			
		}
        log.debug("Inicio criar voto."); 
        
		verificaVoto(sessaoDto, votoDto);
        Voto voto = modelMapper.map(votoDto, Voto.class);
        
        log.debug("Fim criar voto."); 
        return votoRepository.save(voto);
    }
	
	public void verificaVoto(SessaoDTO sessaoDto, VotoDTO votoDto) throws TimeoutException {

		LocalDateTime dataLimite = sessaoDto.getDataInicio().plusMinutes(sessaoDto.getMinutosValidade());
		if (LocalDateTime.now().isAfter(dataLimite)) {
			throw new TimeoutException("Tempo para votação ultrapassado.");
		}

		verificaCpfVoto(votoDto);
		votoJaExiste(votoDto);
	}
    
	public void verificaCpfVoto(VotoDTO votoDto) {
		log.debug("Validacao externa do CPF...");

		ResponseEntity<StatusCpfDTO> cpfValidation = validaCpf(votoDto);
		if (HttpStatus.OK.equals(cpfValidation.getStatusCode())) {
			if (CPF_UNABLE_TO_VOTE.equalsIgnoreCase(cpfValidation.getBody().getStatus())) {
				log.debug("CPF: ", cpfValidation.getBody().getStatus());
			}
		} else {
			log.debug("CPF: ", cpfValidation.getBody().getStatus());
			throw new InvalidCpfException();
		}

		log.debug("Fim da Validacao externa do CPF...");
	}
    
    public ResponseEntity<StatusCpfDTO> validaCpf(VotoDTO voto) {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		HttpEntity<String> entity = new HttpEntity<>(headers);
		return restTemplate.exchange(urlCpfValidator.concat("/").concat(voto.getNumeroCpf().toString()), HttpMethod.GET, entity,
				StatusCpfDTO.class);
	}
    
	public void votoJaExiste(VotoDTO votoDto) {
		Optional<Voto> votoByCpfAndPauta = votoRepository.findByNumeroCpfAndPautaId(votoDto.getNumeroCpf(), votoDto.getIdPauta());

		if (votoByCpfAndPauta.isPresent()) {
			throw new VotoExistsException();
		}
	}
	
	public List<VotoDTO> listarVotos() {
		return votoRepository.findAll().stream()
                .map(voto -> modelMapper.map(voto, VotoDTO.class))
                .collect(Collectors.toList());
	}
	
	public void excluirVoto(Long id){
		Optional<Voto> votoById = votoRepository.findById(id);
		if (!votoById.isPresent()) {
			throw new VotoNotFoundException();
		}		
		votoRepository.delete(votoById.get());
	}
	
	public List<VotoDTO> retornaVotosByPautaId(Long id) {
		Optional<List<Voto>> votoPautaId = votoRepository.findByPautaId(id);
		if (!votoPautaId.isPresent()) {
			throw new VotoNotFoundException();
		}	
		return votoPautaId.get().stream().map(voto -> modelMapper.map(voto, VotoDTO.class))
				.collect(Collectors.toList());
	}
    
    void excluirByPautaId(Long id) {
		Optional<List<Voto>> votos = votoRepository.findByPautaId(id);
		votos.ifPresent(voto -> voto.forEach(votoRepository::delete));
	}

}
