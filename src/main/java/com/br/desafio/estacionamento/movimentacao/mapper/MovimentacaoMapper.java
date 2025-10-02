package com.br.desafio.estacionamento.movimentacao.mapper;

import com.br.desafio.estacionamento.movimentacao.dto.MovimentacaoRequest;
import com.br.desafio.estacionamento.movimentacao.dto.MovimentacaoResponse;
import com.br.desafio.estacionamento.movimentacao.entity.Movimentacao;
import com.br.desafio.estacionamento.vaga.dto.VagaMapper;
import com.br.desafio.estacionamento.vaga.entity.Vaga;
import com.br.desafio.estacionamento.veiculo.dto.VeiculoMapper;
import com.br.desafio.estacionamento.veiculo.entity.Veiculo;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Component
public class MovimentacaoMapper {

    public MovimentacaoMapper() {
    }

    public Movimentacao toEntity(MovimentacaoRequest request, Vaga vaga, Veiculo veiculo, BigDecimal valorTotal){
        Movimentacao m = new Movimentacao();
        m.setEntrada(LocalDateTime.now());
        m.setVaga(vaga);
        m.setVeiculo(veiculo);
        m.setValor(valorTotal);
        return m;
        }

        public MovimentacaoResponse toResponse(Movimentacao movimentacao){
        return new MovimentacaoResponse(
                movimentacao.getId(),
                movimentacao.getEntrada(),
                movimentacao.getSaida(),
                VagaMapper.toResponse(movimentacao.getVaga()),
                VeiculoMapper.toResponse(movimentacao.getVeiculo()),
                movimentacao.getValor()
        );
        }
}
