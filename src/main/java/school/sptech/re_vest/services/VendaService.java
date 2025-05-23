package school.sptech.re_vest.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import school.sptech.re_vest.domain.Venda;
import school.sptech.re_vest.domain.enums.Status;
import school.sptech.re_vest.domain.enums.Tipo;
import school.sptech.re_vest.exception.EntidadeNaoEncontradaException;
import school.sptech.re_vest.repositories.VendaRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class VendaService {
    private final VendaRepository vendaRepository;
    private final ProdutoService produtoService;

    public List<Venda> listar() {
        return vendaRepository.findAll();
    }

    public Venda buscarPorId(int id) {
        return vendaRepository.findById(id).orElseThrow(
                () -> new EntidadeNaoEncontradaException("Venda")
        );
    }

    public Venda salvar(Venda venda) {
        venda.setId(null);

        venda.getCarrinho()
                .forEach(produto -> produtoService.atualizarStatus(produto.getId(), Status.VENDIDO));

        return vendaRepository.save(venda);
    }

    public Integer quantidadeVendasNoDia(Integer eventoId) {
        return vendaRepository.buscarQuantidadeVendasNoDia(eventoId);
    }

    public Double totalVendasNoDia(Integer eventoId) {
        Double valorTotalVendas = vendaRepository.buscarSomaValorTotalVendasNoDia(eventoId);
        return valorTotalVendas != null ? valorTotalVendas : 0.0;
    }

    public Double totalVendasNoEvento(Integer eventoId) {
        Double valorTotalVendas = vendaRepository.buscarSomaValorTotalVendasEvento(eventoId);
        return valorTotalVendas != null ? valorTotalVendas : 0.0;
    }


    public Integer quantidadeVendasNoEvento(Integer eventoId) {
        return vendaRepository.buscarQuantidadeVendasNoEvento(eventoId);
    }



    public List<Map<String, Object>> cincoCategoriaMaisVendida(Integer eventoId) {
        List<Object[]> resultados = vendaRepository.buscarProdutosVendidosNoEvento(eventoId);
        Map<String, Integer> categoriaMaisVendida = new HashMap<>();

        for (Object[] linha : resultados) {
            String categoria = String.valueOf(linha[3]);
            categoriaMaisVendida.put(categoria, categoriaMaisVendida.getOrDefault(categoria, 0) + 1);
        }

        return categoriaMaisVendida.entrySet().stream()
                .sorted((a, b) -> b.getValue().compareTo(a.getValue()))
                .limit(5)
                .map(entry -> {
                    Map<String, Object> map = new HashMap<>();
                    String chave = entry.getKey();
                    String nomeformatado;

                    try {
                        nomeformatado = Tipo.valueOf(chave).getTipo();
                    } catch (IllegalArgumentException e) {
                        nomeformatado = chave;
                    }

                    map.put("categoria", nomeformatado);
                    map.put("quantidade", entry.getValue());
                    return map;
                })
                .toList();
    }

    public List<Map<String, Object>> categoriaMaisVendida(Integer eventoId) {
        List<Object[]> resultados = vendaRepository.buscarProdutosVendidosNoEvento(eventoId);
        Map<String, Integer> categoriaMaisVendida = new HashMap<>();

        for (Object[] linha : resultados) {
            String categoria = String.valueOf(linha[3]);
            categoriaMaisVendida.put(categoria, categoriaMaisVendida.getOrDefault(categoria, 0) + 1);
        }

        return categoriaMaisVendida.entrySet().stream()
                .sorted((a, b) -> b.getValue().compareTo(a.getValue()))
                .limit(1)
                .map(entry -> {
                    Map<String, Object> map = new HashMap<>();
                    String chave = entry.getKey();
                    String nomeformatado;

                    try {
                        nomeformatado = Tipo.valueOf(chave).getTipo();
                    } catch (IllegalArgumentException e) {
                        nomeformatado = chave;
                    }

                    map.put("categoria", nomeformatado);
                    map.put("quantidade", entry.getValue());
                    return map;
                })
                .toList();
    }

    public Double ticketMedioEvento(Integer eventoId) {
        Double valorTotalVendasEvento = vendaRepository.buscarSomaValorTotalVendasEvento(eventoId);
        Integer quantidadeVendasEvento = vendaRepository.buscarQuantidadeVendasNoEvento(eventoId);
        return valorTotalVendasEvento / quantidadeVendasEvento;
    }

    public Double ticketMedioDia(Integer eventoId) {
        Double valorTotalVendasDia = vendaRepository.buscarSomaValorTotalVendasNoDia(eventoId);
        Integer quantidadeVendasDia = vendaRepository.buscarQuantidadeVendasNoDia(eventoId);
        return valorTotalVendasDia / quantidadeVendasDia;
    }
}
