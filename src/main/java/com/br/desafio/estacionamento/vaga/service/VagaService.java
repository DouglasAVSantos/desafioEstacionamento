package com.br.desafio.estacionamento.vaga.service;

import com.br.desafio.estacionamento.shared.ConflictException;
import com.br.desafio.estacionamento.shared.NotFoundException;
import com.br.desafio.estacionamento.vaga.dto.VagaRequest;
import com.br.desafio.estacionamento.vaga.dto.VagaResponse;
import com.br.desafio.estacionamento.vaga.entity.Estado;
import com.br.desafio.estacionamento.vaga.entity.Vaga;
import com.br.desafio.estacionamento.vaga.repository.VagaRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VagaService {

    private final VagaRepository repository;

    public VagaService(VagaRepository vagaRepository) {
        this.repository = vagaRepository;
    }

    @Transactional
    public VagaResponse cadastrar(VagaRequest request) {
        if (repository.findByCodigoAndAtivoTrue(request.codigo()).isPresent()) {
            throw new ConflictException("registro já cadastrado");
        }
        return VagaResponse.of(repository.save(Vaga.of(request)));
    }

    public VagaResponse findVaga(Long id) {
        return VagaResponse.of(repository.findByIdAndAtivoTrue(id).orElseThrow(() -> new NotFoundException("registro não encotrado")));
    }

    public List<VagaResponse> findAll() {
        return repository.findAllByAtivoTrue().stream().map(VagaResponse::of).toList();
    }

    @Transactional
    public void inativaById(Long id) {
        Vaga vaga = getVaga(id);
        vaga.setAtivo(false);
        repository.save(vaga);
    }

    @Transactional
    public VagaResponse checkOut(Long id) {
        Vaga vaga = getVaga(id);
        if (vaga.getEstado().equals(Estado.LIVRE)) {
            throw new ConflictException("Vaga não está ocupada");
        }
        vaga.setEstado(Estado.LIVRE);
        return VagaResponse.of(repository.save(vaga));
    }

    @Transactional
    public VagaResponse checkIn(Long id) {
        Vaga vaga = getVaga(id);
        if (vaga.getEstado().equals(Estado.OCUPADA)) {
            throw new ConflictException("Vaga já ocupada");
        }
        vaga.setEstado(Estado.OCUPADA);
        return VagaResponse.of(repository.save(vaga));
    }

    private Vaga getVaga(Long id) {
        return repository.findByIdAndAtivoTrue(id).orElseThrow(() -> new NotFoundException("registro não encontrado para o id: " + id));
    }

}
