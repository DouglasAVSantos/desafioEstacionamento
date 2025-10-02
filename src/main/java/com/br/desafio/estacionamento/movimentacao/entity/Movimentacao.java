package com.br.desafio.estacionamento.movimentacao.entity;

import com.br.desafio.estacionamento.vaga.entity.Vaga;
import com.br.desafio.estacionamento.veiculo.entity.Veiculo;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "movimentacao")
public class Movimentacao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime entrada;
    private LocalDateTime saida;
    @ManyToOne
    @JoinColumn(name = "vaga_id")
    private Vaga vaga;
    @ManyToOne
    @JoinColumn(name = "veiculo_id")
    private Veiculo veiculo;
    private BigDecimal valor;

    public Movimentacao(Long id, LocalDateTime entrada, LocalDateTime saida, Vaga vaga, Veiculo veiculo, BigDecimal valor) {
        this.id = id;
        this.entrada = entrada;
        this.saida = saida;
        this.vaga = vaga;
        this.veiculo = veiculo;
        this.valor = valor;
    }

    public Movimentacao() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getEntrada() {
        return entrada;
    }

    public void setEntrada(LocalDateTime entrada) {
        this.entrada = entrada;
    }

    public LocalDateTime getSaida() {
        return saida;
    }

    public void setSaida(LocalDateTime saida) {
        this.saida = saida;
    }

    public Vaga getVaga() {
        return vaga;
    }

    public void setVaga(Vaga vaga) {
        this.vaga = vaga;
    }

    public Veiculo getVeiculo() {
        return veiculo;
    }

    public void setVeiculo(Veiculo veiculo) {
        this.veiculo = veiculo;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }


}
