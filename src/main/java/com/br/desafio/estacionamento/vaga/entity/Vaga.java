package com.br.desafio.estacionamento.vaga.entity;

import com.br.desafio.estacionamento.vaga.dto.VagaRequest;
import com.br.desafio.estacionamento.veiculo.entity.Veiculo;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "vagas")
public class Vaga {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String codigo;
    private LocalDateTime criadoEm;
    @Enumerated(EnumType.STRING)
    private Estado estado;
    private Boolean ativo;
    @ManyToOne
    @JoinColumn(name = "veiculo_id")
    private Veiculo veiculo;

    private Vaga() {
    }

    public LocalDateTime getCriadoEm() {
        return criadoEm;
    }

    private  Vaga(String codigo, LocalDateTime criadoEm, Estado estado, Boolean ativo) {
        this.codigo = codigo;
        this.estado = estado;
        this.ativo = ativo;
        this.criadoEm = criadoEm;
    }

    public static Vaga of(VagaRequest request){
        return new Vaga(request.codigo(),LocalDateTime.now(),request.estado(),true);
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
