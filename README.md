# DeskFlow API

一个基于 Spring Boot 3 的工位预订 API。

## 启动要求

- Java 21
- Maven 或项目自带 `mvnw.cmd`

## 启动服务

默认使用本地 `local` profile，连接本机 MySQL，并自动执行 `schema.sql` 和 `data.sql`。

本地默认数据库配置：

- Host: `localhost`
- Port: `3306`
- Database: `deskflow`
- Username: `root`
- Password: `root`

Windows:

```powershell
.\mvnw.cmd spring-boot:run
```

如果你想显式指定本地 MySQL profile：

```powershell
.\mvnw.cmd spring-boot:run "-Dspring-boot.run.profiles=local"
```

## 在 GitHub Codespaces 启动

Codespaces 需要显式使用 `codespace` profile，在 Codespaces 终端里运行：

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

如果 Codespaces 无法连接默认 MySQL，需要提供可访问的 `DB_URL`、`DB_USERNAME` 和 `DB_PASSWORD`。

例如：

```bash
DB_URL=jdbc:mysql://<host>:3306/deskflow?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC \
DB_USERNAME=<username> \
DB_PASSWORD=<password> \
./mvnw spring-boot:run -Dspring-boot.run.profiles=codespace
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