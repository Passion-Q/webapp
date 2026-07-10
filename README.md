# 公文管理系统 (Document Management System)

基于 Spring Boot 的公文管理系统，提供完整的用户认证、公文管理和审核流程功能。

## 🛠️ 技术栈

| 分类 | 技术 | 版本 |
| :--- | :--- | :--- |
| 语言 | Java | 21 |
| 框架 | Spring Boot | 3.2.0 |
| 安全框架 | Spring Security | 6.2.0 |
| 数据库 | MySQL | 8.0+ |
| ORM框架 | Spring Data JPA | 3.2.0 |
| 模板引擎 | Thymeleaf | 3.1.0 |
| JWT库 | JJWT | 0.12.3 |

## ✨ 功能特性

### 用户认证
- 用户注册与登录
- 密码复杂度校验
- 密码修改与重置
- 登录失败次数限制与账户锁定
- JWT Token认证

### 公文管理
- 公文创建、编辑、删除
- 公文状态管理（草稿、待审核、已通过、已驳回）
- 公文分类管理
- 公文搜索与筛选

### 审核流程
- 公文提交审核
- 管理员审核（通过/驳回）
- 审核意见填写

### 安全特性
- BCrypt密码加密存储
- 基于角色的访问控制（RBAC）
- SQL注入防护（JPA参数化查询）
- XSS防护（Thymeleaf自动转义）
- 环境变量配置敏感信息

## 📁 项目结构

```
document-management/
├── src/main/java/com/example/document/
│   ├── controller/          # REST API控制器
│   ├── service/             # 业务逻辑层
│   ├── repository/          # 数据访问层
│   ├── entity/              # 数据库实体
│   ├── dto/                 # 数据传输对象
│   ├── config/              # 配置类
│   ├── security/            # 安全相关组件
│   ├── validation/          # 自定义校验器
│   └── DocumentManagementApplication.java
├── src/main/resources/
│   ├── templates/           # Thymeleaf模板
│   ├── static/              # 静态资源
│   └── application.yml      # 应用配置
├── pom.xml                  # Maven依赖
└── README.md                # 项目说明
```

## 🚀 快速开始

### 环境要求
- Java 21+
- MySQL 8.0+
- Maven 3.8+

### 1. 创建数据库

```sql
CREATE DATABASE document_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

### 2. 配置数据库连接

编辑 `src/main/resources/application.yml`：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/document_db?useSSL=false&serverTimezone=Asia/Shanghai&characterEncoding=UTF-8
    username: your_username
    password: your_password
```

或通过环境变量配置：

```powershell
$env:DB_URL="jdbc:mysql://localhost:3306/document_db?useSSL=true"
$env:DB_USERNAME="your_username"
$env:DB_PASSWORD="your_password"
```

### 3. 运行项目

```bash
mvn spring-boot:run
```

### 4. 访问系统

打开浏览器访问 http://localhost:8080

## 🔐 默认账户

| 用户名 | 密码 | 角色 |
| :--- | :--- | :--- |
| admin | Admin@1234 | 管理员 |
| user | User@1234 | 普通用户 |

## 📋 API接口

### 用户认证
- `POST /login/submit` - 用户登录
- `POST /register` - 用户注册
- `POST /change-password` - 修改密码
- `POST /reset-password` - 重置密码

### 公文管理
- `GET /documents` - 获取公文列表
- `GET /documents/create` - 创建公文页面
- `POST /documents` - 创建公文
- `GET /documents/{id}` - 查看公文详情
- `GET /documents/{id}/edit` - 编辑公文页面
- `POST /documents/{id}` - 更新公文
- `POST /documents/{id}/delete` - 删除公文
- `POST /documents/{id}/submit` - 提交审核
- `POST /documents/{id}/review` - 审核公文

## 🔒 安全配置

### JWT配置
```yaml
jwt:
  secret: ${JWT_SECRET:your_secret_key}
  expiration: ${JWT_EXPIRATION:86400000}
```

### 生产环境建议
- 启用MySQL SSL连接（`useSSL=true`）
- 移除`allowPublicKeyRetrieval=true`参数
- 通过环境变量配置敏感信息
- 定期更新依赖版本

## 📄 许可证

MIT License

## 🤝 贡献

欢迎提交Issue和Pull Request！