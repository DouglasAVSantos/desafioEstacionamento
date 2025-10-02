package com.br.desafio.estacionamento.veiculo.entity;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum TipoVeiculo {
    MOTO,CARRO,CAMINHAO,CAMINHONETE;

    @JsonCreator
    public static TipoVeiculo from(String request) {
        if (request == null || request.isBlank()) {
            return null;
        }
        return TipoVeiculo.valueOf(request.toUpperCase());
    }
}
