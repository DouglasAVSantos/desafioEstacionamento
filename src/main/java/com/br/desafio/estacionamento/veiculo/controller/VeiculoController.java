package com.br.desafio.estacionamento.veiculo.controller;

import com.br.desafio.estacionamento.veiculo.dto.VeiculoRequest;
import com.br.desafio.estacionamento.veiculo.dto.VeiculoResponse;
import com.br.desafio.estacionamento.veiculo.service.VeiculoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/veiculo")
public class VeiculoController {

    private final VeiculoService service;

    public VeiculoController(VeiculoService veiculoService) {
        this.service = veiculoService;
    }

    @PostMapping
    public ResponseEntity<VeiculoResponse> cadastrar(@RequestBody @Valid VeiculoRequest request) {
        VeiculoResponse response = service.cadastrar(request);
        return ResponseEntity.created(URI.create("api/v1/veiculo/" + response.id())).body(response);
    }

    @GetMapping("/")
    public ResponseEntity<List<VeiculoResponse>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<VeiculoResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findVeiculo(id));
    }


    @PutMapping("/{id}")
    public ResponseEntity<VeiculoResponse> atualizar(@PathVariable Long id, @Valid @RequestBody VeiculoRequest request) {
        return ResponseEntity.ok(service.atualizarCadastro(id, request));
    }

}
