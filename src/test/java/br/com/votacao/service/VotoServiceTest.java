package br.com.votacao.service;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeoutException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import br.com.sicredi.votacao.config.RestTemplateConfig;
import br.com.sicredi.votacao.dto.SessaoDTO;
import br.com.sicredi.votacao.dto.StatusCpfDTO;
import br.com.sicredi.votacao.dto.VotacaoDTO;
import br.com.sicredi.votacao.dto.VotoDTO;
import br.com.sicredi.votacao.exception.InvalidCpfException;
import br.com.sicredi.votacao.exception.VotoExistsException;
import br.com.sicredi.votacao.model.Pauta;
import br.com.sicredi.votacao.model.Voto;
import br.com.sicredi.votacao.repository.VotoRepository;
import br.com.sicredi.votacao.service.SessaoService;
import br.com.sicredi.votacao.service.VotacaoService;
import br.com.sicredi.votacao.service.VotoService;

public class VotoServiceTest {

	private VotoService votoService;
	@Mock
	private VotoRepository votoRepository;
	@Mock
	private RestTemplateConfig restTemplate;
	@Mock
	private SessaoService sessaoService;
	@Spy
    private ModelMapper modelMapper;
	@Mock
	private VotacaoService votacaoService;

	@BeforeEach
	public void init() {
		MockitoAnnotations.initMocks(this);
		this.votacaoService = Mockito.mock(VotacaoService.class);
		this.votoService = new VotoService(restTemplate, votoRepository, sessaoService, modelMapper);
	}
		
	private static Voto criaVotoDois() {
		return criaVoto(Long.valueOf(2),"4321", true);
	}

	private static Voto criaVoto() {
		return criaVoto(Long.valueOf(1),"1234", true);
	}
	
	private static Voto criaVoto(Long id, String cpf, Boolean escolha) {
		Voto voto = new Voto();
		voto.setId(Long.valueOf(id));
		Pauta pauta = new Pauta();
		pauta.setId(1L);
		voto.setPauta(pauta);
		voto.setNumeroCpf(cpf);
		voto.setIndicadorVotoSim(escolha);
		return voto;
	}
	
	@Test
	public void verificaVotoTest() {
		SessaoDTO sessao = new SessaoDTO();
		sessao.setDataInicio(LocalDateTime.now());
		sessao.setMinutosValidade(-1L);

		VotoDTO voto = new VotoDTO();
		voto.setIdPauta(1L);

		when(votacaoService.buildVotacaoPauta(anyLong())).thenReturn(VotacaoDTO.builder().build());

		assertThrows(TimeoutException.class, () -> votoService.verificaVoto(sessao, voto));
	}

	@Test
	public void cpfAbleToVoteTest() throws InvalidCpfException{
		VotoDTO voto = new VotoDTO();
		voto.setNumeroCpf("1234");

		StatusCpfDTO cpf = new StatusCpfDTO();
		cpf.setStatus("TESTE");

		ResponseEntity<StatusCpfDTO> response = new ResponseEntity<>(cpf, HttpStatus.NOT_FOUND);

		when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(), eq(StatusCpfDTO.class)))
				.thenReturn(response);

		assertThrows(InvalidCpfException.class,() ->votoService.verificaCpfVoto(voto));
	}

	@Test
	public void cpfAbleToVote2Test() {
		VotoDTO voto = new VotoDTO();
		voto.setNumeroCpf("1234");

		StatusCpfDTO cpf = new StatusCpfDTO();
		cpf.setStatus("UNABLE_TO_VOTE");

		ResponseEntity<StatusCpfDTO> response = new ResponseEntity<>(cpf, HttpStatus.OK);

		when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(), eq(StatusCpfDTO.class)))
				.thenReturn(response);

		votoService.verificaCpfVoto(voto);
	}

	@Test
	public void cpfAbleToVote3Test() {
		VotoDTO voto = new VotoDTO();
		voto.setNumeroCpf("1234");

		StatusCpfDTO cpf = new StatusCpfDTO();
		cpf.setStatus("ABLE_TO_VOTE");

		ResponseEntity<StatusCpfDTO> response = new ResponseEntity<>(cpf, HttpStatus.OK);

		when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(), eq(StatusCpfDTO.class)))
				.thenReturn(response);

		votoService.validaCpf(voto);
	}

	@Test
	public void votoAlreadyExistsTest() throws VotoExistsException{
		VotoDTO dto = new VotoDTO();
		dto.setNumeroCpf("1234");
		dto.setIdPauta(1L );
		
		Voto voto = new Voto();
		Pauta pauta = new Pauta();
		pauta.setId(1L);
		voto.setPauta(pauta);
		
		when(votoRepository.findByNumeroCpfAndPautaId(anyString(), anyLong())).thenReturn(Optional.of(voto));
		assertThrows(VotoExistsException.class, () -> votoService.votoJaExiste(dto));
	}

	@Test
	public void votoAlreadyExistssTest() throws VotoExistsException{
		VotoDTO voto = new VotoDTO();
		voto.setNumeroCpf("123");
		voto.setIdPauta(1L );
		
		when(votoRepository.findByNumeroCpfAndPautaId(anyString(), anyLong())).thenReturn(Optional.empty());
		votoService.votoJaExiste(voto);
	}
	
	@Test
	@DisplayName("Verifica obter todos os votos")
	void obterTodos() {
		List<Voto> votos = asList(criaVoto(), criaVotoDois());
		when(this.votoRepository.findAll()).thenReturn(votos);
		List<VotoDTO> votosRetornados = this.votoService.listarVotos();
		assertEquals(votos.size(), votosRetornados.size());
	}


}
