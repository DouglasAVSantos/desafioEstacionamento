package com.br.desafio.estacionamento.vaga.Service;

import com.br.desafio.estacionamento.shared.ConflictException;
import com.br.desafio.estacionamento.shared.NotFoundException;
import com.br.desafio.estacionamento.vaga.Dto.VagaRequest;
import com.br.desafio.estacionamento.vaga.Entity.Estado;
import com.br.desafio.estacionamento.vaga.Entity.Vaga;
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
    public Vaga cadastrar(VagaRequest request) {
        return repository.save(new Vaga(request));
    }

    public Vaga getVaga(Long id) {
        return repository.findById(id).orElseThrow(() -> new NotFoundException("registro não encotrado"));
    }

    public List<Vaga> getAll() {
        return repository.findAll();
    }

    @Transactional
    public void delete(Long id) {
        Vaga vaga = getVaga(id);
        vaga.setAtivo(false);
        repository.save(vaga);
    }

    @Transactional
    public Vaga checkOut(Long id) {
        Vaga vaga = getVaga(id);
        if (vaga.getEstado().equals(Estado.LIVRE)) {
            throw new ConflictException("Vaga não está ocupada");
        }
        vaga.setEstado(Estado.LIVRE);
        return repository.save(vaga);
    }

    @Transactional
    public Vaga checkIn(Long id) {
        Vaga vaga = getVaga(id);
        if (vaga.getEstado().equals(Estado.OCUPADA)) {
            throw new ConflictException("Vaga já ocupada");
        }
        vaga.setEstado(Estado.OCUPADA);
        return repository.save(vaga);
    }

}
