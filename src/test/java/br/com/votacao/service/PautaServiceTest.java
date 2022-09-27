package br.com.votacao.service;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import static java.util.Arrays.asList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.internal.matchers.apachecommons.ReflectionEquals;
import org.modelmapper.ModelMapper;

import br.com.sicredi.votacao.dto.PautaDTO;
import br.com.sicredi.votacao.exception.PautaNotFoundException;
import br.com.sicredi.votacao.model.Pauta;
import br.com.sicredi.votacao.repository.PautaRepository;
import br.com.sicredi.votacao.service.PautaService;
import br.com.sicredi.votacao.service.SessaoService;
import br.com.sicredi.votacao.service.VotoService;

public class PautaServiceTest {

	@Mock
	private PautaRepository pautaRepository;
	@InjectMocks
	private PautaService pautaService;
	@Mock
	private SessaoService sessaoService;
	@Mock
	private VotoService votoService;
	@Spy
    private ModelMapper modelMapper;

	@BeforeEach
	public void init() {
		MockitoAnnotations.initMocks(this);
		this.pautaService = new PautaService(pautaRepository, votoService, sessaoService, modelMapper);
	}
	
	private static Pauta criaPauta(Long id, String nome) {
		Pauta pauta = new Pauta();
		pauta.setId(Long.valueOf(id));
		pauta.setDescricaoPauta(nome);
		return pauta;
	}
	
	public static PautaDTO criaPautaDto(Pauta pauta) {
		PautaDTO dto = new PautaDTO();
		dto.setId(null);
		dto.setDescricaoPauta(pauta.getDescricaoPauta());
		return dto;
	}
	
	private static Pauta criaPauta() {
		return criaPauta(Long.valueOf(1), "Pauta 1");
	}

	private static Pauta criaPautaDois() {
		return criaPauta(Long.valueOf(2), "Pauta 2");
	}

	@Test
	@DisplayName("Verifica criar pauta")
	void verificaPautaCriadaComSucesso() {
		Pauta pauta = criaPauta();
		PautaDTO pautaDTO = criaPautaDto(pauta);		
		when(this.pautaRepository.save(any(Pauta.class))).thenReturn(pauta);
		assertNotNull(this.pautaService.criar(pautaDTO));
			
	}

	@Test
	@DisplayName("Verifica deletar pauta")
	void deletarPauta() throws PautaNotFoundException {
		Long id = Long.valueOf(1);
		Pauta pauta = criaPauta();
		when(this.pautaRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(pauta));
		this.pautaService.excluir(id);
		verify(this.pautaRepository).findById(id);
	}

	@Test
	@DisplayName("Verifica deletar pauta inexistente")
	void deletarPautaInexistente() throws PautaNotFoundException {
		when(this.pautaRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());
		assertThrows(PautaNotFoundException.class, () -> this.pautaService.excluir(Long.valueOf(1)), "Pauta inexistente");
	}

	@Test
	@DisplayName("Verifica obter todas pautas")
	void obterTodos() {
		List<Pauta> pautas = asList(criaPauta(), criaPautaDois());
		when(this.pautaRepository.findAll()).thenReturn(pautas);
		List<PautaDTO> pautasRetornadas = this.pautaService.listarPautas();
		assertEquals(pautas.size(), pautasRetornadas.size());
	}

	@Test
	@DisplayName("Verifica obter pauta por id")
	void obterPautaPorId() throws PautaNotFoundException {
		Pauta pauta = criaPauta();
		when(this.pautaRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(pauta));
		PautaDTO pautaRetornada = this.pautaService.retornaPauta(pauta.getId());
		assertNotNull(pautaRetornada);
	}

	@Test
	@DisplayName("Verifica obter pauta por id inexistente")
	void obterPautaPorIdInexistente() throws PautaNotFoundException {
		when(this.pautaRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());
		assertThrows(PautaNotFoundException.class, () -> this.pautaService.retornaPauta(Long.valueOf(1)), "Pauta inexistente");
	}
}
