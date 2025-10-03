package com.br.desafio.estacionamento.movimentacao.controller;

import com.br.desafio.estacionamento.movimentacao.dto.MovimentacaoRequest;
import com.br.desafio.estacionamento.movimentacao.dto.MovimentacaoResponse;
import com.br.desafio.estacionamento.movimentacao.service.MovimentacaoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/movimentacao")
public class MovimentacaoController {

    private final MovimentacaoService service;

    public MovimentacaoController(MovimentacaoService service){
        this.service = service;
    }

    @PostMapping()
    public ResponseEntity<MovimentacaoResponse> checkIn(@RequestBody @Valid MovimentacaoRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(service.checkIn(request));
    }

    @PutMapping()
    public ResponseEntity<MovimentacaoResponse> checkOut(@RequestParam String placa){
        return ResponseEntity.status(HttpStatus.OK).body(service.checkOut(placa));
    }
}
