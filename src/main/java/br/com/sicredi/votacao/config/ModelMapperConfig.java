package br.com.sicredi.votacao.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import br.com.sicredi.votacao.dto.PautaDTO;
import br.com.sicredi.votacao.dto.VotoDTO;
import br.com.sicredi.votacao.model.Pauta;
import br.com.sicredi.votacao.model.Voto;

@Configuration
public class ModelMapperConfig {

  @Bean
  public ModelMapper getModelMapper() {
    ModelMapper modelMapper = new ModelMapper();

    modelMapper.createTypeMap(PautaDTO.class, Pauta.class);
    modelMapper.createTypeMap(VotoDTO.class, Voto.class);

    return modelMapper;
  }
}
