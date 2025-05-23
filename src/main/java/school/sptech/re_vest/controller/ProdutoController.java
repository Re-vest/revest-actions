package school.sptech.re_vest.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import school.sptech.re_vest.domain.Imagem;
import school.sptech.re_vest.dto.Produto.ProdutoMapper;
import school.sptech.re_vest.dto.Produto.ProdutoRequestDto;
import school.sptech.re_vest.dto.Produto.ProdutoResponseDto;

import school.sptech.re_vest.domain.Produto;
import school.sptech.re_vest.dto.Produto.VendidosPorDiaDTO;
import school.sptech.re_vest.services.ImagemService;
import school.sptech.re_vest.services.HistoricoService;
import school.sptech.re_vest.services.ProdutoService;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/produtos")
public class ProdutoController {

    private final ProdutoService produtoService;
    private final ImagemService imagemService;
    private final HistoricoService historicoService;

    @Operation(
            summary = "lista todos os produtos",
            description = "endpoint GET que busca todos os produtos no banco de dados"
    )
    @GetMapping
    public ResponseEntity<List<ProdutoResponseDto>> listar(){
        List<Produto> produtos = produtoService.listar();

        if (produtos.isEmpty()){
            return ResponseEntity.noContent().build();
        }

        List<ProdutoResponseDto> listaAuxiliar = produtos.stream()
                .map(ProdutoMapper::toProdutoResponseDto).toList();

        return ResponseEntity.ok(listaAuxiliar);
    }

    @Operation(
            summary = "lista os produtos por ordem de preço",
            description = "endpoint GET que busca produtos com parâmetro, ordenados de forma crescente ('asc') e de forma decrescente ('desc')"
    )
    @GetMapping("/por-preco")
    public ResponseEntity<List<ProdutoResponseDto>> listarPorPreco(@RequestParam(defaultValue = "asc") String ordem) {
        List<Produto> produtos = produtoService.listarPorPreco(ordem);

        if (produtos.isEmpty()) return ResponseEntity.status(204).build();

        List<ProdutoResponseDto> listaAuxiliar = produtos.stream()
                .map(ProdutoMapper::toProdutoResponseDto).toList();

        return ResponseEntity.status(200).body(listaAuxiliar);
    }

    @Operation(
            summary = "busca o número de vendas no evento atual",
            description = "endpoint GET que busca as vendas no atual evento à partir da data do evento"
    )
    @GetMapping("/vendidos-evento")
    public ResponseEntity<Integer> buscarVendasEvento(@RequestParam Integer eventoId) {
        Integer quantidadeVendida = produtoService.getVendasEvento(eventoId);
        return ResponseEntity.status(200).body(quantidadeVendida);
    }

    @Operation(
            summary = "busca as vendas por dia do evento",
            description = "endpoint GET que busca as vendas agrupadas por dia pelo Id do evento"
    )
    @GetMapping("/vendidos-por-dia")
    public ResponseEntity<List<Object[]>> buscarVendasPorDia(@RequestParam Integer eventoId) {
        List<Object[]> vendas = produtoService.buscarVendasPorDia(eventoId);

        if (vendas.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.status(200).body(vendas);
    }

    @Operation(
            summary = "cria um novo produto",
            description = "endpoint POST que cria um novo produto no banco de dados, pode contar uma imagem"
    )

    @PostMapping(consumes = {
            MediaType.APPLICATION_JSON_VALUE,
            MediaType.MULTIPART_FORM_DATA_VALUE
    })
    public ResponseEntity<ProdutoResponseDto> criar(
            @RequestParam("idUsuario") Integer idUsuario,
            @RequestPart("produto") ProdutoRequestDto produtoRequestDto,
            @RequestPart(value = "arquivo", required = false) MultipartFile imagemEnviada
    ) {

        Produto produtoEntity = ProdutoMapper.toProdutoEntity(produtoRequestDto);
        if (imagemEnviada != null && !imagemEnviada.isEmpty()) {
            Imagem imagemSalva = imagemService.uploadImagem(imagemEnviada);
            ProdutoMapper.toEntityContainsImage(produtoEntity, imagemSalva);
        };

        Produto produtoSalvoNoBanco = produtoService.criar(produtoEntity);

        ProdutoResponseDto produtoParaRetornar = ProdutoMapper.toProdutoResponseDto(produtoSalvoNoBanco);

        historicoService.registrarHistorico(idUsuario, "Criação de Produto: " +produtoParaRetornar.getNome());

        return ResponseEntity.status(201).body(produtoParaRetornar);
    }

    @Operation(
            summary = "atualiza um produto existente",
            description = "endpoint PUT que atualiza um produto existente pelo ID"
    )

    @PutMapping(path = "/{id}", consumes = {
            MediaType.APPLICATION_JSON_VALUE,
            MediaType.MULTIPART_FORM_DATA_VALUE
    })
    public ResponseEntity<ProdutoResponseDto> atualizar(
            @RequestParam("idUsuario") Integer idUsuario,
            @PathVariable Integer id,
            @RequestPart("produto") ProdutoRequestDto produtoRequestDto,
            @RequestPart(value = "arquivo", required = false) MultipartFile imagem
    ) {
        Produto produtoEntity = ProdutoMapper.toProdutoEntity(produtoRequestDto);
        if (imagem != null && !imagem.isEmpty()) {
            Imagem imagemPronta = imagemService.uploadImagem(imagem);
            ProdutoMapper.toEntityContainsImage(produtoEntity, imagemPronta);
        };

        Produto produtoAtualizado = produtoService.atualizar(id, produtoEntity);

        ProdutoResponseDto produtoResponseDto = ProdutoMapper.toProdutoResponseDto(produtoAtualizado);

        historicoService.registrarHistorico(idUsuario, "Edição de " + produtoRequestDto.getTipo());
        return ResponseEntity.ok(produtoResponseDto);
    }

    @Operation(
            summary = "deletar um produto",
            description = "endpoint DELETE que deleta um produto do banco de dados com base no ID"
    )
    @DeleteMapping("{id}")
    public ResponseEntity<Void> deletar(@PathVariable Integer id, @RequestParam ("idUsuario") Integer idUsuario){
        produtoService.deletar(id);
        historicoService.registrarHistorico(idUsuario, "Exclusão de produto");
        return ResponseEntity.status(204).build();
    }
}
