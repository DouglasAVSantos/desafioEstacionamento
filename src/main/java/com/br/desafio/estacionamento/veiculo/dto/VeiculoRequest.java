package com.br.desafio.estacionamento.veiculo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record VeiculoRequest(
        @NotBlank(message = "CAMPO OBRIGATORIO")
        String modelo,
        @NotBlank(message = "CAMPO OBRIGATORIO")
        String placa,
        @NotNull(message = "CAMPO OBRIGATORIO")
        String tipo) {

    public VeiculoRequest {
        modelo = modelo.trim().toUpperCase();
        placa = placa.trim().toUpperCase();
        tipo = tipo.trim().toLowerCase();
    }


}
