package com.distributed.storage.master.service;

import com.distributed.storage.common.model.FileMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class FileMetadataManager {
    private final ConcurrentHashMap<String, FileMetadata> fileMetadataMap = new ConcurrentHashMap<>();
    
    @Autowired
    private WorkerNodeManager workerNodeManager;

    public void saveMetadata(FileMetadata metadata) {
        fileMetadataMap.put(metadata.getFileId(), metadata);
    }

    public FileMetadata getMetadata(String fileId) {
        return fileMetadataMap.get(fileId);
    }

    public void deleteMetadata(String fileId) {
        fileMetadataMap.remove(fileId);
    }

    public List<FileMetadata> getAllMetadata() {
        return List.copyOf(fileMetadataMap.values());
    }

    public FileMetadata getMetadataByFileName(String fileName) {
        return fileMetadataMap.values().stream()
                .filter(metadata -> metadata.getFileName().equals(fileName))
                .findFirst()
                .orElse(null);
    }
} 