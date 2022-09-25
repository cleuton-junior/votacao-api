package br.com.sicredi.votacao.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Entity
@EqualsAndHashCode
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "PAUTA")
public class Pauta implements Serializable{

	@Id
	@SequenceGenerator(name = "SQ_PAUTA", sequenceName = "sq_pauta", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SQ_PAUTA")
    private Long id;

	@NotBlank(message = "A descrição da Pauta é obrigatório.")
    @Column(name = "DES_PAUTA", nullable = false, length = 50)
    private String descricaoPauta;

}
