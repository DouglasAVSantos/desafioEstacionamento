package com.br.desafio.estacionamento.vaga.dto;

import com.br.desafio.estacionamento.vaga.Estado;

import java.time.LocalDateTime;

public record VagaResponse(Long id, String codigo, Estado estado, LocalDateTime criadoEm) {



}
