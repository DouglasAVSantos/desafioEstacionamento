package com.br.desafio.estacionamento.movimentacao.repository;

import com.br.desafio.estacionamento.movimentacao.dto.MovimentacaoRelatorioResponse;
import com.br.desafio.estacionamento.movimentacao.dto.MovimentacaoResponse;
import com.br.desafio.estacionamento.movimentacao.entity.Movimentacao;
import com.br.desafio.estacionamento.vaga.entity.Vaga;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface MovimentacaoRepository extends JpaRepository<Movimentacao, Long> {

    String SELECT_PADRAO = """
              Select
                    m.id,
                    m.entrada,
                    m.saida,
                    vagas.id,
                    vagas.codigo,
                    vagas.estado,
                    veiculos.id,
                    veiculos.modelo,
                    veiculos.placa,
                    veiculos.tipo,
                    m.valor
            from movimentacao m
                    join vagas on vagas.id = m.vaga_id
                    join veiculos on veiculos.id = m.veiculo_id
                    
                    """;


    Optional<Movimentacao> findByVeiculoPlacaAndSaidaIsNull(String placa);

    @Query(value = SELECT_PADRAO + """ 
             WHERE veiculos.placa = :placa""",nativeQuery = true)
    List<MovimentacaoRelatorioResponse> findAllByVeiculoPlaca(@Param("placa")String placa);

    @Query(value = SELECT_PADRAO + """ 
            WHERE (m.entrada >= :inicio and m.entrada <= :fim)""",nativeQuery = true)
    List<MovimentacaoRelatorioResponse> findAllByEntradaBetween(@Param("inicio")LocalDateTime inicio, @Param("fim")LocalDateTime fim);

    @Query(value = SELECT_PADRAO + """ 
            WHERE (m.saida >= :inicio and m.saida <= :fim)""",nativeQuery = true)
    List<MovimentacaoRelatorioResponse> findAllBySaidaBetween(@Param("inicio")LocalDateTime inicio, @Param("fim")LocalDateTime fim);

    @Query(value = SELECT_PADRAO + """ 
            WHERE m.saida is null
            """, nativeQuery = true)
    List<MovimentacaoRelatorioResponse> findAllBySaidaIsNull();

    @Query(value = SELECT_PADRAO + """ 
            WHERE (m.entrada >= :inicio and m.saida <= :fim)
                  or
                  (m.saida is null and m.entrada <= :fim)
            """, nativeQuery = true)
    List<MovimentacaoRelatorioResponse> findAllBetween(@Param("inicio")LocalDateTime inicio, @Param("fim")LocalDateTime fim);

    @Query(value = SELECT_PADRAO, nativeQuery = true)
    List<MovimentacaoRelatorioResponse> getAll();

}
