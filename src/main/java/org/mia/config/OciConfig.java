package org.mia.config;

import com.oracle.bmc.ConfigFileReader;
import com.oracle.bmc.auth.ConfigFileAuthenticationDetailsProvider;
import com.oracle.bmc.objectstorage.ObjectStorage;
import com.oracle.bmc.objectstorage.ObjectStorageClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class OciConfig {

    @Value("${oci.config.path}")
    private String configPath;

    @Value("${oci.config.profile}")
    private String profile;

    @Bean
    public ObjectStorage getObjectStorageClient() throws IOException {
        final ConfigFileReader.ConfigFile configFile = ConfigFileReader.parse(configPath, profile);
        final ConfigFileAuthenticationDetailsProvider provider = new ConfigFileAuthenticationDetailsProvider(configFile);

        return ObjectStorageClient.builder().build(provider);
    }
}