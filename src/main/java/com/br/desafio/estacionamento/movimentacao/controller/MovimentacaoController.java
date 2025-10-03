package com.br.desafio.estacionamento.movimentacao.controller;

import com.br.desafio.estacionamento.movimentacao.dto.MovimentacaoRelatorioResponse;
import com.br.desafio.estacionamento.movimentacao.dto.MovimentacaoRequest;
import com.br.desafio.estacionamento.movimentacao.dto.MovimentacaoResponse;
import com.br.desafio.estacionamento.movimentacao.service.MovimentacaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("api/v1/movimentacao")
@Tag(name = "Movimentação", description = "Endpoints para gerenciar o fluxo de entrada e saída de veículos")
public class MovimentacaoController {

    private final MovimentacaoService service;

    public MovimentacaoController(MovimentacaoService service) {
        this.service = service;
    }


    @Operation(summary = "Registrar a entrada de um veículo (Check-in)", description = "Cria uma nova movimentação para um veículo em uma vaga, registrando o início do estacionamento.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Check-in realizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos"),
            @ApiResponse(responseCode = "404", description = "Veículo ou vaga não encontrados"),
            @ApiResponse(responseCode = "409", description = "A vaga solicitada já está ocupada")
    })
    @PostMapping()
    public ResponseEntity<MovimentacaoResponse> checkIn(@RequestBody @Valid MovimentacaoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.checkIn(request));
    }

    @Operation(summary = "Registrar a saída de um veículo (Check-out)", description = "Finaliza uma movimentação em aberto, calcula o valor a ser pago e registra a data/hora de saída.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Check-out realizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Nenhuma movimentação em aberto encontrada para a placa informada")
    })
    @PutMapping()
    public ResponseEntity<MovimentacaoResponse> checkOut(@RequestParam String placa) {
        return ResponseEntity.status(HttpStatus.OK).body(service.checkOut(placa));
    }

    @Operation(summary = "Listar todas as movimentações", description = "Retorna uma lista com todas as movimentações já registradas, incluindo as ativas e as finalizadas.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de movimentações retornada com sucesso")
    })
    @GetMapping("/")
    public ResponseEntity<List<MovimentacaoRelatorioResponse>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @Operation(summary = "Relatório de veículos estacionados", description = "Retorna uma lista de todas as movimentações que estão atualmente em aberto (veículos estacionados).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Relatório retornado com sucesso")
    })
    @GetMapping("/relatorio/estacionados")
    public ResponseEntity<List<MovimentacaoRelatorioResponse>> relatorioVeiculosEstacionados() {
        return ResponseEntity.ok(service.getVeiculosEstacionados());
    }

    @Operation(summary = "Relatório de movimentações por placa", description = "Retorna o histórico completo de movimentações para uma placa de veículo específica.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Relatório retornado com sucesso")
    })
    @GetMapping("/relatorio/por-placa")
    public ResponseEntity<List<MovimentacaoRelatorioResponse>> relatorioVeiculoPorPlaca(@RequestParam String placa) {
        return ResponseEntity.ok(service.getRelatorioVeiculoIndividual(placa));
    }

    @Operation(summary = "Relatório de entradas por período", description = "Retorna todas as movimentações cuja data de entrada está dentro do período especificado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Relatório retornado com sucesso")
    })
    @GetMapping("/relatorio/entrada")
    public ResponseEntity<List<MovimentacaoRelatorioResponse>> relatorioEntradaBetween(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
                                                                                       @RequestParam  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fim) {
        return ResponseEntity.ok(service.getRelatorioEntradaBetween(inicio, fim));
    }

    @Operation(summary = "Relatório de saídas por período", description = "Retorna todas as movimentações cuja data de saída está dentro do período especificado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Relatório retornado com sucesso")
    })
    @GetMapping("/relatorio/saida")
    public ResponseEntity<List<MovimentacaoRelatorioResponse>> relatorioSaidaBetween(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
                                                                                     @RequestParam  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fim) {
        return ResponseEntity.ok(service.getRelatorioSaidaBetween(inicio, fim));
    }

    @Operation(summary = "Relatório geral por período", description = "Retorna todas as movimentações que iniciaram OU terminaram dentro do período especificado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Relatório retornado com sucesso")
    })
    @GetMapping("/relatorio/")
    public ResponseEntity<List<MovimentacaoRelatorioResponse>> relatorioGeral(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
                                                                              @RequestParam  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fim) {
        return ResponseEntity.ok(service.getRelatorioGeralBetween(inicio, fim));
    }
}
