package school.sptech.re_vest.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import school.sptech.re_vest.domain.Evento;
import school.sptech.re_vest.exception.EntidadeNaoEncontradaException;
import school.sptech.re_vest.repositories.EventoRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventoServiceTest {

    @Mock
    private EventoRepository eventoRepository;

    @InjectMocks
    private EventoService eventoService;

    @Test
    @DisplayName("listar() deve retornar lista vazia quando não houver eventos")
    void listar_deveRetornarListaVazia() {
        // Arrange
        when(eventoRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        List<Evento> eventos = eventoService.listar();

        // Assert
        assertTrue(eventos.isEmpty());
        verify(eventoRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("listar() deve retornar lista com eventos quando existirem")
    void listar_deveRetornarListaComEventos() {
        // Arrange
        Evento evento = criarEventoPadrao();
        when(eventoRepository.findAll()).thenReturn(List.of(evento));

        // Act
        List<Evento> eventos = eventoService.listar();

        // Assert
        assertFalse(eventos.isEmpty());
        assertEquals(1, eventos.size());
        verify(eventoRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("buscarPorId() deve retornar evento quando existir")
    void buscarPorId_deveRetornarEvento_QuandoExistir() {
        // Arrange
        Integer id = 1;
        Evento evento = criarEventoPadrao();
        when(eventoRepository.findById(id)).thenReturn(Optional.of(evento));

        // Act
        Evento resultado = eventoService.buscarPorId(id);

        // Assert
        assertNotNull(resultado);
        assertEquals(evento.getTitulo(), resultado.getTitulo());
        verify(eventoRepository, times(1)).findById(id);
    }

    @Test
    @DisplayName("buscarPorId() deve lançar exceção quando evento não existir")
    void buscarPorId_deveLancarExcecao_QuandoNaoExistir() {
        // Arrange
        Integer id = 999;
        when(eventoRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntidadeNaoEncontradaException.class, () -> eventoService.buscarPorId(id));
        verify(eventoRepository, times(1)).findById(id);
    }

    @Test
    @DisplayName("criar() deve salvar e retornar novo evento")
    void criar_deveSalvarERetornarNovoEvento() {
        // Arrange
        Evento novoEvento = criarEventoPadrao();
        novoEvento.setId(null);

        Evento eventoSalvo = criarEventoPadrao();
        eventoSalvo.setId(1);

        when(eventoRepository.save(any(Evento.class))).thenReturn(eventoSalvo);

        // Act
        Evento resultado = eventoService.criar(novoEvento);

        // Assert
        assertNotNull(resultado.getId());
        assertEquals(eventoSalvo.getTitulo(), resultado.getTitulo());
        verify(eventoRepository, times(1)).save(novoEvento);
    }

    @Test
    @DisplayName("editar() deve atualizar e retornar evento quando existir")
    void editar_deveAtualizarERetornarEvento_QuandoExistir() {
        // Arrange
        Integer id = 1;
        Evento eventoExistente = criarEventoPadrao();
        eventoExistente.setId(id);

        Evento eventoAtualizado = criarEventoPadrao();
        eventoAtualizado.setTitulo("Novo Título");
        eventoAtualizado.setDescricao("Nova Descrição");

        when(eventoRepository.findById(id)).thenReturn(Optional.of(eventoExistente));
        when(eventoRepository.save(any(Evento.class))).thenReturn(eventoAtualizado);

        // Act
        Evento resultado = eventoService.editar(id, eventoAtualizado);

        // Assert
        assertEquals(eventoAtualizado.getTitulo(), resultado.getTitulo());
        assertEquals(eventoAtualizado.getDescricao(), resultado.getDescricao());
        verify(eventoRepository, times(1)).findById(id);
        verify(eventoRepository, times(1)).save(eventoExistente);
    }

    @Test
    @DisplayName("editar() deve lançar exceção quando evento não existir")
    void editar_deveLancarExcecao_QuandoNaoExistir() {
        // Arrange
        Integer id = 999;
        Evento eventoAtualizado = criarEventoPadrao();
        when(eventoRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntidadeNaoEncontradaException.class,
                () -> eventoService.editar(id, eventoAtualizado));
        verify(eventoRepository, times(1)).findById(id);
        verify(eventoRepository, never()).save(any());
    }

    @Test
    @DisplayName("deletar() deve remover evento quando existir")
    void deletar_deveRemoverEvento_QuandoExistir() {
        // Arrange
        Integer id = 1;
        when(eventoRepository.existsById(id)).thenReturn(true);

        // Act
        eventoService.deletar(id);

        // Assert
        verify(eventoRepository, times(1)).existsById(id);
        verify(eventoRepository, times(1)).deleteById(id);
    }

    @Test
    @DisplayName("deletar() deve lançar exceção quando evento não existir")
    void deletar_deveLancarExcecao_QuandoNaoExistir() {
        // Arrange
        Integer id = 999;
        when(eventoRepository.existsById(id)).thenReturn(false);

        // Act & Assert
        assertThrows(EntidadeNaoEncontradaException.class,
                () -> eventoService.deletar(id));
        verify(eventoRepository, times(1)).existsById(id);
        verify(eventoRepository, never()).deleteById(id);
    }

    @Test
    @DisplayName("eventosAtivos() deve retornar lista de eventos ativos")
    void eventosAtivos_deveRetornarListaDeEventosAtivos() {
        // Arrange
        Evento eventoAtivo = criarEventoPadrao();
        when(eventoRepository.eventosAtivosNoMomento()).thenReturn(List.of(eventoAtivo));

        // Act
        List<Evento> resultado = eventoService.eventosAtivos();

        // Assert
        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());
        verify(eventoRepository, times(1)).eventosAtivosNoMomento();
    }

    private Evento criarEventoPadrao() {
        Evento evento = new Evento();
        evento.setId(1);
        evento.setTitulo("Evento Teste");
        evento.setDescricao("Descrição do Evento Teste");
        evento.setCor("#FFFFFF");
        evento.setDataInicio(LocalDate.now().plusDays(1));
        evento.setDataFim(LocalDate.now().plusDays(2));
        return evento;
    }
}