package com.distributed.storage.master.controller;

import com.distributed.storage.common.model.FileMetadata;
import com.distributed.storage.common.model.WorkerNode;
import com.distributed.storage.master.service.FileMetadataManager;
import com.distributed.storage.master.service.LoadBalancer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/files")
public class FileController {
    @Autowired
    private FileMetadataManager fileMetadataManager;

    @Autowired
    private LoadBalancer loadBalancer;

    @Autowired
    private RestTemplate restTemplate;

    @PostMapping("/upload")
    public FileMetadata uploadFile(@RequestParam("file") MultipartFile file) {
        // 选择Worker节点
        WorkerNode worker = loadBalancer.selectWorkerWithSpace(file.getSize());
        
        // 转发文件到Worker节点
        String workerUrl = String.format("http://%s:%d/api/files/upload", worker.getHost(), worker.getPort());
        FileMetadata metadata = restTemplate.postForObject(workerUrl, file, FileMetadata.class);
        
        // 保存元数据
        if (metadata != null) {
            metadata.setWorkerNodeId(worker.getNodeId());
            fileMetadataManager.saveMetadata(metadata);
        }
        
        return metadata;
    }

    @GetMapping("/{fileId}")
    public Resource downloadFile(@PathVariable String fileId) {
        FileMetadata metadata = fileMetadataManager.getMetadata(fileId);
        if (metadata == null) {
            throw new RuntimeException("File not found");
        }

        // 从Worker节点获取文件
        String workerUrl = String.format("http://%s:%d/api/files/%s", 
            metadata.getWorkerNodeId().split(":")[0],
            Integer.parseInt(metadata.getWorkerNodeId().split(":")[1]),
            fileId);
        
        return restTemplate.getForObject(workerUrl, Resource.class);
    }

    @DeleteMapping("/{fileId}")
    public void deleteFile(@PathVariable String fileId) {
        FileMetadata metadata = fileMetadataManager.getMetadata(fileId);
        if (metadata == null) {
            throw new RuntimeException("File not found");
        }

        // 从Worker节点删除文件
        String workerUrl = String.format("http://%s:%d/api/files/%s", 
            metadata.getWorkerNodeId().split(":")[0],
            Integer.parseInt(metadata.getWorkerNodeId().split(":")[1]),
            fileId);
        
        restTemplate.delete(workerUrl);
        fileMetadataManager.deleteMetadata(fileId);
    }

    @GetMapping
    public List<FileMetadata> listFiles() {
        return fileMetadataManager.getAllMetadata();
    }
} 