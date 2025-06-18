package com.distributed.storage.master.service;

import com.distributed.storage.common.model.WorkerNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
public class LoadBalancer {
    private final Random random = new Random();
    
    @Autowired
    private WorkerNodeManager workerNodeManager;

    public WorkerNode selectWorker() {
        List<WorkerNode> activeWorkers = workerNodeManager.getAllWorkers().stream()
                .filter(WorkerNode::isActive)
                .toList();
        
        if (activeWorkers.isEmpty()) {
            throw new RuntimeException("No active worker nodes available");
        }
        
        // 简单的随机负载均衡策略
        return activeWorkers.get(random.nextInt(activeWorkers.size()));
    }

    public WorkerNode selectWorkerWithSpace(long requiredSpace) {
        return workerNodeManager.getAllWorkers().stream()
                .filter(WorkerNode::isActive)
                .filter(worker -> worker.getAvailableSpace() >= requiredSpace)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No worker node with sufficient space available"));
    }
} 