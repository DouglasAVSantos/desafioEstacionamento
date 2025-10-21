package com.br.desafio.estacionamento.shared.dto;

import org.springframework.http.HttpStatus;

import java.util.List;

public record ErroResponse(int status, String mensagem, List<ErroCampo> erros) {

    public static ErroResponse mensagemCampo(String campo, String mensagemErro){
        return new ErroResponse(HttpStatus.BAD_REQUEST.value(), "erro", List.of(new ErroCampo(campo, mensagemErro)));
    }

        public static ErroResponse mensagemDefault(String mensagemErro){
        return new ErroResponse(HttpStatus.BAD_REQUEST.value(), "erro", List.of());
    }
}
