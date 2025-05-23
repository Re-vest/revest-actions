package school.sptech.re_vest.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import school.sptech.re_vest.domain.Imagem;
import school.sptech.re_vest.dto.Imagem.ImagemMapper;
import school.sptech.re_vest.dto.Imagem.ImagemResponseDto;
import school.sptech.re_vest.services.ImagemService;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/imagens")
public class ImagemController {

    private final ImagemService imagemService;

    @Operation(
            summary = "listar as imagens",
            description = "endpoint GET no qual lista todas as imagens listadas no banco de dados"
    )
    @GetMapping
    public ResponseEntity<List<ImagemResponseDto>> listar() {
        List<Imagem> imagens = imagemService.listar();
        if (imagens.isEmpty()) return ResponseEntity.status(204).build();

        List<ImagemResponseDto> imagensDto = imagens.stream().map(ImagemMapper::toDto).toList();
        return ResponseEntity.status(200).body(imagensDto);
    }
}
