package com.br.desafio.estacionamento.veiculo.dto;

import com.br.desafio.estacionamento.veiculo.entity.Veiculo;

import java.time.format.DateTimeFormatter;

public record VeiculoResponse(
        Long id,
        String modelo,
        String placa,
        String veiculo,
        String registradoEm
) {

    public VeiculoResponse(Veiculo veiculo) {
        this(veiculo.getId(),
        veiculo.getModelo(),
        veiculo.getPlaca(),
        veiculo.getTipo().name(),
        veiculo.getRegistradoEm().format(DateTimeFormatter.ISO_DATE_TIME));
    }
}
