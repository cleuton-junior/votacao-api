package br.com.sicredi.votacao.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonSubTypes.Type;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@EqualsAndHashCode
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "VOTO")
public class Voto implements Serializable{
	@Id
	@SequenceGenerator(name = "SQ_VOTO", sequenceName = "sq_voto", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SQ_VOTO")
    private Long id;

	@NotNull(message = "Necessário informar a Pauta para o voto.")
    @ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "PAUTA")
    private Pauta pauta;

    @Column(name = "NUM_CPF")
    private String numeroCpf;

    @Column(name = "INDQ_VOTO_SIM")
    private Boolean indicadorVotoSim;
}
