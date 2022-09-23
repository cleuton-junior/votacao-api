package br.com.sicredi.votacao.model;

import java.math.BigInteger;

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
@Entity(name = "VOTO")
@Table(name = "VOTO")
@SequenceGenerator(name = "SQ_VOTO", sequenceName = "sequence_voto", allocationSize = 1)
public class Voto {
	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SQ_VOTO")
    private BigInteger id;

    @Column(name = "ID_PAUTA")
    private BigInteger idPauta;

    @Column(name = "NUM_CPF")
    private Long numeroCpf;

    @Column(name = "INDQ_VOTO_SIM")
    private Integer indicadorVotoSim;
}
