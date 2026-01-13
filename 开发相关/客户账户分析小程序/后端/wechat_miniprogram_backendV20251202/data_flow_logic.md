# 数据流与处理逻辑文档

本文档详细描述了系统中的数据流动、处理逻辑以及与数据库（MySQL/Redis）的交互细节。

## 1. 数据库设计 (Schema Design)

### 1.1 MySQL 关系型数据库

主要用于持久化存储用户基础信息和操作日志。

#### 1.1.1 用户表 (`users`)
存储微信小程序用户的基本身份信息。

| 字段名 | 类型 | 说明 | 来源 |
| :--- | :--- | :--- | :--- |
| `id` | BIGINT | 主键，自增 | 数据库自动生成 |
| `phone_number` | VARCHAR | 用户手机号 | 微信授权解密获取 |
| `nickname` | VARCHAR | 用户昵称 | 微信授权或默认生成 |
| `openid` | VARCHAR | 微信 OpenID | 微信 `jscode2session` 接口获取 |
| `created_at` | DATETIME | 创建时间 | 插入时当前时间 |
| `created_by` | BIGINT | 创建人ID | 系统默认 0 |
| `updated_at` | DATETIME | 更新时间 | 更新时当前时间 |
| `updated_by` | BIGINT | 更新人ID | 系统默认 0 |

**对应 Mapper 操作 (`UserMapper.xml`)**:
- `insertUser`: 插入新用户。
- `findUserById`: 根据 ID 查询（用于 JWT 校验后的上下文加载）。
- `findUserByOpenid`: 根据 OpenID 查询（用于 OpenID 登录模式）。
- `findUserByPhoneNumber`: 根据手机号查询（用于手机号快捷登录模式）。
- `updateUser`: 更新用户信息。

#### 1.1.2 用户访问日志表 (`user_access_log`)
记录用户关键操作的行为日志。

| 字段名 | 类型 | 说明 |
| :--- | :--- | :--- |
| `id` | BIGINT | 主键，自增 |
| `user_id` | BIGINT | 用户ID |
| `access_time` | DATETIME | 访问时间 |
| `function_code` | VARCHAR | 功能编码 (如 LOGIN, QUERY_USERINFO) |
| `client_ip` | VARCHAR | 客户端IP |
| `trade_account` | VARCHAR | 关联的交易账号 (如有) |
| `access_status` | INT | 访问状态 (1成功, 0失败) |
| `error_message` | VARCHAR | 错误信息 |
| `created_time` | DATETIME | 创建时间 |

**对应 Mapper 操作 (`UserAccessLogMapper.java`)**:
- `@Insert`: 使用注解直接定义 SQL 插入日志记录。

### 1.2 Redis 键值存储

用于存储高频访问的临时状态、令牌和会话信息。

| Key 模式 | 类型 | TTL (过期时间) | 说明 |
| :--- | :--- | :--- | :--- |
| `wx:access_token` | String | ~2小时 | 微信后端接口调用凭证，避免频繁请求微信服务器。 |
| `user:trade_account:{userId}` | String | 3小时 | 用户当前绑定的交易账号。绑定成功后写入，登出或过期删除。 |
| `user:trade_account_expiration_time:{userId}` | String | 3小时 | 交易账号绑定的具体过期时间字符串。 |
| `bi_ssotoken` | String | 4分钟 | BI 报表系统的单点登录 Token，加密生成，短时有效。 |
| `accessToken` (Spring Cache) | String | - | `WeChatService.getAccessToken` 方法使用的缓存。 |

---

## 2. 详细数据流处理逻辑

### 2.1 用户认证数据流 (User Authentication Flow)

此流程负责将微信的临时身份转化为系统的持久化用户身份。

