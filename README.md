# distributed

# 分布式文件存储系统

这是一个基于 Spring Boot 和 ZooKeeper 的分布式文件存储系统，支持文件的分布式存储、负载均衡和自动扩展。

## 系统架构

* Master 节点：负责文件元数据管理、负载均衡和请求路由
* Worker 节点：负责实际的文件存储和检索
* ZooKeeper：负责服务注册与发现

## 技术栈

* Java 11
* Spring Boot 2.7.0
* ZooKeeper 3.7.0
* Maven 3.8.1
* Docker & Docker Compose

## 快速开始

### 1. 环境要求

* JDK 11+
* Maven 3.8+
* Docker & Docker Compose
* ZooKeeper 3.7+

### 2. 构建项目

    mvn clean package

### 3. 运行 ZooKeeper

    docker-compose up -d zookeeper

### 4. 运行 Master 节点

    java -jar master/target/master-1.0-SNAPSHOT.jar

### 5. 运行 Worker 节点

    java -jar worker/target/worker-1.0-SNAPSHOT.jar

## API 文档

### 文件上传

    POST /api/files/upload
    Content-Type: multipart/form-data
    
    file: <file>

### 文件下载

    GET /api/files/{fileId}

### 文件删除

    DELETE /api/files/{fileId}

### 文件列表

    GET /api/files

## 配置说明

### Master 节点配置

    server:
      port: 8080
    
    zookeeper:
      connect-string: localhost:2181
      namespace: distributed-storage

### Worker 节点配置

    server:
      port: 8081
    
    zookeeper:
      connect-string: localhost:2181
      namespace: distributed-storage
    
    storage:
      base-path: /data/storage
      temp-path: /data/temp

## 开发说明

1. 项目使用 Maven 多模块结构
2. 使用 Spring Boot 作为基础框架
3. 使用 ZooKeeper 进行服务注册与发现
4. 使用 Docker 进行容器化部署

## 注意事项

1. 确保 ZooKeeper 服务正常运行
2. 确保存储目录具有适当的读写权限
3. 建议在生产环境中使用 HTTPS
4. 建议配置适当的日志级别


