package com.br.desafio.estacionamento.vaga.mapper;

import com.br.desafio.estacionamento.vaga.dto.VagaRequest;
import com.br.desafio.estacionamento.vaga.dto.VagaResponse;
import com.br.desafio.estacionamento.vaga.entity.Vaga;
import org.springframework.stereotype.Component;

@Component
public class VagaMapper {

    public static Vaga toEntity(VagaRequest request) {
        return Vaga.of(request);
    }

    public static VagaResponse toResponse(Vaga vaga) {

        return new VagaResponse(
                vaga.getId(),
                vaga.getCodigo(),
                vaga.getEstado().name(),
                vaga.getCriadoEm()
        );
    }
}
