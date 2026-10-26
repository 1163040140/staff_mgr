# Staff Management System - Migration Guide (Staff Management System - Migration Guide)

> 此项目用于测试技术插件，自学使用

## 项目概述
- **框架**: Spring Boot 3.1.12
- **数据库**: MySQL 8.0 + MyBatis-Plus 3.5.12
- **缓存**: Redis (Spring Data Redis Lettuce)
- **部署目标**: VMware VM 内 Docker 化

## 已完成的配置更改

### application.yml 完整新内容

```yaml
spring:
  datasource:
    url: jdbc:mysql://192.168.209.128:3306/staff_mgr?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=Asia/Shanghai
    username: remote_user
    password: YourStrongPassword@123
    driver-class-name: com.mysql.cj.jdbc.Driver
    hikari:
      connection-timeout: 30000
      minimum-idle: 5
      maximum-pool-size: 20

  data:
    redis:
      host: localhost
      port: 6379
      password: ""  # 如有密码请填写
      database: 0
      timeout: 5000ms
      lettuce:
        pool:
          max-active: 20
          max-idle: 10
          min-idle: 5
          max-wait: 2000ms

mybatis-plus:
  configuration:
    map-underscore-to-camel-case: true
    mapper-locations: classpath:mapper/*.xml
```

- **pom.xml**: 已包含 `spring-boot-starter-data-redis` 依赖，无需新增

---

## 迁移步骤

### 1. 在 VMware VM 内安装 Docker

#### CentOS 系统（推荐）：

```bash
# 更新系统
sudo yum update -y

# 安装依赖
sudo yum install -y yum-utils device-mapper-persistent-data lvm2

# 添加 Docker 仓库
sudo yum-config-manager --add-repo https://download.docker.com/linux/centos/docker-ce.repo

# 安装 Docker Engine
sudo yum install -y docker-ce docker-ce-cli containerd.io docker-compose-plugin

# 启动 Docker
sudo systemctl start docker
sudo systemctl enable docker

# 验证
docker --version
docker compose version
```

#### Ubuntu 系统：

```bash
sudo apt-get update
sudo apt-get install -y ca-certificates curl gnupg lsb-release
curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo gpg --dearmor -o /usr/share/keyrings/docker-archive-keyring.gpg
echo "deb [arch=$(dpkg --print-architecture) signed-by=/usr/share/keyrings/docker-archive-keyring.gpg] \
  https://download.docker.com/linux/ubuntu $(lsb_release -cs) stable" | \
  sudo tee /etc/apt/sources.list.d/docker.list > /dev/null
sudo apt-get update
sudo apt-get install -y docker-ce docker-ce-cli containerd.io docker-compose-plugin
sudo systemctl start docker
sudo systemctl enable docker
```

#### 将当前用户加入 Docker 用户组（避免每次使用 sudo）：

```bash
sudo usermod -aG docker $USER
# 退出并重新登录使配置生效
```

### 2. 导出 VM 上的 MySQL 数据

由于应用当前数据库位于 `192.168.209.128`（VMware NAT 网段），需要在 VM 内执行备份：

```bash
# 进入 VM 后，在 VM 内登录 MySQL
mysql -h 192.168.209.128 -u root -p

# 导出所有数据库
mysqldump -h 192.168.209.128 -u root -p --all-databases --routines --triggers --single-transaction > /tmp/all_databases.sql

# 或导出指定数据库
mysqldump -h 192.168.209.128 -u root -p --routines --triggers staff_mgr > /tmp/staff_mgr.sql
```

### 3. 启动 Docker MySQL 容器

将上一步导出的 SQL 文件放到 `sql_scripts/` 目录，然后：

```bash
# 在 VM 内进入项目目录
cd /path/to/staff_mgr

# 启动 MySQL 容器（docker-compose.yml 会自动导入 sql_scripts/ 中的 SQL）
docker compose up -d mysql

# 查看容器日志
docker logs -f staff_mgr_mysql
```

> **注意**: docker-compose.yml 配置 MySQL 初始化时会自动执行 `/docker-entrypoint-initdb.d/*.sql` 文件。

### 4. 启动 Redis 容器

