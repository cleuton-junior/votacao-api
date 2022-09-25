package br.com.sicredi.votacao.model;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

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
@Table(name = "SESSAO")
public class Sessao implements Serializable{

    @Id
    @SequenceGenerator(name = "SQ_SESSAO", sequenceName = "sq_sessao", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SQ_SESSAO")
    private Long id;

    private LocalDateTime dataInicio;

    private Long minutosValidade;

    @ManyToOne(fetch = FetchType.EAGER)
    private Pauta pauta;

    public Sessao pauta(Pauta pauta) {
        this.pauta = pauta;
        return this;
    }
}
