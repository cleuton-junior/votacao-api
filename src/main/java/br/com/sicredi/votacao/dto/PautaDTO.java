package br.com.sicredi.votacao.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PautaDTO {
    private Long id;
    private String descricaoPauta;

}
