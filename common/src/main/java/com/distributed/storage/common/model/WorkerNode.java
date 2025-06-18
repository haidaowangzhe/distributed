package com.distributed.storage.common.model;

import lombok.Data;
import lombok.Builder;

@Data
@Builder
public class WorkerNode {
    private String nodeId;
    private String host;
    private int port;
    private long availableSpace;
    private boolean active;
    private long lastHeartbeat;
} 