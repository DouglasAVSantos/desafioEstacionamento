package com.br.desafio.estacionamento.vaga.Dto;

import com.br.desafio.estacionamento.vaga.Entity.Estado;
import jakarta.validation.constraints.NotBlank;

public record VagaRequest(@NotBlank String codigo,Estado estado) {

    public VagaRequest{
        estado = Estado.LIVRE;
    }
}
