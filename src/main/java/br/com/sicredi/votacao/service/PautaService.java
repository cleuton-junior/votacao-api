package br.com.sicredi.votacao.service;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import br.com.sicredi.votacao.dto.VotacaoDTO;
import br.com.sicredi.votacao.model.Pauta;
import br.com.sicredi.votacao.repository.PautaRepository;
import br.com.sicredi.votacao.repository.VotoRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PautaService {

	@Autowired
	private PautaRepository pautaRepository;
	@Autowired
	private VotoRepository votoRepository;

	public Pauta criar(String descricaoPauta) {
		Pauta pauta = pautaRepository.findByDescricaoPautaIgnoreCase(descricaoPauta);
		Assert.isNull(pauta, "Pauta já cadastrada.");
		pauta = new Pauta();
		pauta.setDescricaoPauta(descricaoPauta);
		return pautaRepository.save(pauta);
	}

	public Pauta recuperarPauta(BigInteger id) {
		Optional<Pauta> pautaOpt = pautaRepository.findById(id);
		Assert.isTrue(pautaOpt.isPresent(), "Pauta não encontrada.");
		return pautaOpt.get();
	}

	public List<Pauta> listarPautas() {
		return pautaRepository.findAll();
	}

	public void abrirPauta(BigInteger idPauta, Integer qtdMinutos) {
		log.debug("Inicio abertura de votacao pauta:" + idPauta);

		Optional<Pauta> pautaOpt = pautaRepository.findById(idPauta);
		Assert.isTrue(pautaOpt.isPresent(), "Pauta não encontrada.");

		Pauta pauta = pautaOpt.get();

		pauta.setDtVotacaoFim(LocalDateTime.now().plusMinutes(qtdMinutos));
		pautaRepository.save(pauta);

		votoRepository.deleteByIdPauta(pauta.getId());

		log.debug("Fim abertura de votacao pauta:" + idPauta);

	}

	public VotacaoDTO fecharPauta(BigInteger idPauta) {
		log.debug("Inicio fechamento pauta:" + idPauta);
		Optional<Pauta> pautaOptional = pautaRepository.findById(idPauta);
		Assert.isTrue(pautaOptional.isPresent(), "Pauta não encontrada.");
		Pauta pauta = pautaOptional.get();

		pauta.setDtVotacaoFim(LocalDateTime.now());
		pautaRepository.save(pauta);

		VotacaoDTO dto = votoRepository.sumVotes(pauta.getId());
		log.debug("Fim fechamento pauta id=" + dto.getIdPauta() + " - S=" + dto.getQtdSim() + ", N=" + dto.getQtdNao());
		return dto;
	}
}
