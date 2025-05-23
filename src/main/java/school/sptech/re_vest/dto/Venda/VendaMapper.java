package school.sptech.re_vest.dto.Venda;

import school.sptech.re_vest.domain.Evento;
import school.sptech.re_vest.domain.Produto;
import school.sptech.re_vest.domain.Usuario;
import school.sptech.re_vest.domain.Venda;
import school.sptech.re_vest.dto.Produto.ProdutoMapper;
import school.sptech.re_vest.dto.Produto.ProdutoResponseDto;
import school.sptech.re_vest.dto.Usuario.UsuarioResponseDto;

import java.time.LocalDate;
import java.util.List;

public class VendaMapper {

    public static VendaResponseDto toVendaResponseDto(Venda venda) {
        if (venda == null) return null;

        List<ProdutoResponseDto> produtosDto = venda.getCarrinho().stream().map(ProdutoMapper::toProdutoResponseDto).toList();

        UsuarioResponseDto usuarioResponseDto = UsuarioResponseDto.builder()
                .id(venda.getVendedor().getId())
                .nome(venda.getVendedor().getNome())
                .email(venda.getVendedor().getEmail())
                .perfil(venda.getVendedor().getPerfil())
                .build();

        return VendaResponseDto.builder()
                .id(venda.getId())
                .idEvento(venda.getEvento().getId())
                .carrinho(produtosDto)
                .valorTotal(venda.getValorTotal())
                .dataVenda(venda.getDataVenda())
                .usuario(usuarioResponseDto)
                .build();
    }

    public static Venda toVendaEntity(VendaRequestDto dto, Evento evento, List<Produto> listaProdutos, Usuario vendedor) {
        if (dto == null) return null;

        Double valorTotal = listaProdutos.stream()
                .mapToDouble(Produto::getPreco)
                .sum();

        return Venda.builder()
                .evento(evento)
                .carrinho(listaProdutos)
                .dataVenda(dto.getDate() != null ? dto.getDate() : LocalDate.now())
                .valorTotal(valorTotal)
                .vendedor(vendedor) // falta inserir aqui o objeto Usuario
                .build();
    }
}
