package com.br.desafio.estacionamento.veiculo.dto;

import com.br.desafio.estacionamento.veiculo.entity.TipoVeiculo;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record VeiculoRequest (
        @NotBlank
        String modelo,
        @NotBlank
        String placa,
        @NotNull
        TipoVeiculo tipo){

    public VeiculoRequest {
        modelo = modelo.trim().toUpperCase();
        placa = placa.trim().toUpperCase();
    }
}
