package com.br.desafio.estacionamento.movimentacao.mapper;

import com.br.desafio.estacionamento.movimentacao.dto.MovimentacaoResponse;
import com.br.desafio.estacionamento.movimentacao.entity.Movimentacao;
import com.br.desafio.estacionamento.vaga.mapper.VagaMapper;
import com.br.desafio.estacionamento.veiculo.mapper.VeiculoMapper;
import org.springframework.stereotype.Component;

@Component
public class MovimentacaoMapper {


    public MovimentacaoResponse toResponse(Movimentacao movimentacao) {
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
