package com.br.desafio.estacionamento.vaga.dto;

import com.br.desafio.estacionamento.vaga.Estado;
import jakarta.validation.constraints.NotBlank;

import java.util.Objects;

public record VagaRequest(@NotBlank String codigo, Estado estado) {

    public VagaRequest {
        codigo = codigo.trim().toUpperCase();
        estado = Objects.requireNonNullElse(estado, Estado.LIVRE);
    }

}
