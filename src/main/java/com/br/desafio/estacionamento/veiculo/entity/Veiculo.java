package com.br.desafio.estacionamento.veiculo.entity;

import com.br.desafio.estacionamento.veiculo.TipoVeiculo;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "veiculos")
public class Veiculo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String modelo;
    @Column(unique = true)
    private String placa;
    @Enumerated(EnumType.STRING)
    private TipoVeiculo tipo;
    @Column(name = "registrado_em")
    private LocalDateTime registradoEm;

    public LocalDateTime getRegistradoEm() {
        return registradoEm;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Veiculo veiculo = (Veiculo) o;
        return Objects.equals(id, veiculo.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    public void setRegistradoEm(LocalDateTime registradoEm) {
        this.registradoEm = registradoEm;
    }

    public Veiculo(String modelo, String placa, TipoVeiculo tipo) {
        this.modelo = modelo;
        this.placa = placa;
        this.tipo = tipo;
    }

    public Veiculo() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public TipoVeiculo getTipo() {
        return tipo;
    }

    public void setTipo(TipoVeiculo tipo) {
        this.tipo = tipo;
    }
}
