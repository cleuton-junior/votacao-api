package br.com.sicredi.votacao.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.sicredi.votacao.model.Voto;

public interface VotoRepository extends JpaRepository<Voto, Long> {

	Optional<List<Voto>> findByPautaId(Long id);

}
