package com.br.desafio.estacionamento.vaga.repository;

import com.br.desafio.estacionamento.vaga.entity.Estado;
import com.br.desafio.estacionamento.vaga.entity.Vaga;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VagaRepository extends JpaRepository<Vaga,Long> {

    Optional<Vaga>findByIdAndAtivoTrue(Long id);
    Optional<Vaga>findByCodigoAndAtivoTrue(String codigo);
    List<Vaga> findAllByAtivoTrue();
    List<Vaga> findAllByAtivoTrueAndEstado(Estado estado);
}
