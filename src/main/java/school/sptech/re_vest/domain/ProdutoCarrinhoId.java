package school.sptech.re_vest.domain;

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import lombok.EqualsAndHashCode;

@Embeddable
@EqualsAndHashCode
public class ProdutoCarrinhoId implements Serializable {
    private Integer produto;
    private Integer venda;

    public ProdutoCarrinhoId() {}

    public ProdutoCarrinhoId(Integer produto, Integer venda) {
        this.produto = produto;
        this.venda = venda;
    }
}