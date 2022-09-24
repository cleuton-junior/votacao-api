package br.com.sicredi.votacao.repository;

import java.math.BigInteger;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.sicredi.votacao.model.Pauta;

public interface PautaRepository extends JpaRepository<Pauta, BigInteger> {

    Pauta findByDescricaoPautaIgnoreCase(String desPauta);

}

