package school.sptech.re_vest.dto.Produto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
import school.sptech.re_vest.domain.Imagem;
import school.sptech.re_vest.domain.enums.Categoria;
import school.sptech.re_vest.domain.enums.Condicao;
import school.sptech.re_vest.domain.enums.Status;
import school.sptech.re_vest.domain.enums.Tipo;
import school.sptech.re_vest.dto.Imagem.ImagemRequestDto;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProdutoRequestDto {
    @NotBlank(message = "O produto deve possuir um nome.")
    private String nome;

    @NotNull(message = "O produto deve possuir um preço.")
    @DecimalMin(value = "0.01", message = "O valor do produto deve ser maior do que 0.00")
    private Double preco;

    private String descricao;
    private String cor;
    private String tamanho;

    @NotNull(message = "O produto deve ter um TIPO.")
    private Tipo tipo;

    @NotNull(message = "O produto deve ter uma CONDIÇÃO")
    private Condicao condicao;

    @NotNull(message = "O produto deve ter um STATUS")
    private Status status;

    @NotNull(message = "O produto deve pertencer à uma CATEGORIA")
    private Categoria categoria;

    private Imagem imagem;
}
