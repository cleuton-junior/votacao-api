package br.com.sicredi.votacao.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SessaoDTO {

    private Long id;
    private LocalDateTime dataInicio;
    private Long minutosValidade;
    private Long idPauta;
}
