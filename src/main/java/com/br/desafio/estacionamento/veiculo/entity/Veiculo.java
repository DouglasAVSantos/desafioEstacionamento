package com.br.desafio.estacionamento.veiculo.entity;

import com.br.desafio.estacionamento.veiculo.dto.VeiculoRequest;
import jakarta.persistence.*;

@Entity
@Table(name = "veiculos")
public class Veiculo {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String modelo;
    @Column(unique = true)
    private String placa;
    @Enumerated(EnumType.STRING)
    private TipoVeiculo tipo;

    private Veiculo(String modelo, String placa, TipoVeiculo tipo) {
        this.modelo = modelo;
        this.placa = placa;
        this.tipo = tipo;
    }

    public Veiculo() {
    }

    public static Veiculo of(VeiculoRequest request){
        return new Veiculo(request.modelo(),request.placa(),request.tipo());
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
