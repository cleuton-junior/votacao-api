package br.com.sicredi.votacao.model;

import java.math.BigInteger;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@Entity(name = "PAUTA")
@Table(name = "PAUTA")
@SequenceGenerator(name = "SQ_PAUTA", sequenceName = "sequence_pauta", allocationSize = 1)
public class Pauta {

	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SQ_PAUTA")
    private BigInteger id;

    @Column(name = "DES_PAUTA", nullable = false, length = 50)
    private String descricaoPauta;

    @Column(name = "DT_VOTACAO_FIM")
    private LocalDateTime dtVotacaoFim;
}
