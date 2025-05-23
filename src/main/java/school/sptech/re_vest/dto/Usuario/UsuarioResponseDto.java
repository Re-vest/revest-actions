package school.sptech.re_vest.dto.Usuario;

import lombok.Builder;
import lombok.Data;
import school.sptech.re_vest.domain.enums.PerfilUsuario;

@Data
@Builder
public class UsuarioResponseDto {
    private Integer id;
    private String nome;
    private String email;
    private PerfilUsuario perfil;
}
