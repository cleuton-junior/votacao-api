package br.com.votacao.service;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.modelmapper.ModelMapper;

import br.com.sicredi.votacao.dto.PautaDTO;
import br.com.sicredi.votacao.dto.SessaoDTO;
import br.com.sicredi.votacao.exception.PautaNotFoundException;
import br.com.sicredi.votacao.exception.SessaoNotFoundException;
import br.com.sicredi.votacao.model.Pauta;
import br.com.sicredi.votacao.model.Sessao;
import br.com.sicredi.votacao.repository.PautaRepository;
import br.com.sicredi.votacao.repository.SessaoRepository;
import br.com.sicredi.votacao.service.SessaoService;

public class SessaoServiceTest {
	
	@InjectMocks
	private SessaoService sessaoService;
	@Mock
	private SessaoRepository sessaoRepository;
	@Mock
	private PautaRepository pautaRepository;
	@Spy
	private ModelMapper modelMapper;

	
	@BeforeEach
	public void init() {
		MockitoAnnotations.initMocks(this);
		this.sessaoService = new SessaoService(sessaoRepository, pautaRepository, modelMapper);
	}
	
	public static SessaoDTO criarSessarDto(Sessao sessao) {
		SessaoDTO dto = new SessaoDTO();
		dto.setId(sessao.getId());
		dto.setIdPauta(sessao.getPauta().getId());
		dto.setDataInicio(sessao.getDataInicio());
		dto.setMinutosValidade(sessao.getMinutosValidade());
		return dto;
	}
	
	public static Sessao criarSessao(Long id, Pauta pauta, LocalDateTime dtInicio, Long mntValidade) {
		Sessao sessao = new Sessao();
		sessao.setId(id);
		sessao.setPauta(pauta);
		sessao.setDataInicio(dtInicio);
		sessao.setMinutosValidade(mntValidade);
		return sessao;
	}
	
	public static Sessao criarSessao() {
		Pauta pauta = new Pauta();
		pauta.setId(Long.valueOf(1));
		pauta.setDescricaoPauta("Pauta 1");
		return criarSessao(Long.valueOf(1), pauta, LocalDateTime.now(), Long.valueOf(5));
	}
	
	public static Sessao criarSessaoDois() {
		Pauta pauta = new Pauta();
		pauta.setId(Long.valueOf(2));
		pauta.setDescricaoPauta("Pauta 2");
		return criarSessao(Long.valueOf(2), pauta, LocalDateTime.now(), Long.valueOf(3));
	}
	
	
	@Test
	@DisplayName("Verifica criar sessão")
	void verificaPautaCriadaComSucesso() {
		Sessao sessao = criarSessao();
		SessaoDTO sessaoDTO = criarSessarDto(sessao);
		when(this.pautaRepository.findById(anyLong())).thenReturn(Optional.of(sessao.getPauta()));
		when(this.sessaoRepository.save(any(Sessao.class))).thenReturn(sessao);
		assertNotNull(this.sessaoService.criarSessao(sessaoDTO));
			
	}
	
	@Test
	@DisplayName("Verifica criar sessão")
	void verificaPautaCriadaComErro() {
		Sessao sessao = criarSessao();
		SessaoDTO sessaoDTO = criarSessarDto(sessao);
		when(this.pautaRepository.findById(anyLong())).thenReturn(Optional.empty());
		when(this.sessaoRepository.save(any(Sessao.class))).thenReturn(sessao);
		assertThrows(IllegalArgumentException.class, () -> this.sessaoService.criarSessao(sessaoDTO));
			
	}
	
	@Test
	@DisplayName("Verifica deletar sessão")
	void deletarSessao() {
		Long id = Long.valueOf(1);
		Sessao sessao = criarSessao();
		when(this.sessaoRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(sessao));
		this.sessaoService.excluir(id);
		verify(this.sessaoRepository).findById(id);
	}
	
	@Test
	@DisplayName("Verifica deletar sessão por pauta")
	void deletarSessaoPorPautaID() {
		Long id = Long.valueOf(1);
		List<Sessao> sessoes = asList(criarSessao(), criarSessaoDois());
		when(this.sessaoRepository.findByPautaId(Mockito.anyLong())).thenReturn(Optional.of(sessoes));
		this.sessaoService.excluirByPautaId(id);
	}
	
	@Test
	@DisplayName("Verifica deletar sessao inexistente")
	void deletarSessaoInexistente() throws SessaoNotFoundException {
		when(this.sessaoRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());
		assertThrows(SessaoNotFoundException.class, () -> this.sessaoService.excluir(Long.valueOf(1)), "Sessão inexistente");
	}

	@Test
	@DisplayName("Verifica obter todas as sessões")
	void obterTodos() {
		List<Sessao> sessoes = asList(criarSessao(), criarSessaoDois());
		when(this.sessaoRepository.findAll()).thenReturn(sessoes);
		List<SessaoDTO> sessoesRetornadas = this.sessaoService.listarSessoes();
		assertEquals(sessoes.size(), sessoesRetornadas.size());
	}
	
	@Test
	@DisplayName("Verifica obter sessão por id")
	void obterSessaoPorId() throws SessaoNotFoundException {
		Sessao sessao = criarSessao();
		when(this.sessaoRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(sessao));
		SessaoDTO sessaoRetornada = this.sessaoService.retornaSessao(sessao.getId());
		assertNotNull(sessaoRetornada);
	}
	
	@Test
	@DisplayName("Verifica obter sessão inexistente por id ")
	void obterSessaoPorIdInexistente() throws SessaoNotFoundException {
		when(this.sessaoRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());
		assertThrows(SessaoNotFoundException.class, () -> this.sessaoService.retornaSessao(Long.valueOf(1)), "Sessão inexistente");
	}
	
	@Test
	@DisplayName("Verifica obter sessão por pauta id")
	void obterSessaoPorPautaId() throws SessaoNotFoundException {
		Sessao sessao = criarSessao();
		when(this.sessaoRepository.findByIdAndPautaId(Mockito.anyLong(), Mockito.anyLong())).thenReturn(Optional.of(sessao));
		SessaoDTO sessaoRetornada = this.sessaoService.retornaSessaoPorPautaId(sessao.getId(), sessao.getPauta().getId());
		assertNotNull(sessaoRetornada);
	
	}
	
	@Test
	@DisplayName("Verifica obter sessão por pauta id inexistente")
	void obterSessaoPorPautaIdInexistente() throws SessaoNotFoundException {
		Sessao sessao = criarSessao();
		when(this.sessaoRepository.findByIdAndPautaId(Mockito.anyLong(), Mockito.anyLong())).thenReturn(Optional.empty());
		assertThrows(SessaoNotFoundException.class, () ->this.sessaoService.retornaSessaoPorPautaId(sessao.getId(), sessao.getPauta().getId()));
	
	}
	
}
