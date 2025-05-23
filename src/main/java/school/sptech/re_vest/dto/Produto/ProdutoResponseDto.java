package school.sptech.re_vest.dto.Produto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import school.sptech.re_vest.domain.Imagem;
import school.sptech.re_vest.domain.enums.Status;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProdutoResponseDto {
    private Integer id;
    private String nome;
    private Double preco;
    private String descricao;
    private String cor;
    private String tamanho;
    private String tipo;
    private String condicao;
    private Status status;
    private String categoria;

    private Imagem imagem;
}
