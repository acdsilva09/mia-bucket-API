package org.mia.controller;

import org.mia.dto.UploadResponse;
import org.mia.service.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/media")
public class FileController {

    @Autowired
    private FileStorageService storageService;

    @PostMapping("/upload")
    public ResponseEntity<UploadResponse> upload(@RequestParam("file") MultipartFile file) {
        try {
            String publicUrl = storageService.uploadFile(file);
            return ResponseEntity.ok(new UploadResponse(file.getOriginalFilename(), publicUrl));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}