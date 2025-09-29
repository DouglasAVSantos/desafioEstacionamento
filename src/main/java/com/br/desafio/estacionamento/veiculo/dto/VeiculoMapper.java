package com.br.desafio.estacionamento.veiculo.dto;

import com.br.desafio.estacionamento.veiculo.entity.Veiculo;
import org.springframework.stereotype.Component;

@Component
public class VeiculoMapper {

    public static Veiculo toEntity(VeiculoRequest request){
        return Veiculo.of(request);
    }

    public static VeiculoResponse toResponse(Veiculo entity){
        return new VeiculoResponse(entity);
    }
}
