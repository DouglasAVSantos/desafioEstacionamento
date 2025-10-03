package com.br.desafio.estacionamento.veiculo.mapper;

import com.br.desafio.estacionamento.veiculo.dto.VeiculoRequest;
import com.br.desafio.estacionamento.veiculo.dto.VeiculoResponse;
import com.br.desafio.estacionamento.veiculo.entity.TipoVeiculo;
import com.br.desafio.estacionamento.veiculo.entity.Veiculo;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;

@Component
public class VeiculoMapper {

    private static TipoVeiculo toTipo(String input){
        return Arrays.stream(TipoVeiculo.values())
                .filter(e -> e.name().equalsIgnoreCase(input))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "Tipo inválido. \nTipos aceitos: " + Arrays.toString(TipoVeiculo.values())
                ));
    }

    public static Veiculo toEntity(VeiculoRequest request) {
        return new Veiculo(request.modelo(), request.placa(), toTipo(request.tipo()));
    }

    public static Veiculo toAttEntity(Veiculo veiculo , VeiculoRequest request) {
        veiculo.setPlaca(request.placa());
        veiculo.setModelo(request.modelo());
        veiculo.setTipo(toTipo(request.tipo()));
        veiculo.setRegistradoEm(LocalDateTime.now());
        return veiculo;
    }

    public static VeiculoResponse toResponse(Veiculo entity) {
        return new VeiculoResponse(entity);
    }
}
