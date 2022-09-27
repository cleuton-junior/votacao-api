package br.com.sicredi.votacao.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import br.com.sicredi.votacao.dto.SessaoDTO;
import br.com.sicredi.votacao.exception.SessaoNotFoundException;
import br.com.sicredi.votacao.model.Pauta;
import br.com.sicredi.votacao.model.Sessao;
import br.com.sicredi.votacao.repository.PautaRepository;
import br.com.sicredi.votacao.repository.SessaoRepository;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class SessaoService {

    private SessaoRepository sessaoRepository;
    private PautaRepository pautaRepository;
    private ModelMapper modelMapper;

    public List<SessaoDTO> listarSessoes() {
        return sessaoRepository.findAll().stream().map(sessao -> modelMapper.map(sessao, SessaoDTO.class))
				.collect(Collectors.toList());
    }

	public Sessao criarSessao(SessaoDTO sessaoDTO) {
		Optional<Pauta> findById = pautaRepository.findById(sessaoDTO.getIdPauta());
		if (!findById.isPresent()) {
			throw new IllegalArgumentException("Pauta não encontrada.");
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

    public void excluir(Long id) {
        Optional<Sessao> sessaoById = sessaoRepository.findById(id);
        if (!sessaoById.isPresent()) {
        	throw new SessaoNotFoundException();
        }
        sessaoRepository.delete(sessaoById.get());
    }

    public void excluirByPautaId(Long id) {
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

    public SessaoDTO retornaSessaoPorPautaId(Long idSessao, Long pautaId) {
    	Optional<Sessao> sessao = sessaoRepository.findByIdAndPautaId(idSessao, pautaId);
        if(!sessao.isPresent()){
        	throw new SessaoNotFoundException();
        }
        
        return modelMapper.map(sessao.get(), SessaoDTO.class);
    }
}
