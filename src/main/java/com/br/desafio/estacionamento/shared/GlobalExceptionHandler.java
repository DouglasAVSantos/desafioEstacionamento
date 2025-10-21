package com.br.desafio.estacionamento.shared;

import com.br.desafio.estacionamento.shared.dto.ErroCampo;
import com.br.desafio.estacionamento.shared.dto.ErroResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ErroResponse> handle(ConflictException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ErroResponse.mensagemDefault(ex.getMessage()));
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErroResponse> handle(NotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErroResponse.mensagemDefault(ex.getMessage()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErroResponse> handle(IllegalArgumentException ex) {

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErroResponse.mensagemCampo("tipo",ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErroResponse> handle(MethodArgumentTypeMismatchException ex) {
        if (ex.getRequiredType().isEnum()) {
            return ResponseEntity.badRequest().body(
                    ErroResponse.mensagemCampo(ex.getName(),
                                            "Valores aceitos: " + Arrays.toString(ex.getRequiredType().getEnumConstants())));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErroResponse.mensagemCampo(ex.getName(), ex.getMessage()));
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErroResponse> handle(MethodArgumentNotValidException ex) {
        List<FieldError> fieldErrors = ex.getFieldErrors();
        List<ErroCampo> erros = fieldErrors.stream().map(f-> new ErroCampo(f.getField(), f.getDefaultMessage())).toList();
        return ResponseEntity.badRequest().body(new ErroResponse(HttpStatus.BAD_REQUEST.value(), "Dados de entrada inválidos", erros));
    }

}
