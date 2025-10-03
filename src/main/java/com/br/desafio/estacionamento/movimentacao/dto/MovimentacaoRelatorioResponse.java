package com.br.desafio.estacionamento.movimentacao.dto;

import java.math.BigDecimal;
import java.sql.Timestamp;

public record MovimentacaoRelatorioResponse(
        Long id,
        Timestamp entrada,
        Timestamp saida,
        Long vagaId,
        String codigo,
        String estado,
        Long idVeiculo,
        String modelo,
        String placa,
        String tipo,
        BigDecimal valor
) {

}
