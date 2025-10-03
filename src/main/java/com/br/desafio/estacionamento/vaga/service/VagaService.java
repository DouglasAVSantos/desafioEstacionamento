package com.br.desafio.estacionamento.vaga.service;

import com.br.desafio.estacionamento.shared.ConflictException;
import com.br.desafio.estacionamento.shared.NotFoundException;
import com.br.desafio.estacionamento.vaga.mapper.VagaMapper;
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
        return VagaMapper.toResponse(repository.save(VagaMapper.toEntity(request)));
    }

    public VagaResponse findVaga(Long id) {
        return VagaMapper.toResponse(repository.findByIdAndAtivoTrue(id).orElseThrow(() -> new NotFoundException("registro não encotrado")));
    }

    public Vaga findVaga(String codigo) {
        return repository.findByCodigoAndAtivoTrue(codigo).orElseThrow(() -> new NotFoundException("registro não encotrado"));
    }

    public List<VagaResponse> findAll() {
        return repository.findAllByAtivoTrue().stream().map(VagaMapper::toResponse).toList();
    }

    public List<VagaResponse> findAllByEstado(Estado estado) {
        return repository.findAllByAtivoTrueAndEstado(estado).stream().map(VagaMapper::toResponse).toList();
    }

    @Transactional
    public void inativaById(Long id) {
        Vaga vaga = getVaga(id);
        vaga.setAtivo(false);
        repository.save(vaga);
    }

    @Transactional
    public Vaga checkOut(String codigoDaVaga) {
        Vaga vaga = findVaga(codigoDaVaga);
        if (vaga.getEstado().equals(Estado.LIVRE)) {
            throw new ConflictException("Vaga não está ocupada");
        }
        vaga.setEstado(Estado.LIVRE);
        return repository.save(vaga);
    }

    @Transactional
    public Vaga checkIn(String codigoDaVaga) {
        Vaga vaga = findVaga(codigoDaVaga);
        if (vaga.getEstado().equals(Estado.OCUPADA)) {
            throw new ConflictException("Vaga já ocupada");
        }
        vaga.setEstado(Estado.OCUPADA);
        return repository.save(vaga);
    }

    public Vaga getVaga(Long id) {
        return repository.findByIdAndAtivoTrue(id).orElseThrow(() -> new NotFoundException("registro não encontrado para o id: " + id));
    }

}
