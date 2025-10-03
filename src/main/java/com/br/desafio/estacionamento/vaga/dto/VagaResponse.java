package com.br.desafio.estacionamento.vaga.dto;

import java.time.LocalDateTime;

public record VagaResponse(Long id, String codigo, String estado, LocalDateTime dataCriacao) {


}
