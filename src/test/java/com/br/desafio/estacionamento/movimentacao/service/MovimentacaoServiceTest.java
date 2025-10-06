package com.br.desafio.estacionamento.movimentacao.service;

import com.br.desafio.estacionamento.movimentacao.dto.MovimentacaoRelatorioResponse;
import com.br.desafio.estacionamento.movimentacao.dto.MovimentacaoRequest;
import com.br.desafio.estacionamento.movimentacao.dto.MovimentacaoResponse;
import com.br.desafio.estacionamento.movimentacao.entity.Movimentacao;
import com.br.desafio.estacionamento.movimentacao.repository.MovimentacaoRepository;
import com.br.desafio.estacionamento.shared.ConflictException;
import com.br.desafio.estacionamento.shared.NotFoundException;
import com.br.desafio.estacionamento.vaga.Estado;
import com.br.desafio.estacionamento.vaga.dto.VagaRequest;
import com.br.desafio.estacionamento.vaga.entity.Vaga;
import com.br.desafio.estacionamento.vaga.mapper.VagaMapper;
import com.br.desafio.estacionamento.vaga.service.VagaService;
import com.br.desafio.estacionamento.veiculo.dto.VeiculoRequest;
import com.br.desafio.estacionamento.veiculo.entity.Veiculo;
import com.br.desafio.estacionamento.veiculo.mapper.VeiculoMapper;
import com.br.desafio.estacionamento.veiculo.service.VeiculoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MovimentacaoServiceTest {

    @InjectMocks
    private MovimentacaoService service;

    @Mock
    private VagaService vagaService;
    @Mock
    private VeiculoService veiculoService;
    @Mock
    private MovimentacaoRepository repository;
    private BigDecimal valorHoraInicial = new BigDecimal("5.00");
    private BigDecimal valorDemaisHoras = new BigDecimal("2.00");
    private VeiculoRequest veiculoRequest = new VeiculoRequest("Fazer 150", "123", "moto");
    private Veiculo veiculo = VeiculoMapper.toEntity(veiculoRequest);
    private VagaRequest vagaRequest = new VagaRequest("A1", Estado.LIVRE);
    private Vaga vaga = VagaMapper.toEntity(vagaRequest);
    private MovimentacaoRequest movimentacaoRequest = new MovimentacaoRequest("A1", "123");
    private Movimentacao movimentacao = new Movimentacao(1l, LocalDateTime.now(), LocalDateTime.now().plusMinutes(15), vaga, veiculo, valorHoraInicial);
    private List<MovimentacaoRelatorioResponse> listaRelatorio = List.of(new MovimentacaoRelatorioResponse(
            1L, Timestamp.valueOf(LocalDateTime.now()), Timestamp.valueOf(LocalDateTime.now()), 1L, "A1", "LIVRE", 1L, "Fazer 150", "123", "moto", valorHoraInicial));

    @Test
    void deveLancarConflictExceptionNocheckIn() {
        Mockito.when(repository.findByVeiculoPlacaAndSaidaIsNull(veiculoRequest.placa())).thenReturn(Optional.of(movimentacao));
        when(veiculoService.findVeiculo(movimentacaoRequest.placaVeiculo())).thenReturn(veiculo);

        ConflictException ex = assertThrows(ConflictException.class, () -> service.checkIn(movimentacaoRequest));

        assertEquals("Veiculo ja consta em outra vaga", ex.getMessage());

        verify(repository).findByVeiculoPlacaAndSaidaIsNull(veiculoRequest.placa());
    }

    @Test
    void deveFazerCheckInComSucesso() {
        Mockito.when(repository.findByVeiculoPlacaAndSaidaIsNull(veiculoRequest.placa())).thenReturn(Optional.empty());
        when(veiculoService.findVeiculo(movimentacaoRequest.placaVeiculo())).thenReturn(veiculo);
        when(vagaService.checkIn(movimentacaoRequest.codigoVaga())).thenReturn(vaga);
        when(repository.save(any(Movimentacao.class))).thenReturn(movimentacao);

        MovimentacaoResponse result = service.checkIn(movimentacaoRequest);


        assertEquals(movimentacao.getId(), result.id());
        assertEquals(movimentacao.getVeiculo().getModelo(), result.veiculo().modelo());
        assertEquals(movimentacao.getVeiculo().getPlaca(), result.veiculo().placa());
        assertEquals(movimentacao.getVeiculo().getTipo().name(), result.veiculo().veiculo().name());

        verify(repository).findByVeiculoPlacaAndSaidaIsNull(veiculoRequest.placa());
    }

    @Test
    void checkOutNotFound() {
        when(repository.findByVeiculoPlacaAndSaidaIsNull(movimentacaoRequest.placaVeiculo())).thenReturn(Optional.empty());

        NotFoundException ex = assertThrows(NotFoundException.class, () -> service.checkOut(movimentacaoRequest.placaVeiculo()));

        assertEquals("Nenhuma movimentação em aberto para a placa: " + movimentacaoRequest.placaVeiculo(), ex.getMessage());

        verify(repository).findByVeiculoPlacaAndSaidaIsNull(movimentacaoRequest.placaVeiculo());
    }

    @Test
    void checkOutComSucesso() {
        when(repository.findByVeiculoPlacaAndSaidaIsNull(movimentacaoRequest.placaVeiculo())).thenReturn(Optional.of(movimentacao));
        when(vagaService.checkOut(movimentacaoRequest.codigoVaga())).thenReturn(vaga);
        when(repository.save(any(Movimentacao.class))).thenReturn(movimentacao);

        MovimentacaoResponse result = service.checkOut(movimentacaoRequest.placaVeiculo());

        assertEquals(movimentacao.getId(), result.id());
        assertEquals(movimentacao.getVeiculo().getModelo(), result.veiculo().modelo());
        assertEquals(movimentacao.getVeiculo().getPlaca(), result.veiculo().placa());

        verify(repository).findByVeiculoPlacaAndSaidaIsNull(movimentacaoRequest.placaVeiculo());
        verify(vagaService).checkOut(movimentacaoRequest.codigoVaga());
        verify(repository).save(any(Movimentacao.class));
    }

    @Test
    void getMovimentacao() {
        when(repository.findById(movimentacao.getId())).thenReturn(Optional.of(movimentacao));

        assertThat(service.getMovimentacao(1L)).isEqualTo(movimentacao);

        verify(repository).findById(1L);

    }

    @Test
    void deveLancarNotFoundExceptionAoChamarGetMovimentacao() {
        when(repository.findById(movimentacao.getId())).thenReturn(Optional.empty());

        NotFoundException result = assertThrows(NotFoundException.class,()->service.getMovimentacao(1L));

        assertEquals("Movimentação não encontrada para o id:1",result.getMessage());
        verify(repository).findById(1L);

    }

    @Test
    void calculoValorTotal() {
        LocalDateTime inicio = LocalDateTime.now();
        LocalDateTime fim = LocalDateTime.now().plusMinutes(15);

        BigDecimal result = service.calculoValorTotal(inicio, fim);

        assertEquals(valorHoraInicial, result);

    }

    @Test
    void calculoValorTotalCase2() {
        LocalDateTime inicio = LocalDateTime.now();
        LocalDateTime fim = LocalDateTime.now().plusMinutes(65);

        BigDecimal result = service.calculoValorTotal(inicio, fim);

        assertEquals(valorHoraInicial.add(valorDemaisHoras), result);
    }

    @Test
    void getVeiculosEstacionados() {
        when(repository.findAllBySaidaIsNull()).thenReturn(listaRelatorio);

        assertThat(service.getVeiculosEstacionados()).isEqualTo(listaRelatorio);

        verify(repository).findAllBySaidaIsNull();
    }

    @Test
    void getAll() {
        when(repository.getAll()).thenReturn(listaRelatorio);

        assertThat(service.getAll()).isEqualTo(listaRelatorio);

        verify(repository).getAll();
    }

    @Test
    void getRelatorioVeiculoIndividual() {
        String placa = "ABC3214";
        when(repository.findAllByVeiculoPlaca(placa)).thenReturn(listaRelatorio);

        assertThat(service.getRelatorioVeiculoIndividual(placa)).isEqualTo(listaRelatorio);

        verify(repository).findAllByVeiculoPlaca(placa);
    }

    @Test
    void getRelatorioEntradaBetween() {
        LocalDateTime inicio = LocalDateTime.now();
        LocalDateTime fim = LocalDateTime.now().plusMinutes(15);
        when(repository.findAllByEntradaBetween(inicio, fim)).thenReturn(listaRelatorio);

        assertThat(service.getRelatorioEntradaBetween(inicio, fim)).isEqualTo(listaRelatorio);

        verify(repository).findAllByEntradaBetween(inicio, fim);
    }

    @Test
    void getRelatorioSaidaBetween() {
        LocalDateTime inicio = LocalDateTime.now();
        LocalDateTime fim = LocalDateTime.now().plusMinutes(15);
        when(repository.findAllBySaidaBetween(inicio, fim)).thenReturn(listaRelatorio);

        assertThat(service.getRelatorioSaidaBetween(inicio, fim)).isEqualTo(listaRelatorio);

        verify(repository).findAllBySaidaBetween(inicio, fim);
    }

    @Test
    void getRelatorioGeralBetween() {
        LocalDateTime inicio = LocalDateTime.now();
        LocalDateTime fim = LocalDateTime.now().plusMinutes(15);
        when(repository.findAllBetween(inicio, fim)).thenReturn(listaRelatorio);

        assertThat(service.getRelatorioGeralBetween(inicio, fim)).isEqualTo(listaRelatorio);

        verify(repository).findAllBetween(inicio, fim);
    }
}