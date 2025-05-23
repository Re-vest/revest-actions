package school.sptech.re_vest.dto.Imagem;

import school.sptech.re_vest.domain.Imagem;

public class ImagemMapper {
    public static ImagemResponseDto toDto(Imagem imagem) {
        ImagemResponseDto dto = new ImagemResponseDto();
        dto.setId(imagem.getId());
        dto.setNomeImagem(imagem.getNomeArquivo());
        dto.setImagemUrl(imagem.getUrlImagem());
        return dto;
    }

}
