package com.br.desafio.estacionamento.movimentacao.service;

import com.br.desafio.estacionamento.movimentacao.dto.MovimentacaoRequest;
import com.br.desafio.estacionamento.movimentacao.dto.MovimentacaoResponse;
import com.br.desafio.estacionamento.movimentacao.entity.Movimentacao;
import com.br.desafio.estacionamento.movimentacao.mapper.MovimentacaoMapper;
import com.br.desafio.estacionamento.movimentacao.repository.MovimentacaoRepository;
import com.br.desafio.estacionamento.shared.ConflictException;
import com.br.desafio.estacionamento.shared.NotFoundException;
import com.br.desafio.estacionamento.vaga.entity.Vaga;
import com.br.desafio.estacionamento.vaga.service.VagaService;
import com.br.desafio.estacionamento.veiculo.entity.Veiculo;
import com.br.desafio.estacionamento.veiculo.service.VeiculoService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class MovimentacaoService {

    private final MovimentacaoRepository repository;
    private VagaService vagaService;
    private VeiculoService veiculoService;
    private BigDecimal valorHoraInicial = new BigDecimal("5.00");
    private BigDecimal valorDemaisHoras = new BigDecimal("2.00");
    private MovimentacaoMapper mapper = new MovimentacaoMapper();

    public MovimentacaoService(MovimentacaoRepository movimentacaoRepository, VagaService vagaService, VeiculoService veiculoService) {
        this.repository = movimentacaoRepository;
        this.vagaService = vagaService;
        this.veiculoService = veiculoService;
    }

    @Transactional
    public MovimentacaoResponse checkIn(MovimentacaoRequest request) {
        Veiculo veiculo = veiculoService.findVeiculo(request.placaVeiculo());
        Vaga vaga = vagaService.checkIn(request.codigoVaga());
        if (repository.findByVeiculoPlacaAndSaidaIsNull(veiculo.getPlaca()).isPresent()) {
            throw new ConflictException("Veiculo ja consta em outra vaga");
        }
        Movimentacao movimentacao = new Movimentacao();
        movimentacao.setEntrada(LocalDateTime.now());
        movimentacao.setVaga(vaga);
        movimentacao.setVeiculo(veiculo);

        return mapper.toResponse(repository.save(movimentacao));
    }

    @Transactional
    public MovimentacaoResponse checkOut(String placa) {
        Movimentacao movimentacao = repository.findByVeiculoPlacaAndSaidaIsNull(placa.trim().toUpperCase())
                .orElseThrow(() -> new NotFoundException("Nenhuma movimentação em aberto para a placa: " + placa));
        LocalDateTime horarioAtual = LocalDateTime.now();
        movimentacao.setSaida(horarioAtual);
        movimentacao.setValor(calculoValorTotal(movimentacao.getEntrada(), horarioAtual));
        vagaService.checkOut(movimentacao.getVaga().getCodigo());
        return mapper.toResponse(repository.save(movimentacao));
    }

    public Movimentacao getMovimentacao(Long id) {
        return repository.findById(id).orElseThrow(() -> new NotFoundException("Movimentação não encontrada para o id:" + id));
    }

    public BigDecimal calculoValorTotal(LocalDateTime inicio, LocalDateTime fim) {
        Duration duracao = Duration.between(inicio, fim);
        BigDecimal horasACobrar = BigDecimal.valueOf(duracao.toMinutes()).divide(BigDecimal.valueOf(60), RoundingMode.UP);
        return (
                horasACobrar.compareTo(BigDecimal.valueOf(2)) < 0 ? valorHoraInicial
                        :
                        horasACobrar.subtract(BigDecimal.ONE)
                                .multiply(valorDemaisHoras)
                                .add(valorHoraInicial)
        );
    }

    public List<MovimentacaoResponse> getVeiculosEstacionados() {
        return repository.findAllBySaidaIsNull();
    }

    public List<MovimentacaoResponse> getAll() {
        return repository.getAll();
    }

    public List<MovimentacaoResponse> getRelatorioVeiculoIndividual(String placa) {
        return repository.findAllByVeiculoPlaca(placa.trim().toUpperCase());
    }

    public List<MovimentacaoResponse> getRelatorioEntradaBetween(LocalDateTime inicio, LocalDateTime fim) {
        return repository.findAllByEntradaBetween(inicio, fim);
    }

    public List<MovimentacaoResponse> getRelatorioSaidaBetween(LocalDateTime inicio, LocalDateTime fim) {
        return repository.findAllBySaidaBetween(inicio, fim);
    }

    public List<MovimentacaoResponse> getRelatorioGeralBetween(LocalDateTime inicio, LocalDateTime fim) {
        return repository.findAllBetween(inicio, fim);
    }

}
