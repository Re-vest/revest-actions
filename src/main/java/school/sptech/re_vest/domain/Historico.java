package school.sptech.re_vest.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Historico {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "fkusuario")
    private Integer idUsuario;
    private String acao;
    @Column(name = "datahora")
    private LocalDateTime dataHora;
}
