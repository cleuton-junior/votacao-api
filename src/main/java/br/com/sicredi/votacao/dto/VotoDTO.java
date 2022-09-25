package br.com.sicredi.votacao.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class VotoDTO {
	
	private Long id;
    private Long idPauta;
    private String numeroCpf;
    private Boolean indicadorVotoSim;

}
