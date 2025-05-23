package school.sptech.re_vest.services;

import com.azure.storage.blob.*;
import com.azure.storage.blob.models.BlobServiceProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import school.sptech.re_vest.domain.Imagem;
import school.sptech.re_vest.domain.Produto;
import school.sptech.re_vest.dto.Imagem.ImagemMapper;
import school.sptech.re_vest.dto.Imagem.ImagemResponseDto;
import school.sptech.re_vest.exception.EntidadeNaoEncontradaException;
import school.sptech.re_vest.repositories.ImagemRepository;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ImagemService {

    private final ImagemRepository imagemRepository;
    private final BlobServiceClient blobServiceClient;

    @Value("${azure.storage.access-key}")
    private String accountKey;

    @Value("${azure.storage.container-name}")
    private String containerName;

    @Value("${azure.storage.blob-endpoint}")
    private String blobEndpoint;

    public List<Imagem> listar() {
        return imagemRepository.findAll();
    }

    public Imagem uploadImagem(MultipartFile arquivo) {

        try {

            byte[] imageBytes = arquivo.getBytes();
            String nomeArquivo = arquivo.getOriginalFilename();

            BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(containerName);
            BlobClient blobClient = containerClient.getBlobClient(nomeArquivo);
            blobClient.upload(new ByteArrayInputStream(imageBytes));

            String urlImagem = String.format("%s/%s", blobEndpoint, nomeArquivo);

            Imagem imagemBanco = new Imagem();
            imagemBanco.setNomeArquivo(nomeArquivo);
            imagemBanco.setUrlImagem(urlImagem);
            return imagemRepository.save(imagemBanco);

        } catch (IOException e) {
            e.getMessage();
        }

        return null;
    }

    public void deletarNuvemImagem(String nomeArquivo) {
        try {
            BlobServiceClient blobServiceClient = new BlobServiceClientBuilder()
                    .connectionString(accountKey)
                    .buildClient();

            BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(containerName);

            BlobClient blobClient = containerClient.getBlobClient(nomeArquivo);

            if (blobClient.exists()) {
                blobClient.delete();
                System.out.println("Blob '" + nomeArquivo + "' deleted successfully.");
            } else {
                System.out.println("Blob '" + nomeArquivo + "' does not exist.");
            }

        } catch (Exception e) {
            e.getMessage();
        }

    }

    public Imagem salvarImagem(Imagem imagem) {
        return imagemRepository.save(imagem);
    }

    public Imagem buscarPorId(Integer id) {
        return imagemRepository.findById(id).orElseThrow(() -> new EntidadeNaoEncontradaException("Imagem"));
    }

    public void deletar(Integer id) {
        imagemRepository.delete(buscarPorId(id));
    }
}
        /*
            BlobClient blobClient = blobServiceClient
                    .getBlobContainerClient(this.containerName)
                    .getBlobClient(nomeArquivo);
         */