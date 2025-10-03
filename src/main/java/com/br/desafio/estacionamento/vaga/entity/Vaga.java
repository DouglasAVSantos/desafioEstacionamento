package com.br.desafio.estacionamento.vaga.entity;

import com.br.desafio.estacionamento.vaga.Estado;
import com.br.desafio.estacionamento.vaga.dto.VagaRequest;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "vagas")
public class Vaga {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String codigo;
    @Column(name = "criado_em")
    private LocalDateTime criadoEm;
    @Enumerated(EnumType.STRING)
    private Estado estado;
    private Boolean ativo;

    private Vaga() {
    }

    public LocalDateTime getCriadoEm() {
        return criadoEm;
    }


    private Vaga(String codigo, LocalDateTime criadoEm, Estado estado, Boolean ativo) {
        this.codigo = codigo;
        this.estado = estado;
        this.ativo = ativo;
        this.criadoEm = criadoEm;
    }

    public static Vaga of(VagaRequest request) {
        return new Vaga(request.codigo(), LocalDateTime.now(), request.estado(), true);
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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Vaga vaga = (Vaga) o;
        return Objects.equals(id, vaga.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
