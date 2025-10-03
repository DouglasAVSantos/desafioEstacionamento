package com.br.desafio.estacionamento.vaga.controller;

import com.br.desafio.estacionamento.vaga.dto.VagaRequest;
import com.br.desafio.estacionamento.vaga.dto.VagaResponse;
import com.br.desafio.estacionamento.vaga.entity.Estado;
import com.br.desafio.estacionamento.vaga.service.VagaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/vaga")
@Tag(name = "Vagas", description = "Endpoints para o gerenciamento de vagas de estacionamento")
public class VagaController {

    private final VagaService service;

    public VagaController(VagaService vagaService) {
        this.service = vagaService;
    }

    @Operation(summary = "Cadastrar uma nova vaga", description = "Registra uma nova vaga no sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Vaga cadastrada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos"),
            @ApiResponse(responseCode = "409", description = "Vaga com este código já cadastrada")
    })
    @PostMapping
    public ResponseEntity<VagaResponse> cadastrar(@RequestBody @Valid VagaRequest request) {
        VagaResponse response = service.cadastrar(request);
        return ResponseEntity.created(URI.create("api/v1/vaga/" + response.id())).body(response);
    }

    @Operation(summary = "Listar todas as vagas", description = "Retorna uma lista com todas as vagas cadastradas no sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de vagas retornada com sucesso")
    })
    @GetMapping("/")
    public ResponseEntity<List<VagaResponse>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @Operation(summary = "Listar vagas por estado", description = "Filtra e retorna as vagas com base no estado (LIVRE ou OCUPADA).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de vagas filtrada retornada com sucesso")
    })
    @GetMapping("/estado")
    public ResponseEntity<List<VagaResponse>> findAllByEstado(@RequestParam Estado estado) {
        return ResponseEntity.ok(service.findAllByEstado(estado));
    }

    @Operation(summary = "Buscar vaga por ID", description = "Retorna os detalhes de uma vaga específica a partir do seu ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Vaga encontrada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Vaga não encontrada para o ID informado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<VagaResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findVaga(id));
    }

    @Operation(summary = "Inativar uma vaga", description = "Realiza a inativação lógica de uma vaga, impedindo que seja usada para novas movimentações.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Vaga inativada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Vaga não encontrada para o ID informado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> inativaById(@PathVariable Long id) {
        service.inativaById(id);
        return ResponseEntity.noContent().build();
    }

}
