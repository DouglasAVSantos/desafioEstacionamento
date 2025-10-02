package com.br.desafio.estacionamento.veiculo.dto;

import com.br.desafio.estacionamento.veiculo.entity.TipoVeiculo;
import com.fasterxml.jackson.annotation.JsonCreator;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record VeiculoRequest (
        @NotBlank(message = "CAMPO OBRIGATORIO")
        String modelo,
        @NotBlank(message = "CAMPO OBRIGATORIO")
        String placa,
        @NotNull(message = "CAMPO OBRIGATORIO")
        TipoVeiculo tipo){

    public VeiculoRequest {
        modelo = modelo.trim().toUpperCase();
        placa = placa.trim().toUpperCase();
    }


}
