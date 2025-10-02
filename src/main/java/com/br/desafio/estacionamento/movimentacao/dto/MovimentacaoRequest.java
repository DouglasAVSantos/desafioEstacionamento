package com.br.desafio.estacionamento.movimentacao.dto;

import com.br.desafio.estacionamento.vaga.entity.Vaga;
import com.br.desafio.estacionamento.veiculo.entity.Veiculo;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record MovimentacaoRequest(
        @NotBlank(message = "Vaga é obrigatório")
        String codigoVaga,
        @NotBlank(message = "Veiculo é obrigatório") String placaVeiculo) {
}
