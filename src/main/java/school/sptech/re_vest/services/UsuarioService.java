package school.sptech.re_vest.services;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import school.sptech.re_vest.api.configuration.security.jwt.GerenciadorTokenJwt;
import school.sptech.re_vest.domain.Usuario;
import school.sptech.re_vest.dto.Usuario.UsuarioMapper;
import school.sptech.re_vest.exception.CredenciaisInvalidasException;
import school.sptech.re_vest.exception.EmailJaCadastradoException;
import school.sptech.re_vest.exception.EntidadeNaoEncontradaException;
import school.sptech.re_vest.repositories.UsuarioRepository;
import school.sptech.re_vest.services.autenticacao.dto.UsuarioLoginDto;
import school.sptech.re_vest.services.autenticacao.dto.UsuarioTokenDto;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private GerenciadorTokenJwt gerenciadorTokenJwt;

    @Autowired
    private AuthenticationManager authenticationManager;
    public List<Usuario> listar(){
        return usuarioRepository.findAll();
    }

    public Usuario criar(Usuario novoUsuario){
        Optional<Usuario> existente = usuarioRepository.findByEmail(novoUsuario.getEmail());

        if (existente.isPresent()){
            throw new EmailJaCadastradoException();
        }

        String senhaCriptografada = passwordEncoder.encode(novoUsuario.getSenha());
        novoUsuario.setSenha(senhaCriptografada);

        return usuarioRepository.save(novoUsuario);
    }

    public Usuario atualizar(Integer id, Usuario usuarioAtualizado){
        if (usuarioRepository.existsById(id)){
            usuarioAtualizado.setId(id);
            return usuarioRepository.save(usuarioAtualizado);
        }
        throw new EntidadeNaoEncontradaException("Usuario");
    }

    public void deletar(Integer id){
        if (!usuarioRepository.existsById(id)){
            throw new EntidadeNaoEncontradaException("Usuario");
        }
        usuarioRepository.deleteById(id);
    }

    public Usuario buscarPorId(Integer id){
        return usuarioRepository.findById(id).orElseThrow(()-> new EntidadeNaoEncontradaException("Usuário de id: "+ id));
    }

    public UsuarioTokenDto autenticar(UsuarioLoginDto usuarioLoginDto) {
        try {
            final UsernamePasswordAuthenticationToken credentials = new UsernamePasswordAuthenticationToken(
                    usuarioLoginDto.getEmail(), usuarioLoginDto.getSenha());

            final Authentication authentication = this.authenticationManager.authenticate(credentials);

            Usuario usuarioAutenticado = usuarioRepository.findByEmail(usuarioLoginDto.getEmail())
                    .orElseThrow(() -> new ResponseStatusException(404, "Email do usuário não cadastrado", null));

            SecurityContextHolder.getContext().setAuthentication(authentication);

            final String token = gerenciadorTokenJwt.generateToken(authentication);

            return UsuarioMapper.of(usuarioAutenticado, token);
        } catch (BadCredentialsException ex) {
            throw new CredenciaisInvalidasException();
        }
    }
}
