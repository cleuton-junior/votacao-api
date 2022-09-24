package br.com.sicredi.votacao.dto;

import java.math.BigInteger;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class VotoDTO {
	
    private BigInteger idPauta;
    private Long numeroCpf;
    private Integer indicadorVotoSim;

}
