package com.br.desafio.estacionamento.veiculo.service;

import com.br.desafio.estacionamento.shared.ConflictException;
import com.br.desafio.estacionamento.shared.NotFoundException;
import com.br.desafio.estacionamento.veiculo.dto.VeiculoMapper;
import com.br.desafio.estacionamento.veiculo.dto.VeiculoRequest;
import com.br.desafio.estacionamento.veiculo.dto.VeiculoResponse;
import com.br.desafio.estacionamento.veiculo.entity.Veiculo;
import com.br.desafio.estacionamento.veiculo.repository.VeiculoRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class VeiculoService {

    private final VeiculoRepository repository;

    public VeiculoService(VeiculoRepository veiculoRepository) {
        this.repository = veiculoRepository;
    }

    @Transactional
    public VeiculoResponse cadastrar(VeiculoRequest request) {
        if (repository.findByPlaca(request.placa()).isPresent()) {
            throw new ConflictException("registro já cadastrado");
        }
        Veiculo v = VeiculoMapper.toEntity(request);
        v.setRegistradoEm(LocalDateTime.now());
        return VeiculoMapper.toResponse(repository.save(v));
    }

    public VeiculoResponse findVeiculo(Long id) {
        return VeiculoMapper.toResponse(getVeiculo(id));
    }

    @Transactional
    public VeiculoResponse atualizarCadastro(Long id, VeiculoRequest request) {
        Veiculo v = getVeiculo(id);
        if (repository.findByPlaca(request.placa()).isPresent()) {
            throw new ConflictException("registro já cadastrado");
        }
        v.setModelo(request.modelo());
        v.setTipo(request.tipo());
        v.setPlaca(request.placa());
        return VeiculoMapper.toResponse(v);
    }

    public List<VeiculoResponse> findAll() {
        return repository.findAll().stream().map(VeiculoMapper::toResponse).toList();
    }

    private Veiculo getVeiculo(Long id) {
        return repository.findById(id).orElseThrow(() -> new NotFoundException("registro não encontrado para o id: " + id));
    }
}
