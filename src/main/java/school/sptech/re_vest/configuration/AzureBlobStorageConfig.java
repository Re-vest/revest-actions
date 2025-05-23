package school.sptech.re_vest.configuration;

import com.azure.storage.blob.BlobServiceClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.azure.storage.blob.BlobServiceClient;

@Configuration
public class AzureBlobStorageConfig {

    @Value("${azure.storage.blob-endpoint}")
    private String blobEndpoint;

    @Value("${azure.storage.account-name}")
    private String accountName;

    @Value("${azure.storage.account-key}")
    private String accountKey;

    @Bean
    public BlobServiceClient blobServiceClient() {
        return new BlobServiceClientBuilder()
                .endpoint(blobEndpoint)
                .credential(new com.azure.storage.common.StorageSharedKeyCredential(accountName, accountKey))
                .buildClient();
    }
}
