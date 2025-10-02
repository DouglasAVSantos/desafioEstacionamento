package com.br.desafio.estacionamento.movimentacao.dto;

import jakarta.validation.constraints.NotBlank;

public record MovimentacaoRequest(
        @NotBlank(message = "Vaga é obrigatório")
        String codigoVaga,
        @NotBlank(message = "Veiculo é obrigatório") String placaVeiculo) {
}
