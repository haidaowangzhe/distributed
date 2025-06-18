package com.distributed.storage.worker.service;

import com.distributed.storage.common.model.FileMetadata;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class FileStorageService {
    @Value("${storage.base-path}")
    private String basePath;

    @Value("${storage.temp-path}")
    private String tempPath;

    public FileMetadata storeFile(MultipartFile file) throws IOException {
        String fileId = UUID.randomUUID().toString();
        String fileName = file.getOriginalFilename();
        String contentType = file.getContentType();
        long fileSize = file.getSize();

        // 创建存储目录
        Path storagePath = Paths.get(basePath, fileId);
        Files.createDirectories(storagePath.getParent());

        // 保存文件
        file.transferTo(storagePath);

        // 创建元数据
        return FileMetadata.builder()
                .fileId(fileId)
                .fileName(fileName)
                .fileSize(fileSize)
                .contentType(contentType)
                .storagePath(storagePath.toString())
                .uploadTime(LocalDateTime.now())
                .lastModifiedTime(LocalDateTime.now())
                .build();
    }

    public File getFile(String fileId) {
        Path filePath = Paths.get(basePath, fileId);
        return filePath.toFile();
    }

    public void deleteFile(String fileId) throws IOException {
        Path filePath = Paths.get(basePath, fileId);
        Files.deleteIfExists(filePath);
    }

    public long getAvailableSpace() {
        return new File(basePath).getFreeSpace();
    }
} 