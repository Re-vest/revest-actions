package school.sptech.re_vest.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import school.sptech.re_vest.api.configuration.security.jwt.GerenciadorTokenJwt;
import school.sptech.re_vest.domain.Usuario;
import school.sptech.re_vest.exception.CredenciaisInvalidasException;
import school.sptech.re_vest.exception.EmailJaCadastradoException;
import school.sptech.re_vest.exception.EntidadeNaoEncontradaException;
import school.sptech.re_vest.repositories.UsuarioRepository;
import school.sptech.re_vest.services.autenticacao.dto.UsuarioLoginDto;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class UsuarioServiceTest {

    @InjectMocks
    private UsuarioService usuarioService;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private GerenciadorTokenJwt gerenciadorTokenJwt;

    @Mock
    private AuthenticationManager authenticationManager;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void listar_shouldReturnAllUsers() {
        List<Usuario> usuarios = List.of(new Usuario(), new Usuario());
        when(usuarioRepository.findAll()).thenReturn(usuarios);

        List<Usuario> result = usuarioService.listar();

        assertEquals(2, result.size());
        verify(usuarioRepository).findAll();
    }

    @Test
    void criar_shouldThrowWhenEmailAlreadyExists() {
        Usuario novoUsuario = new Usuario();
        novoUsuario.setEmail("test@example.com");
        novoUsuario.setSenha("123");

        when(usuarioRepository.findByEmail(novoUsuario.getEmail())).thenReturn(Optional.of(new Usuario()));

        assertThrows(EmailJaCadastradoException.class, () -> usuarioService.criar(novoUsuario));
    }

    @Test
    void atualizar_shouldThrowIfUserDoesNotExist() {
        when(usuarioRepository.existsById(1)).thenReturn(false);
        assertThrows(EntidadeNaoEncontradaException.class, () -> usuarioService.atualizar(1, new Usuario()));
    }

    @Test
    void deletar_shouldThrowIfUserDoesNotExist() {
        when(usuarioRepository.existsById(1)).thenReturn(false);
        assertThrows(EntidadeNaoEncontradaException.class, () -> usuarioService.deletar(1));
    }

    @Test
    void buscarPorId_shouldReturnUserIfExists() {
        Usuario usuario = new Usuario();
        when(usuarioRepository.findById(1)).thenReturn(Optional.of(usuario));

        Usuario found = usuarioService.buscarPorId(1);

        assertEquals(usuario, found);
    }

    @Test
    void buscarPorId_shouldThrowIfNotFound() {
        when(usuarioRepository.findById(1)).thenReturn(Optional.empty());
        assertThrows(EntidadeNaoEncontradaException.class, () -> usuarioService.buscarPorId(1));
    }
}
