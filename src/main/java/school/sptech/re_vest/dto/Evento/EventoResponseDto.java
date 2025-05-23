package school.sptech.re_vest.dto.Evento;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
public class EventoResponseDto {
    private Integer id;
    private String titulo;
    private String descricao;
    private String cor;
    private LocalDate dataInicio;
    private LocalDate dataFim;
}
