package com.br.desafio.estacionamento.movimentacao.controller;


import com.br.desafio.estacionamento.movimentacao.dto.MovimentacaoRelatorioResponse;
import com.br.desafio.estacionamento.movimentacao.dto.MovimentacaoRequest;
import com.br.desafio.estacionamento.movimentacao.dto.MovimentacaoResponse;
import com.br.desafio.estacionamento.movimentacao.entity.Movimentacao;
import com.br.desafio.estacionamento.movimentacao.mapper.MovimentacaoMapper;
import com.br.desafio.estacionamento.movimentacao.service.MovimentacaoService;
import com.br.desafio.estacionamento.vaga.Estado;
import com.br.desafio.estacionamento.vaga.dto.VagaRequest;
import com.br.desafio.estacionamento.vaga.entity.Vaga;
import com.br.desafio.estacionamento.veiculo.dto.VeiculoRequest;
import com.br.desafio.estacionamento.veiculo.mapper.VeiculoMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MovimentacaoController.class)
public class MovimentacaoControllerTest {


    private MovimentacaoController controller;

    @MockBean
    private MovimentacaoService service;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    private final MovimentacaoRequest request = new MovimentacaoRequest("A1", "ABC1234");
    private final Movimentacao movimentacao = new Movimentacao(1L, LocalDateTime.now(), LocalDateTime.now(), Vaga.of(new VagaRequest("A1", Estado.LIVRE)), VeiculoMapper.toEntity(new VeiculoRequest("FAZER", "ABC1234", "MOTO")), BigDecimal.TEN);
    private final MovimentacaoResponse response = new MovimentacaoMapper().toResponse(movimentacao);
    private final MovimentacaoRelatorioResponse relatorio = new MovimentacaoRelatorioResponse(
            1L,
            Timestamp.valueOf(LocalDateTime.now()),
            Timestamp.valueOf(LocalDateTime.now().plusMinutes(15)),
            1L,
            "A1",
            "LIVRE",
            1L,
            "MOTO",
            "ABC1234",
            "MOTO",
            BigDecimal.TEN);
    private final List<MovimentacaoRelatorioResponse> listaRelatorio = List.of(relatorio);


    @Test
    void checkInComSucesso() throws Exception {

        when(service.checkIn(any(MovimentacaoRequest.class))).thenReturn(response);
        mockMvc.perform(post("/api/v1/movimentacao")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L));

        verify(service).checkIn(any(MovimentacaoRequest.class));
    }

    @Test
    void checkOut() throws Exception {
        when(service.checkOut(request.placaVeiculo())).thenReturn(response);

        mockMvc.perform(put("/api/v1/movimentacao")
                        .param("placa", request.placaVeiculo())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.veiculo.placa").value("ABC1234"));

        verify(service).checkOut(request.placaVeiculo());
    }

    @Test
    void getAll() throws Exception {
        when(service.getAll()).thenReturn(listaRelatorio);

        mockMvc.perform(get("/api/v1/movimentacao/")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(1L))
                .andExpect(jsonPath("$.[0].modelo").value(relatorio.modelo()));

        verify(service).getAll();
    }

    @Test
    void relatorioVeiculosEstacionados() throws Exception {
        when(service.getVeiculosEstacionados()).thenReturn(listaRelatorio);

        mockMvc.perform(get("/api/v1/movimentacao/relatorio/estacionados")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(1L))
                .andExpect(jsonPath("$.[0].modelo").value(relatorio.modelo()));

        verify(service).getVeiculosEstacionados();
    }


    @Test
    void relatorioVeiculoPorPlaca() throws Exception {
        when(service.getRelatorioVeiculoIndividual(request.placaVeiculo())).thenReturn(listaRelatorio);

        mockMvc.perform(get("/api/v1/movimentacao/relatorio/por-placa")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("placa", request.placaVeiculo()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(1L))
                .andExpect(jsonPath("$.[0].modelo").value(relatorio.modelo()));

        verify(service).getRelatorioVeiculoIndividual(request.placaVeiculo());

    }

    @Test
    void relatorioEntradaBetween() throws Exception {

        when(service.getRelatorioEntradaBetween(response.entrada(), response.saida())).thenReturn(listaRelatorio);

        mockMvc.perform(get("/api/v1/movimentacao/relatorio/entrada")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("inicio", String.valueOf(response.entrada()))
                        .param("fim", String.valueOf(response.entrada())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(1L))
                .andExpect(jsonPath("$.[0].modelo").value(relatorio.modelo()));

        verify(service).getRelatorioEntradaBetween(response.entrada(), response.saida());

    }

    @Test
    void relatorioSaidaBetween() throws Exception {

        when(service.getRelatorioSaidaBetween(response.entrada(), response.saida())).thenReturn(listaRelatorio);

        mockMvc.perform(get("/api/v1/movimentacao/relatorio/saida")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("inicio", String.valueOf(response.entrada()))
                        .param("fim", String.valueOf(response.entrada())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(1L))
                .andExpect(jsonPath("$.[0].modelo").value(relatorio.modelo()));

        verify(service).getRelatorioSaidaBetween(response.entrada(), response.saida());

    }

    @Test
    void relatorioGeral() throws Exception {

        when(service.getRelatorioGeralBetween(response.entrada(), response.saida())).thenReturn(listaRelatorio);

        mockMvc.perform(get("/api/v1/movimentacao/relatorio/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("inicio", String.valueOf(response.entrada()))
                        .param("fim", String.valueOf(response.entrada())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(1L))
                .andExpect(jsonPath("$.[0].modelo").value(relatorio.modelo()));

        verify(service).getRelatorioGeralBetween(response.entrada(), response.saida());

    }

}
