package school.sptech.re_vest.dto.Evento;

import school.sptech.re_vest.domain.Evento;

public class EventoMapper {
    public static EventoResponseDto toEventoResponseDto(Evento evento) {
        if (evento == null) return null;

        return EventoResponseDto.builder()
                .id(evento.getId())
                .titulo(evento.getTitulo())
                .descricao(evento.getDescricao())
                .cor(evento.getCor())
                .dataInicio(evento.getDataInicio())
                .dataFim(evento.getDataFim())
                .build();
    }

    public static Evento toEventoEntity(EventoRequestDto dto) {
        if (dto == null) return null;

        return Evento.builder()
                .titulo(dto.getTitulo())
                .descricao(dto.getDescricao())
                .cor(dto.getCor())
                .dataInicio(dto.getDataInicio())
                .dataFim(dto.getDataFim())
                .build();
    }
}
