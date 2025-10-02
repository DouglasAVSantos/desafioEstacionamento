package com.br.desafio.estacionamento.vaga.controller;

import com.br.desafio.estacionamento.vaga.dto.VagaRequest;
import com.br.desafio.estacionamento.vaga.dto.VagaResponse;
import com.br.desafio.estacionamento.vaga.entity.Estado;
import com.br.desafio.estacionamento.vaga.service.VagaService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/vaga")
public class VagaController {

    private final VagaService service;

    public VagaController(VagaService vagaService){
        this.service = vagaService;
    }

    @PostMapping
    public ResponseEntity<VagaResponse> cadastrar(@RequestBody @Valid VagaRequest request){
        VagaResponse response = service.cadastrar(request);
        return ResponseEntity.created(URI.create("api/v1/vaga/"+response.id())).body(response);
    }

    @GetMapping("/")
public ResponseEntity<List<VagaResponse>> findAll(){
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/estado")
    public ResponseEntity<List<VagaResponse>> findAllByEstado(@RequestParam Estado estado){
        return ResponseEntity.ok(service.findAllByEstado(estado));
    }

    @GetMapping("/{id}")
    public ResponseEntity<VagaResponse> findById(@PathVariable Long id){
        return ResponseEntity.ok(service.findVaga(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> inativaById(@PathVariable Long id){
        service.inativaById(id);
        return ResponseEntity.noContent().build();
    }

}
