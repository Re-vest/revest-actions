package school.sptech.re_vest.dto.Imagem;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImagemRequestDto {
    private MultipartFile arquivo;
}
