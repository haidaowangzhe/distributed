package com.distributed.storage.worker.service;

import com.distributed.storage.common.model.WorkerNode;
import org.apache.curator.framework.CuratorFramework;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.net.InetAddress;

@Service
public class WorkerRegistrationService {
    @Value("${server.port}")
    private int port;

    @Autowired
    private CuratorFramework curatorFramework;

    @Autowired
    private FileStorageService fileStorageService;

    private String nodeId;

    @PostConstruct
    public void init() throws Exception {
        // 生成节点ID
        nodeId = InetAddress.getLocalHost().getHostName() + ":" + port;
        
        // 注册到ZooKeeper
        registerWithZooKeeper();
    }

    private void registerWithZooKeeper() throws Exception {
        String path = "/workers/" + nodeId;
        
        // 创建节点数据
        WorkerNode workerNode = WorkerNode.builder()
                .nodeId(nodeId)
                .host(InetAddress.getLocalHost().getHostAddress())
                .port(port)
                .availableSpace(fileStorageService.getAvailableSpace())
                .active(true)
                .lastHeartbeat(System.currentTimeMillis())
                .build();

        // 创建临时节点
        curatorFramework.create()
                .creatingParentsIfNeeded()
                .withMode(org.apache.zookeeper.CreateMode.EPHEMERAL)
                .forPath(path, workerNode.toString().getBytes());
    }

    public String getNodeId() {
        return nodeId;
    }
} 