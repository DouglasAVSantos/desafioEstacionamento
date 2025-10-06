package com.br.desafio.estacionamento.vaga.service;

import com.br.desafio.estacionamento.shared.ConflictException;
import com.br.desafio.estacionamento.shared.NotFoundException;
import com.br.desafio.estacionamento.vaga.Estado;
import com.br.desafio.estacionamento.vaga.dto.VagaRequest;
import com.br.desafio.estacionamento.vaga.dto.VagaResponse;
import com.br.desafio.estacionamento.vaga.entity.Vaga;
import com.br.desafio.estacionamento.vaga.mapper.VagaMapper;
import com.br.desafio.estacionamento.vaga.repository.VagaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VagaServiceTest {

    @InjectMocks
    private VagaService service;

    @Mock
    private VagaRepository repository;

    private final VagaRequest request = new VagaRequest("A1", Estado.LIVRE);
    private final Vaga vaga = VagaMapper.toEntity(request);
    private final VagaResponse response = VagaMapper.toResponse(vaga);

    @Test
    void deveCadastrarComSucesso() {
        when(repository.findByCodigoAndAtivoTrue(request.codigo())).thenReturn(Optional.empty());
        when(repository.save(any(Vaga.class))).thenReturn(vaga);

        VagaResponse result = service.cadastrar(request);

        assertThat(result).isEqualTo(response);
        verify(repository).findByCodigoAndAtivoTrue(request.codigo());
        verify(repository).save(any(Vaga.class));
    }

    @Test
    void deveLancarConflictExceptionAoCadastrar() {
        when(repository.findByCodigoAndAtivoTrue(request.codigo())).thenReturn(Optional.of(vaga));

        ConflictException ex = assertThrows(ConflictException.class, () -> service.cadastrar(request));

        assertEquals("registro já cadastrado", ex.getMessage());
        verify(repository).findByCodigoAndAtivoTrue(request.codigo());
        verify(repository, never()).save(any(Vaga.class));
    }

    @Test
    void deveEncontrarVagaPorId() {
        when(repository.findByIdAndAtivoTrue(1L)).thenReturn(Optional.of(vaga));

        VagaResponse result = service.findVaga(1L);

        assertThat(result).isEqualTo(response);
        verify(repository).findByIdAndAtivoTrue(1L);
    }

    @Test
    void deveLancarNotFoundExceptionAoBuscarVagaPorId() {
        when(repository.findByIdAndAtivoTrue(1L)).thenReturn(Optional.empty());

        NotFoundException ex = assertThrows(NotFoundException.class, () -> service.findVaga(1L));

        assertEquals("Vaga não encotrada para o id '1'", ex.getMessage());
        verify(repository).findByIdAndAtivoTrue(1L);
    }

    @Test
    void deveEncontrarVagaPorCodigo() {
        when(repository.findByCodigoAndAtivoTrue(request.codigo())).thenReturn(Optional.of(vaga));

        Vaga result = service.findVaga(request.codigo());

        assertThat(result).isEqualTo(vaga);
        verify(repository).findByCodigoAndAtivoTrue(request.codigo());
    }

    @Test
    void deveLancarNotFoundExceptionAoBuscarVagaPorCodigo() {
        when(repository.findByCodigoAndAtivoTrue(request.codigo())).thenReturn(Optional.empty());

        NotFoundException ex = assertThrows(NotFoundException.class, () -> service.findVaga(request.codigo()));

        assertEquals("Vaga não encotrada para o codigo 'A1'", ex.getMessage());
        verify(repository).findByCodigoAndAtivoTrue(request.codigo());
    }

    @Test
    void deveRetornarTodasAsVagasAtivas() {
        List<VagaResponse> listaResponse = List.of(response);
        when(repository.findAllByAtivoTrue()).thenReturn(listaResponse);

        List<VagaResponse> result = service.findAll();

        assertThat(result).isEqualTo(listaResponse);
        verify(repository).findAllByAtivoTrue();
    }

    @Test
    void deveRetornarTodasAsVagasPorEstado() {
        List<VagaResponse> listaResponse = List.of(response);
        when(repository.findAllByAtivoTrueAndEstado(Estado.LIVRE)).thenReturn(listaResponse);

        List<VagaResponse> result = service.findAllByEstado(Estado.LIVRE);

        assertThat(result).isEqualTo(listaResponse);
        verify(repository).findAllByAtivoTrueAndEstado(Estado.LIVRE);
    }

    @Test
    void deveInativarVagaPorId() {
        when(repository.findByIdAndAtivoTrue(1L)).thenReturn(Optional.of(vaga));

        service.inativaById(1L);

        verify(repository).save(any(Vaga.class));
        verify(repository).findByIdAndAtivoTrue(1L);
    }

    @Test
    void deveFazerCheckOutComSucesso() {
        Vaga vagaOcupada = VagaMapper.toEntity(new VagaRequest("A1", Estado.OCUPADA));
        when(repository.findByCodigoAndAtivoTrue("A1")).thenReturn(Optional.of(vagaOcupada));
        when(repository.save(any(Vaga.class))).thenReturn(vagaOcupada);

        Vaga result = service.checkOut("A1");

        assertThat(result.getEstado()).isEqualTo(Estado.LIVRE);
        verify(repository).findByCodigoAndAtivoTrue("A1");
        verify(repository).save(vagaOcupada);
    }

    @Test
    void deveLancarConflictExceptionNoCheckOutComVagaLivre() {
        when(repository.findByCodigoAndAtivoTrue("A1")).thenReturn(Optional.of(vaga));

        ConflictException ex = assertThrows(ConflictException.class, () -> service.checkOut("A1"));

        assertEquals("Vaga não está ocupada", ex.getMessage());
        verify(repository).findByCodigoAndAtivoTrue("A1");
        verify(repository, never()).save(any(Vaga.class));
    }

    @Test
    void deveFazerCheckInComSucesso() {
        when(repository.findByCodigoAndAtivoTrue("A1")).thenReturn(Optional.of(vaga));
        when(repository.save(any(Vaga.class))).thenReturn(vaga);

        Vaga result = service.checkIn("A1");

        assertThat(result.getEstado()).isEqualTo(Estado.OCUPADA);
        verify(repository).findByCodigoAndAtivoTrue("A1");
        verify(repository).save(vaga);
    }

    @Test
    void deveLancarConflictExceptionNoCheckInComVagaOcupada() {
        Vaga vagaOcupada = VagaMapper.toEntity(new VagaRequest("A1", Estado.OCUPADA));
        when(repository.findByCodigoAndAtivoTrue("A1")).thenReturn(Optional.of(vagaOcupada));

        ConflictException ex = assertThrows(ConflictException.class, () -> service.checkIn("A1"));

        assertEquals("Vaga já ocupada", ex.getMessage());
        verify(repository).findByCodigoAndAtivoTrue("A1");
        verify(repository, never()).save(any(Vaga.class));
    }

    @Test
    void deveRetornarVagaComGetVaga() {
        vaga.setId(2L);
        when(repository.findByIdAndAtivoTrue(2L)).thenReturn(Optional.of(vaga));

        Vaga result = service.getVaga(2L);

        assertThat(result).isEqualTo(vaga);
        verify(repository).findByIdAndAtivoTrue(2L);
    }

    @Test
    void deveLancarNotFoundExceptionComGetVaga() {
        when(repository.findByIdAndAtivoTrue(1L)).thenReturn(Optional.empty());

        NotFoundException ex = assertThrows(NotFoundException.class, () -> service.getVaga(1L));

        assertEquals("registro não encontrado para o id: 1", ex.getMessage());
        verify(repository).findByIdAndAtivoTrue(1L);
    }
}