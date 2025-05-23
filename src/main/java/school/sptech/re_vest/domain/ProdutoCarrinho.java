package school.sptech.re_vest.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity(name = "produtocarrinho")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProdutoCarrinho {
    @EmbeddedId
    private ProdutoCarrinhoId id;

    @ManyToOne
    @MapsId("produto")
    @JoinColumn(name = "fkproduto", nullable = false)
    private Produto produto;

    @ManyToOne
    @MapsId("venda")
    @JoinColumn(name = "fkvenda", nullable = false)
    private Venda venda;
}
