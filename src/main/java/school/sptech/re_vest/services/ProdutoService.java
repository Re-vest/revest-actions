package school.sptech.re_vest.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import school.sptech.re_vest.domain.Produto;
import school.sptech.re_vest.domain.enums.Tipo;
import school.sptech.re_vest.domain.enums.Status;
import school.sptech.re_vest.exception.EntidadeNaoEncontradaException;
import school.sptech.re_vest.exception.ProdutoNaoDisponivelException;
import school.sptech.re_vest.repositories.ProdutoRepository;
import school.sptech.re_vest.utils.Ordenacoes;

import java.text.Normalizer;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ProdutoService {

    private final ProdutoRepository produtoRepository;
    private final ImagemService imagemService;

    public List<Produto> listar(){
        return produtoRepository.findAll();
    }

    public Produto buscarPorId(int id) {
        return produtoRepository.findById(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Produto"));
    }

    public List<Produto> listarPorPreco(String ordem) {
        List<Produto> produtos = produtoRepository.findAll();

        if (produtos.isEmpty()) return produtos;
        Ordenacoes.quickSortPreco(produtos, 0, produtos.size() - 1);
        if ((Objects.equals(ordem, "desc"))) {
            Ordenacoes.inverterOrdemLista(produtos);
        };

        return produtos;
    }

    private String formatarCategoria(String categoria) {
        String categoriaSemAcento = removerAcentos(categoria);
        return Arrays.stream(Tipo.values())
                .filter(valor -> valor.name().equalsIgnoreCase(categoriaSemAcento))
                .findFirst()
                .map(Tipo::getTipo)
                .orElse(categoria);
    }
    private String removerAcentos(String texto) {
        return Normalizer.normalize(texto, Normalizer.Form.NFD)
                .replaceAll("[^\\p{ASCII}]", "");
    }

    public Produto criar(Produto novoProduto){
        novoProduto.setId(null);
        return produtoRepository.save(novoProduto);
    }

    public Produto atualizar(Integer id, Produto produtoAtualizado){
        Produto produtoExistente = produtoRepository.findById(id).orElseThrow(() -> new EntidadeNaoEncontradaException("Produto"));

        if (produtoExistente.getImagem() != null &&
                (produtoAtualizado.getImagem() == null ||
                        !produtoExistente.getImagem().getId().equals(produtoAtualizado.getImagem().getId()))) {
            Integer idImagem = produtoExistente.getImagem().getId();
            imagemService.deletarNuvemImagem(produtoExistente.getImagem().getNomeArquivo());

            produtoExistente.setImagem(null);
            produtoRepository.save(produtoExistente);

            imagemService.deletar(idImagem);
        }

        produtoAtualizado.setId(id);
        return produtoRepository.save(produtoAtualizado);
    }

    public void deletar(Integer id){
        Produto produto = produtoRepository.findById(id).orElseThrow(() -> new EntidadeNaoEncontradaException("Produto"));
        if (produto.getImagem() != null) {
            imagemService.deletar(produto.getImagem().getId());
        }
        produtoRepository.deleteById(id);
    }

    public void atualizarStatus(Integer produtoId, Status status) {
        Produto produto = produtoRepository.findById(produtoId).orElseThrow(() -> new EntidadeNaoEncontradaException("Produto"));

        produto.setStatus(status);
        produtoRepository.save(produto);
    }

    public Produto validarStatus(Produto produto) {
        if (produto.getStatus() != Status.DISPONIVEL) {
            throw new ProdutoNaoDisponivelException(produto.getId().toString(), produto.getStatus().toString());
        }
        return produto;
    }

    public Integer getVendasEvento(Integer eventoId) {
        return produtoRepository.buscarQuantidadeVendidaNoEvento(eventoId);
    }

    public List<Object[]> buscarVendasPorDia(Integer eventoId) {
        List<Object[]> resultados = produtoRepository.buscarVendasPorDia(eventoId);
        return resultados;
    }
}
