package school.sptech.re_vest.dto.Usuario;

import school.sptech.re_vest.domain.Usuario;
import school.sptech.re_vest.services.autenticacao.dto.UsuarioTokenDto;

public class UsuarioMapper {
    public static UsuarioResponseDto toUsuarioResponseDto(Usuario usuario) {
        if (usuario == null) {
            return null;
        }

        return UsuarioResponseDto.builder()
                .id(usuario.getId())
                .nome(usuario.getNome())
                .email(usuario.getEmail())
                .perfil(usuario.getPerfil())
                .build();
    }

    public static Usuario toUsuarioEntity(UsuarioRequestDto usuarioRequestDto) {
        if (usuarioRequestDto == null) {
            return null;
        }

        return Usuario.builder()
                .nome(usuarioRequestDto.getNome())
                .email(usuarioRequestDto.getEmail())
                .senha(usuarioRequestDto.getSenha())
                .perfil(usuarioRequestDto.getPerfil())
                .build();
    }

    public static UsuarioTokenDto of(Usuario usuario, String token) {
        UsuarioTokenDto usuarioTokenDto = new UsuarioTokenDto();

        usuarioTokenDto.setUserId(usuario.getId());
        usuarioTokenDto.setEmail(usuario.getEmail());
        usuarioTokenDto.setNome(usuario.getNome());
        usuarioTokenDto.setPerfilUsuario(usuario.getPerfil());
        usuarioTokenDto.setToken(token);

        return usuarioTokenDto;
    }
}
