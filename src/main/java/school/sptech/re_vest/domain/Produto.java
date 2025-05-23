package school.sptech.re_vest.domain;

import jakarta.persistence.*;
import lombok.*;
import school.sptech.re_vest.domain.enums.Categoria;
import school.sptech.re_vest.domain.enums.Condicao;
import school.sptech.re_vest.domain.enums.Status;
import school.sptech.re_vest.domain.enums.Tipo;

@Entity
@Table(name = "Produto")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
 public class Produto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String nome;
    private Double preco;
    private String descricao;
    private String cor;
    private String tamanho;
    @Enumerated(EnumType.STRING)
    private Tipo tipo;
    @Enumerated(EnumType.STRING)
    private Condicao condicao;
    @Enumerated(EnumType.STRING)
    @Column(name = "statusproduto")
    private Status status;
    @Enumerated(EnumType.STRING)
    private Categoria categoria;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "fkimagem")
    private Imagem imagem;
}