package br.com.sicredi.votacao.dto;

import java.io.Serializable;

import br.com.sicredi.votacao.model.Pauta;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class VotacaoDTO implements Serializable{
	private static final long serialVersionUID = -6178565792109945596L;
	
	private Pauta pauta;
    private Integer qtdSim;
    private Integer qtdNao;
    private Integer totalVotos;
	private Integer totalSessoes;
}
