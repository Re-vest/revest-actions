package school.sptech.re_vest.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Builder
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class Venda {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "fkevento")
    private Evento evento;
    @ManyToMany
    @JoinTable(
            name = "produtocarrinho",
            joinColumns = @JoinColumn(name = "fkvenda"),
            inverseJoinColumns = @JoinColumn(name = "fkproduto")
    )
    private List<Produto> carrinho;
    @Column(name = "datavenda")
    private LocalDate dataVenda;
    @Column(name = "valortotal")
    private Double valorTotal;
    @ManyToOne
    @JoinColumn(name = "fkusuario")
    private Usuario vendedor;
}