1.  **前端发起**: 用户点击登录，小程序前端获取 `code` 并发送 `POST /api-wx/users/auth`。
2.  **获取微信身份**:
    *   **OpenID 模式**: 后端请求微信 `jscode2session` 接口 -> 获得 `openid`。
    *   **手机号模式**:
        *   后端检查 Redis `wx:access_token`。不存在则请求微信获取并存入 Redis。
        *   使用 `access_token` + `code` 请求微信解密接口 -> 获得 `phoneNumber`。
3.  **数据库匹配 (MySQL)**:
    *   使用 `openid` 或 `phoneNumber` 查询 `users` 表。
    *   **命中**: 获取现有 `User` 对象。
    *   **未命中**: 创建新 `User` 对象，设置默认昵称等，执行 `insertUser` 写入 `users` 表。
4.  **生成令牌**:
    *   使用 `User.id` 生成 JWT 字符串。
5.  **返回结果**: 返回 JWT + 用户信息给前端。

### 2.2 交易账号绑定数据流 (Trade Account Binding Flow)

此流程不涉及 MySQL 存储，完全依赖内存(Socket)验证和 Redis 状态存储。

1.  **前端发起**: 用户输入交易账号密码，发送 `POST /api-wx/users/tradeaccount/login`。
2.  **解密**: 后端使用 RSA 私钥解密密码。
3.  **外部验证 (Socket)**:
    *   建立 TCP 连接。
    *   发送 6295/6296/6011/6297 指令序列与柜台系统交互。
    *   验证响应报文中的状态码。
4.  **状态持久化 (Redis)**:
    *   验证成功后，**不写入 MySQL**。
    *   写入 Redis: `SET user:trade_account:{userId} = {tradeAccount}`。
    *   写入 Redis: `SET user:trade_account_expiration_time:{userId} = {expireTime}`。
5.  **返回结果**: 返回脱敏后的账号状态。

### 2.3 报表访问数据流 (BI Reporting Flow)

此流程汇聚 MySQL 中的用户身份和 Redis 中的绑定状态，生成外部系统链接。

1.  **前端发起**: 请求 `POST /api-wx/reports` (携带 `reportCode`)。
2.  **身份校验**:
    *   解析 Header 中的 JWT -> 获得 `userId`。
    *   查询 Redis `user:trade_account:{userId}`。
    *   **数据流断路**: 如果 Redis 中无数据，抛出异常（提示未绑定交易账号）。
3.  **参数构建**:
    *   从 Redis 读取 `trade_account` -> SM4 加密 -> `encryptedClientId`。
    *   从 Redis 读取 `bi_ssotoken`。若无，则生成 JSON -> RSA 加密 -> 存入 Redis -> `biSsoToken`。
4.  **链接生成**: 拼接 BI 系统 URL + `encryptedClientId` + `biSsoToken`。
5.  **返回结果**: 返回完整的 HTTPS 链接。

### 2.4 访问日志数据流 (Access Logging Flow)

这是一个横切关注点，由 AOP 切面自动触发。

1.  **触发**: Controller 方法被调用，且带有 `@AccessLog` 注解。
2.  **数据采集**:
    *   `JwtAuthorizationFilter` 将用户信息放入 SecurityContext -> 获取 `userId`。
    *   解析请求参数 -> 获取 `tradeAccount` (如果是登录接口)。
    *   获取 IP 地址、功能编码 (`functionCode`)、执行结果状态。
3.  **持久化 (MySQL)**:
    *   构建 `UserAccessLog` 对象。
    *   调用 `UserAccessLogMapper.insert`。
    *   **最终落库**: 数据写入 `user_access_log` 表。

---

## 3. 数据安全策略

-   **传输层**: 所有数据流均在 HTTPS 通道中传输。
-   **应用层 (MySQL)**: 用户表存储基础信息，敏感的交易账号密码**绝不入库**。
-   **缓存层 (Redis)**: 交易账号仅在 Redis 中短时存储，且随 Session 过期自动清除。
-   **交互层 (Socket)**: 交易密码在传输给柜台前经过特定协议封装，不以明文形式暴露在日志中（日志中仅记录加密串或脱敏信息）。
