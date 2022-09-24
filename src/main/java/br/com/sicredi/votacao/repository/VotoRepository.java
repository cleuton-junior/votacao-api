package br.com.sicredi.votacao.repository;

import java.math.BigInteger;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.com.sicredi.votacao.dto.VotacaoDTO;
import br.com.sicredi.votacao.model.Voto;

public interface VotoRepository extends JpaRepository<Voto, BigInteger> {

    /*static final String QUERY_SUM =
            "SELECT new br.com.sicredi.votacao.business.dto.VotacaoDTO(p.id, p.desPauta," +
                    " (SELECT count(1) FROM voto v where v.idPauta = p.id and v.indVotoSim = 1)," +
                    " (SELECT count(1) FROM voto v where v.idPauta = p.id and v.indVotoSim != 1))" +
                    " FROM pauta p" +
                    " WHERE p.id = ?1";*/

    @Query("SELECT e FROM VOTO e WHERE e.idPauta = ?1 AND e.numeroCpf = ?2")
    Optional<Voto> findVoto(BigInteger idPauta, Long cpf);

    Optional<Voto> findByIdPautaAndNumeroCpf(BigInteger idPauta, Long cpf);

    void deleteByIdPauta(BigInteger id);

    @Query("SELECT new br.com.sicredi.votacao.dto.VotacaoDTO(p.id, p.descricaoPauta," +
            " (SELECT count(1) FROM VOTO v where v.idPauta = p.id and v.indicadorVotoSim = 1)," +
            " (SELECT count(1) FROM VOTO v where v.idPauta = p.id and v.indicadorVotoSim != 1))" +
            " FROM PAUTA p" +
            " WHERE p.id = ?1")
    VotacaoDTO sumVotes(BigInteger id);
}
