package school.sptech.re_vest.dto.Evento;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class EventoRequestDto {
    private String titulo;
    private String descricao;
    private String cor;
    private LocalDate dataInicio;
    private LocalDate dataFim;
}
