package com.br.desafio.estacionamento.movimentacao.controller;

import com.br.desafio.estacionamento.movimentacao.TipoRelatorio;
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
import java.util.ArrayList;
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

    @Operation(
            summary = "Gerar relatórios de movimentação",
            description = """
                    Endpoint para gerar diferentes tipos de relatórios. O comportamento é determinado pelo parâmetro obrigatório `tipo`.
                    <br>
                    <h3>Tipos de Relatório (`tipo`):</h3>
                    <ul>
                        <li><b>VEICULOS_ESTACIONADOS</b>: Retorna os veículos atualmente no estacionamento.</li>
                        <li><b>VEICULO_POR_PLACA</b>: Retorna o histórico de um veículo específico. Requer o parâmetro `placa`.</li>
                        <li><b>ENTRADA_POR_PERIODO</b>: Retorna movimentações com entrada no período especificado. Requer os parâmetros `inicio` e `fim`.</li>
                        <li><b>SAIDA_POR_PERIODO</b>: Retorna movimentações com saída no período especificado. Requer os parâmetros `inicio` e `fim`.</li>
                        <li><b>GERAL_POR_PERIODO</b>: Retorna todas as movimentações que ocorreram dentro do período. Requer os parâmetros `inicio` e `fim`.</li>
                    </ul>
                    """
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Relatório retornado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Combinação de parâmetros inválida para o tipo de relatório solicitado")
    })
    @GetMapping("/relatorio")
    public ResponseEntity<List<MovimentacaoRelatorioResponse>> gerarRelatorio(@RequestParam TipoRelatorio tipo,
                                                                              @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
                                                                              @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fim,
                                                                              @RequestParam(required = false) String placa
    ) {
        List<MovimentacaoRelatorioResponse> result = new ArrayList<>();
        switch (tipo) {
            case VEICULOS_ESTACIONADOS -> {
                result = service.getVeiculosEstacionados();
            }
            case VEICULO_POR_PLACA -> {
                if (placa != null) result = service.getRelatorioVeiculoIndividual(placa);
            }
            case ENTRADA_POR_PERIODO -> {
                if (inicio != null && fim != null)
                    result = service.getRelatorioEntradaBetween(inicio, fim);
            }
            case SAIDA_POR_PERIODO -> {
                if (inicio != null && fim != null)
                    result = (service.getRelatorioSaidaBetween(inicio, fim));
            }
            case GERAL_POR_PERIODO -> {
                if (inicio != null && fim != null)
                    result = (service.getRelatorioGeralBetween(inicio, fim));
            }

        }
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "Listar todas as movimentações", description = "Retorna uma lista completa de todas as movimentações já registradas no sistema.")
    @GetMapping("/")
    public ResponseEntity<List<MovimentacaoRelatorioResponse>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }
}
