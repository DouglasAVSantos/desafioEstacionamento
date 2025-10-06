package com.br.desafio.estacionamento.veiculo.service;

import com.br.desafio.estacionamento.shared.ConflictException;
import com.br.desafio.estacionamento.shared.NotFoundException;
import com.br.desafio.estacionamento.veiculo.TipoVeiculo;
import com.br.desafio.estacionamento.veiculo.dto.VeiculoRequest;
import com.br.desafio.estacionamento.veiculo.dto.VeiculoResponse;
import com.br.desafio.estacionamento.veiculo.entity.Veiculo;
import com.br.desafio.estacionamento.veiculo.mapper.VeiculoMapper;
import com.br.desafio.estacionamento.veiculo.repository.VeiculoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VeiculoServiceTest {

    @InjectMocks
    VeiculoService service;

    @Mock
    VeiculoRepository repository;

    VeiculoRequest request = new VeiculoRequest("CG150", "ABC1234", "MOTO");
    Veiculo veiculo = VeiculoMapper.toEntity(request);
    VeiculoResponse response = VeiculoMapper.toResponse(veiculo);

    @Test
    void cadastrarSucesso() {
        when(repository.findByPlaca(request.placa())).thenReturn(Optional.empty());
        when(repository.save(any(Veiculo.class))).thenReturn(veiculo);

        VeiculoResponse result = service.cadastrar(request);

        assertEquals(veiculo.getId(), result.id());
        assertEquals(veiculo.getModelo(), result.modelo());
        assertEquals(veiculo.getPlaca(), result.placa());

        verify(repository).findByPlaca(request.placa());
        verify(repository).save(any(Veiculo.class));
    }

    @Test
    void deveLancarConflictExceptionAoCadastrar() {
        when(repository.findByPlaca(request.placa())).thenReturn(Optional.of(veiculo));
        ConflictException result = assertThrows(ConflictException.class, () -> service.cadastrar(request));

        assertEquals("registro já cadastrado", result.getMessage());

        verify(repository).findByPlaca(request.placa());
        verify(repository, never()).save(any(Veiculo.class));
    }

//    @Test
//    void findVeiculo() {
//
//    }

    @Test
    void testFindVeiculo() {
        when(repository.findByPlaca(request.placa())).thenReturn(Optional.of(veiculo));
        assertThat(service.findVeiculo(request.placa())).isEqualTo(veiculo);

        verify(repository).findByPlaca(request.placa());
    }

    @Test
    void deveLancarIllegalArgumentExceptionAoTentarMappearDeRequestParaEntity() {
        VeiculoRequest requestComErro = new VeiculoRequest("FAZER", "ABC1233", "refrigerante");
        when(repository.findByPlaca(requestComErro.placa())).thenReturn(Optional.empty());
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            service.cadastrar(requestComErro);
        });

        assertEquals("Tipo inválido. Tipos aceitos: " + Arrays.toString(TipoVeiculo.values()), ex.getMessage());
        verify(repository).findByPlaca(requestComErro.placa());
        verify(repository, never()).save(any());
    }

    @Test
    void testFindVeiculoPorId() {
        veiculo.setId(2L);
        when(repository.findById(2L)).thenReturn(Optional.of(veiculo));
        assertThat(service.findVeiculo(2L)).isEqualTo(VeiculoMapper.toResponse(veiculo));

        verify(repository).findById(2L);
    }

    @Test
    void testFindVeiculoNotFound() {
        when(repository.findByPlaca(request.placa())).thenReturn(Optional.empty());
        NotFoundException ex = assertThrows(NotFoundException.class, () -> service.findVeiculo(request.placa()));

        assertThat(ex.getMessage()).isEqualTo("Veiculo não encontrado");
        verify(repository).findByPlaca(request.placa());
    }

    @Test
    void atualizarCadastroComSucesso() {
        when(repository.findById(1L)).thenReturn(Optional.of(veiculo));
        when(repository.findByPlaca(request.placa())).thenReturn(Optional.empty());
        when(repository.save(any(Veiculo.class))).thenReturn(veiculo);

        VeiculoResponse result = service.atualizarCadastro(1L, request);

        assertEquals(veiculo.getId(), result.id());
        assertEquals(veiculo.getModelo(), result.modelo());
        assertEquals(veiculo.getPlaca(), result.placa());

        verify(repository).findById(1L);
        verify(repository).findByPlaca(request.placa());
        verify(repository).save(any(Veiculo.class));
    }

    @Test
    void LancaNotFoundAoAtualizarCadastro() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        NotFoundException result = assertThrows(NotFoundException.class, () -> {
            service.atualizarCadastro(1L, request);
        });

        assertEquals("registro não encontrado para o id: 1", result.getMessage());

        verify(repository).findById(1L);
        verify(repository, never()).findByPlaca(request.placa());
        verify(repository, never()).save(any(Veiculo.class));
    }

    @Test
    void LancaConflictExceptionAoAtualizarCadastro() {
        when(repository.findById(1L)).thenReturn(Optional.of(veiculo));
        when(repository.findByPlaca(request.placa())).thenReturn(Optional.of(veiculo));
        ConflictException result = assertThrows(ConflictException.class, () -> {
            service.atualizarCadastro(1L, request);
        });

        assertEquals("veiculo com a placa '" + request.placa() + "' já cadastrada", result.getMessage());

        verify(repository).findById(1L);
        verify(repository).findByPlaca(request.placa());
        verify(repository, never()).save(any(Veiculo.class));
    }

    @Test
    void findAll() {
        List<VeiculoResponse> listaTeste = List.of(response);
        when(repository.findAll()).thenReturn(List.of(veiculo));

        assertThat(service.findAll()).isEqualTo(listaTeste);

        verify(repository).findAll();

    }
}