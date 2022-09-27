package br.com.sicredi.votacao.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import br.com.sicredi.votacao.dto.PautaDTO;
import br.com.sicredi.votacao.exception.PautaNotFoundException;
import br.com.sicredi.votacao.model.Pauta;
import br.com.sicredi.votacao.repository.PautaRepository;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class PautaService {
	
	private PautaRepository pautaRepository;	
	private VotoService votoService;	
	private SessaoService sessaoService;	
    private ModelMapper modelMapper;

	public Pauta criar(PautaDTO pautaDTO) {
		Pauta pauta = modelMapper.map(pautaDTO, Pauta.class);
		return pautaRepository.save(pauta);
	}

	public PautaDTO retornaPauta(Long id) {
		Optional<Pauta> pautaOpt = pautaRepository.findById(id);
		if (!pautaOpt.isPresent()) {
			throw new PautaNotFoundException();
		}
		PautaDTO dto = modelMapper.map(pautaOpt.get(), PautaDTO.class);
		return dto;
	}

	public List<PautaDTO> listarPautas() {
		return pautaRepository.findAll().stream().map(pauta -> modelMapper.map(pauta, PautaDTO.class))
				.collect(Collectors.toList());
	}
	
	public void excluir(Long id) {
		Optional<Pauta> pautaOpt = pautaRepository.findById(id);
		if (!pautaOpt.isPresent()) {
			throw new PautaNotFoundException();
		}

		sessaoService.excluirByPautaId(id);
		votoService.excluirByPautaId(id);
		pautaRepository.delete(pautaOpt.get());
	}

}
