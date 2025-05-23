package school.sptech.re_vest.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Evento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String titulo;
    private String descricao;
    private String cor;
    @Column(name = "datainicio")
    private LocalDate dataInicio;
    @Column(name = "datafim")
    private LocalDate dataFim;
}
