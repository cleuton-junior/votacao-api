package br.com.sicredi.votacao.dto;

import java.math.BigInteger;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PautaDTO {
    private BigInteger id;
    private String descricaoPauta;
    private LocalDateTime dtVotacaoFim;

}
