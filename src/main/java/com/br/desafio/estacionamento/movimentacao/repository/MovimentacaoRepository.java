package com.br.desafio.estacionamento.movimentacao.repository;

import com.br.desafio.estacionamento.movimentacao.entity.Movimentacao;
import com.br.desafio.estacionamento.vaga.entity.Estado;
import com.br.desafio.estacionamento.veiculo.entity.TipoVeiculo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface MovimentacaoRepository extends JpaRepository<Movimentacao, Long> {

    Optional<Movimentacao> findByVeiculoPlacaAndSaidaIsNull(String placa);


    List<Movimentacao> findAllByVagaEstado(Estado estado);

    List<Movimentacao> findAllByVeiculoPlaca(String placa);

    List<Movimentacao> findAllByEntradaBetween(LocalDateTime inicio, LocalDateTime fim);

    List<Movimentacao> findAllBySaidaIsNull();

    List<Movimentacao> findAllBySaidaIsNullAndVeiculoTipo(TipoVeiculo tipo);


}
