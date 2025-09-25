package com.br.desafio.estacionamento.vaga.Entity;

import com.br.desafio.estacionamento.vaga.Dto.VagaRequest;
import jakarta.persistence.*;

@Entity
@Table(name = "vagas")
public class Vaga {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String codigo;
    @Enumerated(EnumType.STRING)
    private Estado estado;
    private Boolean ativo;

    public Vaga(VagaRequest request) {
        codigo = request.codigo();
        estado = request.estado();
        ativo = true;
    }

    public Long getId() {
        return id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Estado getEstado() {
        return estado;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }
}
