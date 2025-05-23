package school.sptech.re_vest.dto.Usuario;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import school.sptech.re_vest.domain.enums.PerfilUsuario;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioRequestDto {
    private String nome;
    private String email;
    private String senha;
    private PerfilUsuario perfil;
}
