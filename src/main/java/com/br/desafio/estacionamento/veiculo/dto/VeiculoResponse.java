package com.br.desafio.estacionamento.veiculo.dto;

import com.br.desafio.estacionamento.veiculo.TipoVeiculo;

import java.time.LocalDateTime;

public record VeiculoResponse(
        Long id,
        String modelo,
        String placa,
        TipoVeiculo veiculo,
        LocalDateTime registradoEm
) {
}
