package com.br.desafio.estacionamento.shared;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<Map<String,Object>>handleConflict(ConflictException ex){
        HashMap<String,Object> map = new HashMap<>();
        map.put("horario", LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss - dd/MM/yyyy")));
        map.put("erro",ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(map);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Map<String,Object>>handleNotFound(NotFoundException ex){
        HashMap<String,Object> map = new HashMap<>();
        map.put("horario", LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss - dd/MM/yyyy")));
        map.put("erro",ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(map);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String,Object>>handleIllegalArgument(IllegalArgumentException ex){
        HashMap<String,Object> map = new HashMap<>();
        map.put("horario", LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss - dd/MM/yyyy")));
        map.put("erro",ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(map);
    }
}
