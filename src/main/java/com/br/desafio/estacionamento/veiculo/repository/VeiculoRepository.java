package com.br.desafio.estacionamento.veiculo.repository;

import com.br.desafio.estacionamento.veiculo.entity.Veiculo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VeiculoRepository extends JpaRepository<Veiculo, Long> {

    Optional<Veiculo> findByPlaca(String placa);
}
