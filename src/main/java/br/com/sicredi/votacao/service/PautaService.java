package br.com.sicredi.votacao.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.sicredi.votacao.dto.PautaDTO;
import br.com.sicredi.votacao.exception.PautaNotFoundException;
import br.com.sicredi.votacao.model.Pauta;
import br.com.sicredi.votacao.repository.PautaRepository;

@Service
public class PautaService {

	@Autowired
	private PautaRepository pautaRepository;
	@Autowired
	private VotoService votoService;
	@Autowired
	private SessaoService sessaoService;
	@Autowired
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
	
	public void delete(Long id) {
		Optional<Pauta> pautaOpt = pautaRepository.findById(id);
		if (!pautaOpt.isPresent()) {
			throw new PautaNotFoundException();
		}

		pautaRepository.delete(pautaOpt.get());
		sessaoService.deleteByPautaId(id);
		votoService.deleteByPautaId(id);
	}

}
