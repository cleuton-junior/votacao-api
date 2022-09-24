package br.com.sicredi.votacao.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import br.com.sicredi.votacao.model.Pauta;
import br.com.sicredi.votacao.repository.PautaRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PautaService {

	@Autowired
	private PautaRepository pautaRepository;
	@Autowired
	private VotoService votoService;

	public Pauta criar(Pauta pauta) {
		Pauta pautaTest = pautaRepository.findByDescricaoPautaIgnoreCase(pauta.getDescricaoPauta());
		Assert.isNull(pautaTest, "Pauta já cadastrada.");
		return pautaRepository.save(pauta);
	}

	public Pauta recuperarPauta(Long id) {
		Optional<Pauta> pautaOpt = pautaRepository.findById(id);
		Assert.isTrue(pautaOpt.isPresent(), "Pauta não encontrada.");
		return pautaOpt.get();
	}

	public List<Pauta> listarPautas() {
		return pautaRepository.findAll();
	}
	
	public void delete(Long id) {
		Optional<Pauta> pautaOpt = pautaRepository.findById(id);
		Assert.isTrue(pautaOpt.isPresent(), "Pauta não encontrada.");
        pautaRepository.delete(pautaOpt.get());
        votoService.deleteByPautaId(id);
    }

	public void abrirPauta(Long idPauta, Integer qtdMinutos) {
		log.debug("Inicio abertura de votacao pauta:" + idPauta);

		Optional<Pauta> pautaOpt = pautaRepository.findById(idPauta);
		Assert.isTrue(pautaOpt.isPresent(), "Pauta não encontrada.");

		Pauta pauta = pautaOpt.get();

		pauta.setDtVotacaoFim(LocalDateTime.now().plusMinutes(qtdMinutos));
		pautaRepository.save(pauta);

		votoService.deleteByPautaId(pauta.getId());

		log.debug("Fim abertura de votacao pauta:" + idPauta);

	}

}
