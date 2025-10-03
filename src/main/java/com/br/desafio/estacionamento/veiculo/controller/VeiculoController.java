package com.br.desafio.estacionamento.veiculo.controller;

import com.br.desafio.estacionamento.veiculo.dto.VeiculoRequest;
import com.br.desafio.estacionamento.veiculo.dto.VeiculoResponse;
import com.br.desafio.estacionamento.veiculo.service.VeiculoService;
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
@RequestMapping("/api/v1/veiculo")
@Tag(name = "Veículos", description = "Endpoints para o gerenciamento de veículos")
public class VeiculoController {

    private final VeiculoService service;

    public VeiculoController(VeiculoService veiculoService) {
        this.service = veiculoService;
    }

    @Operation(summary = "Cadastrar um novo veículo", description = "Registra um novo veículo no sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Veículo cadastrado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos"),
            @ApiResponse(responseCode = "409", description = "Veículo com esta placa já cadastrado")
    })
    @PostMapping
    public ResponseEntity<VeiculoResponse> cadastrar(@RequestBody @Valid VeiculoRequest request) {
        VeiculoResponse response = service.cadastrar(request);
        return ResponseEntity.created(URI.create("api/v1/veiculo/" + response.id())).body(response);
    }

    @Operation(summary = "Listar todos os veículos", description = "Retorna uma lista com todos os veículos cadastrados no sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de veículos retornada com sucesso")
    })
    @GetMapping("/")
    public ResponseEntity<List<VeiculoResponse>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @Operation(summary = "Buscar veículo por ID", description = "Retorna os detalhes de um veículo específico a partir do seu ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Veículo encontrado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Veículo não encontrado para o ID informado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<VeiculoResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findVeiculo(id));
    }


    @Operation(summary = "Atualizar dados de um veículo", description = "Atualiza as informações de um veículo já existente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Veículo atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos"),
            @ApiResponse(responseCode = "404", description = "Veículo não encontrado para o ID informado"),
            @ApiResponse(responseCode = "409", description = "A nova placa informada já pertence a outro veículo")
    })
    @PutMapping("/{id}")
    public ResponseEntity<VeiculoResponse> atualizar(@PathVariable Long id, @Valid @RequestBody VeiculoRequest request) {
        return ResponseEntity.ok(service.atualizarCadastro(id, request));
    }

}
