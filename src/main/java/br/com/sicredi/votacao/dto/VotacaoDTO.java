package br.com.sicredi.votacao.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class VotacaoDTO {
    private BigInteger idPauta;
    private String desPauta;
    private Long qtdSim;
    private Long qtdNao;
}
