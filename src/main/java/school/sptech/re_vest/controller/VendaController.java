package school.sptech.re_vest.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import school.sptech.re_vest.domain.Evento;
import school.sptech.re_vest.domain.Produto;
import school.sptech.re_vest.domain.Usuario;
import school.sptech.re_vest.domain.Venda;
import school.sptech.re_vest.dto.Venda.VendaMapper;
import school.sptech.re_vest.dto.Venda.VendaRequestDto;
import school.sptech.re_vest.dto.Venda.VendaResponseDto;
import school.sptech.re_vest.services.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/vendas")
public class VendaController {
    private final VendaService vendaService;
    private final ProdutoService produtoService;
    private final UsuarioService usuarioService;
    private final EventoService eventoService;
    private final HistoricoService historicoService;

    @Operation(
            summary = "lista todas as vendas",
            description = "endpoint GET que busca todas as vendas no banco de dados"
    )
    @GetMapping
    public ResponseEntity<List<VendaResponseDto>> listar() {
        List<Venda> vendas = vendaService.listar();
        if (vendas.isEmpty()) return ResponseEntity.status(204).build();

        return ResponseEntity.status(200).body(vendas.stream().map(VendaMapper::toVendaResponseDto).toList());
    }

    @Operation(
            summary = "busca uma venda específica",
            description = "endpoint GET que busca uma venda à partir do ID"
    )
    @GetMapping("/{id}")
    public ResponseEntity<VendaResponseDto> buscarPorId(@PathVariable int id) {
        Venda venda = vendaService.buscarPorId(id);
        return ResponseEntity.status(200).body(VendaMapper.toVendaResponseDto(venda));
    }

    @Operation(
            summary = "cria uma nova venda",
            description = "endpoint POST que cria uma nova venda no banco de dados"
    )
    @PostMapping
    public ResponseEntity<VendaResponseDto> cadastrarVenda(@RequestBody VendaRequestDto vendaRequestDto, @RequestParam("idUsuario") Integer idUsuario) {
        List<Produto> produtosBuscados = vendaRequestDto.getProdutosId().stream()
                .map(produtoService::buscarPorId)
                .toList();

        List<Produto> produtos = produtosBuscados.stream()
                .map(produtoService::validarStatus)
                .toList();

        Usuario vendedor = usuarioService.buscarPorId(vendaRequestDto.getIdVendedor());

        Evento evento = eventoService.buscarPorId(vendaRequestDto.getIdEvento());

        Venda vendaEntity = VendaMapper.toVendaEntity(vendaRequestDto, evento, produtos, vendedor);

        Venda venda = vendaService.salvar(vendaEntity);
        VendaResponseDto dto = VendaMapper.toVendaResponseDto(venda);

        Usuario usuario = usuarioService.buscarPorId(idUsuario);

        historicoService.registrarHistorico(
                usuario.getId(),
                usuario.getNome(),
                "Venda realizada"
        );

        return ResponseEntity.status(200).body(dto);
    }

    @Operation(
            summary = "Buscar total de vendas do dia",
            description = "Endpoint GET que retorna a quantidade total de vendas realizadas na data atual"
    )
    @GetMapping("/quantidade-vendas-dia")
    public ResponseEntity<Integer> quantidadeVendasDia(@RequestParam("eventoId") Integer eventoId) {
        Integer quantidadeVendas = vendaService.quantidadeVendasNoDia(eventoId);

        if (quantidadeVendas == 0 || quantidadeVendas == null) {
            return ResponseEntity.status(204).build();
        }
        return ResponseEntity.status(200).body(quantidadeVendas);
    }

    @Operation(
            summary = "Buscar valor total de vendas do dia",
            description = "Endpoint GET que retorna o valor total de venda realizada na data atual"
    )
    @GetMapping("/valor-vendas-dia")
    public ResponseEntity<Double> valorVendasDia(@RequestParam("eventoId") Integer eventoId) {
        Double valorTotalVendas = vendaService.totalVendasNoDia(eventoId);

        if (valorTotalVendas == 0.0) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(valorTotalVendas);
    }

    @Operation(
            summary = "Buscar total de vendas do dia",
            description = "Endpoint GET que retorna a quantidade total de vendas realizadas no evento"
    )
    @GetMapping("/quantidade-vendas-evento")
    public ResponseEntity<Integer> quantidadeVendasEvento(@RequestParam("eventoId") Integer eventoId) {
        Integer quantidadeVendas = vendaService.quantidadeVendasNoEvento(eventoId);

        if (quantidadeVendas == 0 || quantidadeVendas == null) {
            return ResponseEntity.status(204).build();
        }
        return ResponseEntity.status(200).body(quantidadeVendas);
    }

    @Operation(
            summary = "Buscar resumo de vendas do evento",
            description = "Retorna o resumo das vendas de um evento específico"
    )
    @GetMapping("/valor-vendas-evento")
    public ResponseEntity<Double> valorVendasEvento(@RequestParam("eventoId") Integer eventoId) {
        Double valorTotalVendas = vendaService.totalVendasNoEvento(eventoId);

        if (valorTotalVendas == 0.0) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(valorTotalVendas);
    }

    @Operation(
            summary = "Buscar as 5 categorias mais vendidas de um evento",
            description = "Retorna as categorias com mais produtos vendidos em um determinado evento"
    )
    @GetMapping("/categorias-mais-vendidas")
    public ResponseEntity<List<Map<String, Object>>> categoriasMaisVendidas(@RequestParam("eventoId") Integer eventoId) {
        List<Map<String, Object>> resultado = vendaService.cincoCategoriaMaisVendida(eventoId);
        if (resultado.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(resultado);
    }

    @Operation(
            summary = "Buscar a categoria mai vendida de um evento",
            description = "Retorna a categoria com mais o produto vendido em um determinado evento"
    )
    @GetMapping("/categoria-mais-vendida")
    public ResponseEntity<List<Map<String, Object>>> categoriaMaisVendida(@RequestParam("eventoId") Integer eventoId) {
        List<Map<String, Object>> resultado = vendaService.categoriaMaisVendida(eventoId);
        if (resultado.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(resultado);
    }

    @Operation(
            summary = "Retorna o ticket médio de vendas do evento",
            description = "Retorna o ticket médio de vendas do evento inteiro"
    )
    @GetMapping("/ticket-medio-evento")
    public ResponseEntity<Double> ticketMedioEvento(@RequestParam("eventoId") Integer eventoId) {
        Double valorTotalVendasEvento = vendaService.ticketMedioEvento(eventoId);
        if (valorTotalVendasEvento == 0.0) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(valorTotalVendasEvento);
    }

    @Operation(
            summary = "Retorna o ticket médio de vendas do dia",
            description = "Retorna o ticket médio de vendas do dia inteiro"
    )
    @GetMapping("/ticket-medio-dia")
    public ResponseEntity<Double> ticketMedioDia(@RequestParam("eventoId") Integer eventoId) {
        Double valorTotalVendasDia = vendaService.ticketMedioDia(eventoId);
        if (valorTotalVendasDia == 0.0) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(valorTotalVendasDia);
    }
}