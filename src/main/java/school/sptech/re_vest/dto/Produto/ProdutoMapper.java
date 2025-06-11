package school.sptech.re_vest.dto.Produto;

import school.sptech.re_vest.domain.Imagem;
import school.sptech.re_vest.domain.Produto;

public class ProdutoMapper {
    public static ProdutoResponseDto toProdutoResponseDto(Produto produto){
        ProdutoResponseDto dto = new ProdutoResponseDto();
        dto.setId(produto.getId());
        dto.setNome(produto.getNome());
        dto.setPreco(produto.getPreco());
        dto.setDescricao(produto.getDescricao());
        dto.setCor(produto.getCor());
        dto.setTamanho(produto.getTamanho());
        dto.setTipo(produto.getTipo().toString());
        dto.setCondicao(produto.getCondicao().toString());
        dto.setStatus(produto.getStatus());
        dto.setCategoria(produto.getCategoria().toString());
        dto.setImagem(produto.getImagem());
        return dto;
    }

    public static Produto toProdutoEntity(ProdutoRequestDto dto){
        Produto produto = new Produto();
        produto.setNome(dto.getNome());
        produto.setPreco(dto.getPreco());
        produto.setDescricao(dto.getDescricao());
        produto.setCor(dto.getCor());
        produto.setTamanho(dto.getTamanho());
        produto.setTipo(dto.getTipo());
        produto.setCondicao(dto.getCondicao());
        produto.setStatus(dto.getStatus());
        produto.setCategoria(dto.getCategoria());
        produto.setImagem(dto.getImagem());

        return produto;
    }

    public static Produto toEntityContainsImage(Produto produto, Imagem imagem) {
        produto.setImagem(imagem);
        return produto;
    }
}
