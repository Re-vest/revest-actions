package school.sptech.re_vest.controller;

import com.azure.core.annotation.Get;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import school.sptech.re_vest.domain.Usuario;
import school.sptech.re_vest.dto.Usuario.UsuarioMapper;
import school.sptech.re_vest.dto.Usuario.UsuarioRequestDto;
import school.sptech.re_vest.dto.Usuario.UsuarioResponseDto;
import school.sptech.re_vest.exception.EmailJaCadastradoException;
import school.sptech.re_vest.services.UsuarioService;
import school.sptech.re_vest.services.autenticacao.dto.UsuarioLoginDto;
import school.sptech.re_vest.services.autenticacao.dto.UsuarioTokenDto;
import school.sptech.re_vest.utils.Ordenacoes;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/usuarios")
@SecurityRequirement(name="Bearer")
public class UsuarioController {

    private final UsuarioService usuarioService;

    @Operation(
            summary = "lista todos os usuários",
            description = "endpoint GET que busca todos os usuários no banco de dados"
    )
    @GetMapping
    public ResponseEntity<List<UsuarioResponseDto>> listar(){
        List<Usuario> usuarios = usuarioService.listar();

        if(usuarios.isEmpty()){
            return ResponseEntity.noContent().build();
        }

        List<UsuarioResponseDto> listaAuxiliar = new ArrayList<>();

        for (Usuario usuarioDaVez : usuarios){
            UsuarioResponseDto dto = UsuarioMapper.toUsuarioResponseDto(usuarioDaVez);
            listaAuxiliar.add(dto);
        }

        return ResponseEntity.ok(listaAuxiliar);
    }

    @Operation(
            summary = "cria um novo usuário",
            description = "endpoint POST que cria um novo usuário no banco de dados"
    )
    @PostMapping
    public ResponseEntity<?> criar(@RequestBody UsuarioRequestDto usuarioRequestDto){
        try {
            Usuario usuarioEntity = UsuarioMapper.toUsuarioEntity(usuarioRequestDto);

            Usuario usuarioSalvoNoBanco = usuarioService.criar(usuarioEntity);

            UsuarioResponseDto usuarioDtoParaRetornar = UsuarioMapper.toUsuarioResponseDto(usuarioSalvoNoBanco);

            return ResponseEntity.created(null).body(usuarioDtoParaRetornar);
        } catch (EmailJaCadastradoException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
        }
    }

    @Operation(
            summary = "atualiza um usuário existente",
            description = "endpoint PUT que atualiza um usuário existente pelo ID"
    )
    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponseDto> atualizar(
            @PathVariable Integer id,
            @RequestBody UsuarioRequestDto usuarioRequestDto) {

        Usuario usuarioExistente = usuarioService.buscarPorId(id);

        Usuario usuarioEntity = UsuarioMapper.toUsuarioEntity(usuarioRequestDto);

        if (usuarioRequestDto.getSenha() == null || usuarioRequestDto.getSenha().isBlank()) {
            usuarioEntity.setSenha(usuarioExistente.getSenha());
        }

        Usuario usuarioAtualizado = usuarioService.atualizar(id, usuarioEntity);

        UsuarioResponseDto usuarioResponseDto = UsuarioMapper.toUsuarioResponseDto(usuarioAtualizado);

        return ResponseEntity.ok(usuarioResponseDto);
    }


    @Operation(
            summary = "deletar um usuário",
            description = "endpoint DELETE que deleta um usuário do banco de dados com base no ID"
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Integer id){
        usuarioService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "logar usuário",
            description = "endpint POST que faz o login de usuário usando um Token"
    )
    @PostMapping("/login")
    public ResponseEntity<UsuarioTokenDto> login(@RequestBody UsuarioLoginDto usuarioLoginDto) {
        UsuarioTokenDto usuarioTokenDto = this.usuarioService.autenticar(usuarioLoginDto);

        return ResponseEntity.status(200).body(usuarioTokenDto);
    }


    @Operation(
            summary = "Pesquisa de usuário por ID",
            description = "endpoint GET que faz pesquisa de usuário por id"
    )
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDto> buscarPorId(@PathVariable Integer id){
        Usuario usuarioEncontrado = usuarioService.buscarPorId(id);

        if (usuarioEncontrado != null) {
            UsuarioResponseDto dto = UsuarioMapper.toUsuarioResponseDto(usuarioEncontrado);
            return ResponseEntity.ok(dto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @Operation(
            summary = "pesquisa binária por id",
            description = "endpoint GET que faz pesquisa binária por id"
    )
    @GetMapping("/pesquisa/{id}")
    public ResponseEntity<Integer> pesquisa(@PathVariable Integer id){
        List<Usuario> usuarios = usuarioService.listar();
        if(usuarios.isEmpty()){
            return ResponseEntity.noContent().build();
        }

        Ordenacoes.ordenarPorId(usuarios);

        List<Integer> listaAuxiliar = new ArrayList<>();

        for (Usuario usuario : usuarios) {
            listaAuxiliar.add(usuario.getId());
        }

        int resultado = pesquisaBinaria(listaAuxiliar, id);

        if (resultado != -1) {
            return ResponseEntity.ok(resultado);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(resultado);
        }

    }

    private int pesquisaBinaria(List<Integer> lista, int valorPesquisa) {
        int indiceInferior = 0;
        int indiceSuperior = lista.size() - 1;

        while (indiceInferior <= indiceSuperior) {
            int meio = (indiceInferior + indiceSuperior) / 2;

            if (lista.get(meio) == valorPesquisa) {
                return meio;
            } else if (valorPesquisa < lista.get(meio)) {
                indiceSuperior = meio - 1;
            } else {
                indiceInferior = meio + 1;
            }
        }

        return -1;
    }
}
