# DeskFlow API

一个基于 Spring Boot 3 的工位预订 API。

## 启动要求

- Java 21
- Maven 或项目自带 `mvnw.cmd`

## 启动服务

默认会连接项目里已经配置好的目标 MySQL 数据库，并自动执行 `schema.sql` 和 `data.sql`。

Windows:

```powershell
.\mvnw.cmd spring-boot:run
```

如果你只想在本地用 H2 启动测试环境：

```powershell
.\mvnw.cmd spring-boot:run "-Dspring-boot.run.profiles=local"
```

## 在 GitHub Codespaces 启动

项目默认 profile 是 `codespace`，在 Codespaces 终端里直接运行：

```bash
./mvnw spring-boot:run
```

如果你想显式指定 profile：

```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=codespace
```

启动后在 Codespaces 的 `Ports` 面板确认 `8080` 已转发，然后访问：

```text
https://<your-codespace>-8080.app.github.dev/api/health
```

如果 Codespaces 无法连接默认 MySQL，可以先使用本地 H2 profile 验证服务：

```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=local
```

## 检查服务

启动后访问：

```text
http://localhost:8080/api/health
```

数据库和服务都正常时返回：

```json
{"status":"ok","database":"ok"}
```

## 运行测试

```powershell
.\mvnw.cmd test
```