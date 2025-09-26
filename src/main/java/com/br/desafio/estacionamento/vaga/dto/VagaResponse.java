package com.br.desafio.estacionamento.vaga.dto;

import com.br.desafio.estacionamento.vaga.entity.Vaga;

import java.time.LocalDateTime;

public record VagaResponse(Long id, String codigo, String estado, LocalDateTime dataCriacao) {


    public static VagaResponse of(Long id, String codigo, String estado, LocalDateTime dataCriacao) {
        return new VagaResponse(
                id,
                codigo,
                estado,
                dataCriacao);
    }

    public static VagaResponse of(Vaga vaga) {
        return of(vaga.getId(), vaga.getCodigo(), vaga.getEstado().name(), vaga.getCriadoEm());
    }
}
