package com.distributed.storage.common.model;

import lombok.Data;
import lombok.Builder;

import java.time.LocalDateTime;

@Data
@Builder
public class FileMetadata {
    private String fileId;
    private String fileName;
    private long fileSize;
    private String contentType;
    private String storagePath;
    private String workerNodeId;
    private LocalDateTime uploadTime;
    private LocalDateTime lastModifiedTime;
} 