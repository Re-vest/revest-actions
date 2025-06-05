package school.sptech.re_vest.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import school.sptech.re_vest.domain.Evento;
import school.sptech.re_vest.domain.Usuario;
import school.sptech.re_vest.dto.Evento.EventoMapper;
import school.sptech.re_vest.dto.Evento.EventoRequestDto;
import school.sptech.re_vest.dto.Evento.EventoResponseDto;
import school.sptech.re_vest.services.EventoService;
import school.sptech.re_vest.services.HistoricoService;
import school.sptech.re_vest.services.UsuarioService;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/eventos")
public class EventoController {
    private final EventoService eventoService;
    private final HistoricoService historicoService;
    private final UsuarioService usuarioService;

    @Operation(
            summary = "buscar todos os eventos",
            description = "endpoint GET que busca todos os eventos do banco de dados"
    )
    @GetMapping
    public ResponseEntity<List<EventoResponseDto>> listar() {
        List<Evento> eventos = eventoService.listar();
        if (eventos.isEmpty()) return ResponseEntity.status(204).build();

        List<EventoResponseDto> aux = eventos.stream().map(EventoMapper::toEventoResponseDto).toList();


        return ResponseEntity.status(200).body(aux);
    }

    @Operation(
            summary = "criar um novo evento",
            description = "endpoint POST que cria um novo evento no banco de dados"
    )
    @PostMapping
    public ResponseEntity<EventoResponseDto> criar(
            @RequestBody EventoRequestDto eventoRequestDto,
            @RequestParam("idUsuario") Integer idUsuario) {

        Evento evento = EventoMapper.toEventoEntity(eventoRequestDto);
        eventoService.criar(evento);
        EventoResponseDto dto = EventoMapper.toEventoResponseDto(evento);

        Usuario usuario = usuarioService.buscarPorId(idUsuario);

        historicoService.registrarHistorico(
                usuario.getId(),
                usuario.getNome(),
                "Evento criado"
        );
        return ResponseEntity.status(201).body(dto);
    }

    @Operation(
            summary = "atualiza um evento existente",
            description = "endpoint PUT que atualiza um evento existente pelo ID"
    )
    @PutMapping("/{id}")
    public ResponseEntity<EventoResponseDto> atualizar(
            @PathVariable Integer id,
            @RequestBody EventoRequestDto eventoRequestDto,
            @RequestParam("idUsuario") Integer idUsuario) {

        Evento eventoAtualizado = eventoService.editar(id, EventoMapper.toEventoEntity(eventoRequestDto));
        EventoResponseDto dto = EventoMapper.toEventoResponseDto(eventoAtualizado);

        Usuario usuario = usuarioService.buscarPorId(idUsuario);

        historicoService.registrarHistorico(
                usuario.getId(),
                usuario.getNome(),
                "Evento atualizado"
        );
        return ResponseEntity.status(201).body(dto);
    }

    @Operation(
            summary = "deleta um evento",
            description = "endpoint DELETE que deleta um evento existente do banco dados"
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable Integer id, @RequestParam("idUsuario") Integer idUsuario) {
        Usuario usuario = usuarioService.buscarPorId(idUsuario);

        historicoService.registrarHistorico(
                usuario.getId(),
                usuario.getNome(),
                "Evento deletado");

        eventoService.deletar(id);
        return ResponseEntity.status(204).build();
    }

    @Operation(
            summary = "Eventos ativos",
            description = "endpoint GET que retorna os eventos que estão vigentes na data atual"
    )
    @GetMapping("/eventos-ativos")
    public ResponseEntity<List<EventoResponseDto>> listarEventosAtivos() {
        List<Evento> eventosAtivos = eventoService.eventosAtivos();
        if (eventosAtivos.isEmpty()) return ResponseEntity.status(204).build();

        List<EventoResponseDto> aux = eventosAtivos.stream().map(EventoMapper::toEventoResponseDto).toList();

        return ResponseEntity.status(200).body(aux);
    }
}