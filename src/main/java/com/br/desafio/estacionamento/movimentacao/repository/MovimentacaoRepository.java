package com.br.desafio.estacionamento.movimentacao.repository;

import com.br.desafio.estacionamento.movimentacao.dto.MovimentacaoResponse;
import com.br.desafio.estacionamento.movimentacao.entity.Movimentacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface MovimentacaoRepository extends JpaRepository<Movimentacao, Long> {

    Optional<Movimentacao> findByVeiculoPlacaAndSaidaIsNull(String placa);

    List<MovimentacaoResponse> findAllByVeiculoPlaca(String placa);

    List<MovimentacaoResponse> findAllByEntradaBetween(LocalDateTime inicio, LocalDateTime fim);

    List<MovimentacaoResponse> findAllBySaidaBetween(LocalDateTime inicio, LocalDateTime fim);

    List<MovimentacaoResponse> findAllBySaidaIsNull();

    @Query(value = """
            
            SELECT * FROM movimentacao m
            WHERE (m.entrada >= :inicio and m.saida <= :fim)
            or
             (m.saida is null and m.entrada <= :fim)
            
            """, nativeQuery = true)
    List<MovimentacaoResponse> findAllBetween(LocalDateTime inicio, LocalDateTime fim);


    List<MovimentacaoResponse> getAll();

}