```bash
docker compose up -d redis

# 验证 Redis 运行
docker exec -it staff_mgr_redis redis-cli ping
# 应返回 PONG
```

### 5. 修改 staff_mgr 项目配置

编辑 `src/main/resources/application.yml`，将 MySQL URL 修改为 Docker 容器地址：

```yaml
# 运行在 VM 同一台主机上的 Docker 容器
url: jdbc:mysql://localhost:3306/staff_mgr

# 或使用 docker0 网桥 IP（适用于宿主机访问容器网络）
# url: jdbc:mysql://172.17.0.2:3306/staff_mgr
```

Redis 配置已就绪（默认 `localhost:6379`），无需修改。

### 6. 构建并运行项目

```bash
# 编译打包
mvn clean package -DskipTests

# 运行
java -jar target/demo-0.0.1-SNAPSHOT.jar
```

---

## 配置详情说明

### MySQL 容器配置

| 参数 | 值 | 说明 |
|------|-----|------|
| 镜像 | mysql:8.0 | 官方 MySQL 8.0 |
| 容器名 | staff_mgr_mysql | 便于管理 |
| ROOT 密码 | root_password | 容器初始化时设置 |
| 库名 | staff_mgr | 自动创建数据库 |
| 应用用户 | app_user | 应用专用用户 |
| 挂载 | mysql_data:/var/lib/mysql | 数据持久化 |
| 端口映射 | 3306:3306 | 宿主机映射 |

### Redis 容器配置

| 参数 | 值 | 说明 |
|------|-----|------|
| 镜像 | redis:7-alpine | 轻量 Alpine 镜像 |
| 容器名 | staff_mgr_redis | - |
| 端口映射 | 6379:6379 | 宿主机映射 |
| 持久化 | appendonly yes | AOF 持久化 |
| 数据卷 | redis_data:/data | 数据持久化 |

### 应用项目 Redis 配置说明

| 配置项 | 值 | 说明 |
|--------|-----|------|
| host | localhost | Redis 服务地址 |
| port | 6379 | Redis 默认端口 |
| database | 0 | 数据库索引 |
| timeout | 5000ms | 连接超时 |
| max-active | 20 | 最大连接数 |
| max-idle | 10 | 最大空闲连接 |
| min-idle | 5 | 最小空闲连接 |
| max-wait | 2000ms | 获取连接最大等待时间 |

---

## 常见问题排查

1. **MySQL 容器启动后导入失败**
   - 检查 `sql_scripts/` 目录下的 SQL 文件是否存在
   - 检查 SQL 文件编码为 UTF-8
   - 查看容器日志：`docker logs staff_mgr_mysql`

2. **应用无法连接 MySQL**
   - 确认容器是否运行：`docker ps`
   - 确认 MySQL 用户密码：容器配置为 `app_user` / `app_password`
   - 修改 `application.yml` 中的 `username` / `password` 为容器定义的值

3. **应用无法连接 Redis**
   - 确认 Redis 容器运行：`docker ps`
   - Redis 默认无密码，`password: ""` 配置正确

4. **网络问题**
   - VM 使用 NAT，IP 为 `192.168.209.128`
   - Docker 容器在 VM 内部运行，直接使用 `localhost` 即可访问

---

## 下一步操作（由用户在 VM 内完成）

1. **安装 Docker 到 VM**：按照上文步骤
2. **导出 VM MySQL 数据**：`mysqldump` 命令导出 SQL 文件
3. **放置 SQL 文件**：复制到项目下的 `sql_scripts/` 目录
4. **创建 sql_scripts 目录**：`mkdir sql_scripts`
5. **启动服务**：`docker compose up -d mysql redis`
6. **修改 application.yml**：将 `url` 改为 `jdbc:mysql://localhost:3306/staff_mgr`
7. **修改 database 配置**：确认 `username: app_user`, `password: app_password`
8. **重新打包项目**：`mvn clean package -DskipTests`
9. **运行项目**：`java -jar target/demo-0.0.1-SNAPSHOT.jar`

---

> **注意**：由于我无法直接访问你的 VMware VM，步骤 1-4 必须由你在 VM 内手动完成。
> 如果遇到任何问题，请提供 VM 内的错误信息以便我继续协助。
