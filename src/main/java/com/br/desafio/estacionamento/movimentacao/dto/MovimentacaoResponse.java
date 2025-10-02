package com.br.desafio.estacionamento.movimentacao.dto;

import com.br.desafio.estacionamento.vaga.dto.VagaResponse;
import com.br.desafio.estacionamento.veiculo.dto.VeiculoResponse;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record MovimentacaoResponse(
        Long id,
        LocalDateTime entrada,
        LocalDateTime saida,
        VagaResponse vaga,
        VeiculoResponse veiculo,
        BigDecimal valor
) {

}
