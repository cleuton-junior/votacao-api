package br.com.sicredi.votacao.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import br.com.sicredi.votacao.dto.SessaoDTO;
import br.com.sicredi.votacao.exception.SessaoNotFoundException;
import br.com.sicredi.votacao.model.Pauta;
import br.com.sicredi.votacao.model.Sessao;
import br.com.sicredi.votacao.repository.PautaRepository;
import br.com.sicredi.votacao.repository.SessaoRepository;

@Service
public class SessaoService {

	@Autowired
    private SessaoRepository sessaoRepository;
	@Autowired
    private PautaRepository pautaRepository;
	@Autowired
    private ModelMapper modelMapper;

    public List<Sessao> listarSessoes() {
        return sessaoRepository.findAll();
    }

	public Sessao criarSessao(SessaoDTO sessaoDTO) {
		Optional<Pauta> findById = pautaRepository.findById(sessaoDTO.getIdPauta());
		if (!findById.isPresent()) {
			Assert.isNull(findById, "Pauta não encontrada");
			return null;
		}
		Sessao sessao = modelMapper.map(sessaoDTO, Sessao.class);
		sessao.setPauta(findById.get());

		return save(sessao);
	}

    private Sessao save(final Sessao sessao) {
        if (sessao.getDataInicio() == null) {
            sessao.setDataInicio(LocalDateTime.now());
        }
        if (sessao.getMinutosValidade() == null) {
            sessao.setMinutosValidade(1L);
        }

        return sessaoRepository.save(sessao);

    }

    public void delete(Long id) {
        Optional<Sessao> sessaoById = sessaoRepository.findById(id);
        if (!sessaoById.isPresent()) {
        	throw new SessaoNotFoundException();
        }
        sessaoRepository.delete(sessaoById.get());
    }

    void deleteByPautaId(Long id) {
        Optional<List<Sessao>> sessoes = sessaoRepository.findByPautaId(id);
        sessoes.ifPresent(sessaoList -> sessaoList.forEach(sessaoRepository::delete));
    }

	public SessaoDTO retornaSessao(Long id) {
		Optional<Sessao> sessaoOpt = sessaoRepository.findById(id);
		if (!sessaoOpt.isPresent()) {
			throw new SessaoNotFoundException();
		}
		SessaoDTO dto = modelMapper.map(sessaoOpt.get(), SessaoDTO.class);
		return dto;
	}

    public List<SessaoDTO> retornaSessaoPorPautaId(Long pautaId) {
    	Optional<List<Sessao>> sessoes = sessaoRepository.findByPautaId(pautaId);
        if(!sessoes.isPresent() || sessoes.get().isEmpty()){
        	throw new SessaoNotFoundException();
        }
        return sessoes.get().stream().map(sessao -> modelMapper.map(sessao, SessaoDTO.class))
				.collect(Collectors.toList());
    }
}
