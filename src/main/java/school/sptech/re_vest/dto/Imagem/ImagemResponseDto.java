package school.sptech.re_vest.dto.Imagem;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImagemResponseDto {
    private Integer id;
    private String nomeImagem;
    private String imagemUrl;
}
