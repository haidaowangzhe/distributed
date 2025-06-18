package com.distributed.storage.master.service;

import com.distributed.storage.common.model.WorkerNode;
import org.apache.curator.framework.CuratorFramework;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class WorkerNodeManager {
    private final ConcurrentHashMap<String, WorkerNode> workerNodes = new ConcurrentHashMap<>();
    
    @Autowired
    private CuratorFramework curatorFramework;

    public void registerWorker(WorkerNode workerNode) {
        workerNodes.put(workerNode.getNodeId(), workerNode);
    }

    public void unregisterWorker(String nodeId) {
        workerNodes.remove(nodeId);
    }

    public List<WorkerNode> getAllWorkers() {
        return List.copyOf(workerNodes.values());
    }

    public WorkerNode getWorker(String nodeId) {
        return workerNodes.get(nodeId);
    }

    public void updateWorkerStatus(String nodeId, boolean active) {
        WorkerNode worker = workerNodes.get(nodeId);
        if (worker != null) {
            worker.setActive(active);
            workerNodes.put(nodeId, worker);
        }
    }
} 