package com.br.desafio.estacionamento.veiculo.controller;

import com.br.desafio.estacionamento.veiculo.TipoVeiculo;
import com.br.desafio.estacionamento.veiculo.dto.VeiculoRequest;
import com.br.desafio.estacionamento.veiculo.dto.VeiculoResponse;
import com.br.desafio.estacionamento.veiculo.service.VeiculoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static javax.management.Query.value;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(VeiculoController.class)
class VeiculoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private VeiculoService service;

    private VeiculoRequest request;
    private VeiculoResponse response;

    @BeforeEach
    void setUp() {
        request = new VeiculoRequest("FAZER", "ABC1234", "MOTO");
        response = new VeiculoResponse(1L, "FAZER", "ABC1234", TipoVeiculo.MOTO, LocalDateTime.now());
    }

    @Test
    void deveCadastrarVeiculoComSucesso() throws Exception {
        when(service.cadastrar(any(VeiculoRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/v1/veiculo")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "api/v1/veiculo/" + response.id()))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.placa", is("ABC1234")));

        verify(service).cadastrar(any(VeiculoRequest.class));
    }

    @Test
    void deveListarTodosOsVeiculos() throws Exception {
        when(service.findAll()).thenReturn(List.of(response));

        mockMvc.perform(get("/api/v1/veiculo/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].placa", is("ABC1234")));

        verify(service).findAll();
    }

    @Test
    void deveBuscarVeiculoPorId() throws Exception {
        when(service.findVeiculo(1L)).thenReturn(response);

        mockMvc.perform(get("/api/v1/veiculo/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.placa", is("ABC1234")));

        verify(service).findVeiculo(1L);
    }

    @Test
    void deveAtualizarVeiculoComSucesso() throws Exception {
        VeiculoRequest updateRequest = new VeiculoRequest("BIZ", "XYZ9876", "MOTO");
        VeiculoResponse updatedResponse = new VeiculoResponse(1L, "BIZ", "XYZ9876", TipoVeiculo.MOTO, LocalDateTime.now());

        when(service.atualizarCadastro(eq(1L), any(VeiculoRequest.class))).thenReturn(updatedResponse);

        mockMvc.perform(put("/api/v1/veiculo/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.modelo", is("BIZ")))
                .andExpect(jsonPath("$.placa", is("XYZ9876")));

        verify(service).atualizarCadastro(eq(1L), any(VeiculoRequest.class));
    }

    @Test
    void deveLancarMethodArgumentTypeMismatchExceptionAoPassarPathInvalidoNaRota() throws Exception {
        mockMvc.perform(get("/api/v1/veiculo/abc"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.erro")
                        .value("Method parameter 'id': Failed to convert value of type 'java.lang.String' to required type 'java.lang.Long'; For input string: \"abc\""));

        verify(service,never()).findVeiculo("abc");
    }
}