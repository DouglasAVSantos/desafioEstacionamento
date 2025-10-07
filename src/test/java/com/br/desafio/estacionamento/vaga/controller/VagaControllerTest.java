package com.br.desafio.estacionamento.vaga.controller;

import com.br.desafio.estacionamento.vaga.Estado;
import com.br.desafio.estacionamento.vaga.dto.VagaRequest;
import com.br.desafio.estacionamento.vaga.dto.VagaResponse;
import com.br.desafio.estacionamento.vaga.service.VagaService;
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

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(VagaController.class)
class VagaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private VagaService service;

    private VagaRequest request;
    private VagaResponse response;

    @BeforeEach
    void setUp() {
        request = new VagaRequest("A1", Estado.LIVRE);
        response = new VagaResponse(1L, "A1", Estado.LIVRE, LocalDateTime.now());
    }

    @Test
    void deveCadastrarVagaComSucesso() throws Exception {
        when(service.cadastrar(any(VagaRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/v1/vaga")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "api/v1/vaga/" + response.id()))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.codigo", is("A1")));

        verify(service).cadastrar(any(VagaRequest.class));
    }

    @Test
    void deveListarTodasAsVagas() throws Exception {
        when(service.findAll()).thenReturn(List.of(response));

        mockMvc.perform(get("/api/v1/vaga/")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id", is(1)))
                .andExpect(jsonPath("$.[0].codigo", is("A1")));

        verify(service).findAll();
    }

    @Test
    void deveListarVagasPorEstado() throws Exception {
        when(service.findAllByEstado(Estado.LIVRE)).thenReturn(List.of(response));

        mockMvc.perform(get("/api/v1/vaga/").param("estado", "LIVRE")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id", is(1)))
                .andExpect(jsonPath("$.[0].estado", is("LIVRE")));

        verify(service).findAllByEstado(Estado.LIVRE);
    }

    @Test
    void deveBuscarVagaPorId() throws Exception {
        when(service.findVaga(1L)).thenReturn(response);

        mockMvc.perform(get("/api/v1/vaga/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)));

        verify(service).findVaga(1L);
    }

    @Test
    void deveInativarVagaComSucesso() throws Exception {
        mockMvc.perform(delete("/api/v1/vaga/1"))
                .andExpect(status().isNoContent());

        verify(service).inativaById(1L);
    }
}