package server.Config;

import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AzureConfig {
    // Injectează connection string-ul din application.properties
    @Value("${azure.storage.connection-string}")
    private String connectionString;

    @Bean
    public BlobServiceClient blobServiceClient() {
        // Construiește și returnează clientul principal pentru Azure Storage
        return new BlobServiceClientBuilder()
                .connectionString(connectionString)
                .buildClient();
    }
}
