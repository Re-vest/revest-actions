package school.sptech.re_vest.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;
import school.sptech.re_vest.domain.enums.PerfilUsuario;


@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String nome;
    @Column(unique = true)
    @Email
    private String email;
    private String senha;
    @Enumerated(EnumType.STRING)
    @Column(name = "perfilusuario")
    private PerfilUsuario perfil;
}