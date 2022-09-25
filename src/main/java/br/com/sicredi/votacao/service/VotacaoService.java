package br.com.sicredi.votacao.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.sicredi.votacao.dto.VotacaoDTO;
import br.com.sicredi.votacao.model.Pauta;
import br.com.sicredi.votacao.model.Voto;
import br.com.sicredi.votacao.repository.SessaoRepository;
import br.com.sicredi.votacao.repository.VotoRepository;

@Service
public class VotacaoService {

	@Autowired
	private VotoRepository votoRepository;
	@Autowired
	private SessaoRepository sessaoRepository;

	
	public VotacaoDTO getResultVotacao(Long id){
		VotacaoDTO votacaoPauta = buildVotacaoPauta(id);
		return votacaoPauta;
	}
	
	public VotacaoDTO buildVotacaoPauta(Long id) {
		Optional<List<Voto>> votosByPauta = votoRepository.findByPautaId(id);
		if (!votosByPauta.isPresent() || votosByPauta.get().isEmpty()) {
			return null;
		}

		Pauta pauta = votosByPauta.get().iterator().next().getPauta();

		Long totalSessoes = sessaoRepository.countByPautaId(pauta.getId());
		Integer total = votosByPauta.get().size();
		Integer totalSim = (int) votosByPauta.get().stream()
				.filter(voto -> Boolean.TRUE.equals(voto.getIndicadorVotoSim())).count();
		Integer totalNao = total - totalSim;

		return VotacaoDTO.builder().pauta(pauta).totalVotos(total).totalSessoes(totalSessoes.intValue())
				.qtdSim(totalSim).qtdNao(totalNao).build();

	}

}
