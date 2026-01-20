package org.mia.service;

import com.oracle.bmc.objectstorage.ObjectStorage;
import com.oracle.bmc.objectstorage.requests.PutObjectRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
public class FileStorageService {

    @Autowired
    private ObjectStorage objectStorageClient;

    @Value("${oci.bucket.name}")
    private String bucketName;

    @Value("${oci.bucket.namespace}")
    private String namespace;

    @Value("${oci.region}")
    private String region;

    public String uploadFile(MultipartFile file) throws IOException {
        String contentType = file.getContentType();
        String folder = determineFolder(contentType);

        // Gera nome único para evitar sobrescrita
        String fileName = UUID.randomUUID() + "-" + file.getOriginalFilename();
        String objectName = folder + "/" + fileName;

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .namespaceName(namespace)
                .bucketName(bucketName)
                .objectName(objectName)
                .contentType(contentType)
                .putObjectBody(file.getInputStream())
                .build();

        objectStorageClient.putObject(putObjectRequest);

        // URL eterna para bucket público (sem listagem de objetos)
        return String.format("https://objectstorage.%s.oraclecloud.com/n/%s/b/%s/o/%s",
                region, namespace, bucketName, objectName);
    }

    private String determineFolder(String contentType) {
        if (contentType == null) return "outros";
        if (contentType.contains("video")) return "videos";
        if (contentType.contains("audio")) return "audios";
        if (contentType.contains("pdf")) return "pdfs";
        if (contentType.contains("image")) return "imagens";
        return "outros";
    }
}