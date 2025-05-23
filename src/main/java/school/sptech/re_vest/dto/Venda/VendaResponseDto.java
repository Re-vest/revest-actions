package school.sptech.re_vest.dto.Venda;

import lombok.Builder;
import lombok.Data;
import school.sptech.re_vest.domain.Produto;
import school.sptech.re_vest.dto.Produto.ProdutoResponseDto;
import school.sptech.re_vest.dto.Usuario.UsuarioResponseDto;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class VendaResponseDto {
    private Integer id;
    private Integer idEvento;
    private List<ProdutoResponseDto> carrinho;
    private LocalDate dataVenda;
    private Double valorTotal;
    private UsuarioResponseDto usuario;

    public VendaResponseDto(Integer id,Integer idEvento, List<ProdutoResponseDto> carrinho, LocalDate dataVenda, Double valorTotal, UsuarioResponseDto usuario) {
        this.id = id;
        this.idEvento = idEvento;
        this.carrinho = carrinho;
        this.dataVenda = dataVenda;
        this.valorTotal = valorTotal;
        this.usuario = usuario;
    }

    public VendaResponseDto() {}
}
