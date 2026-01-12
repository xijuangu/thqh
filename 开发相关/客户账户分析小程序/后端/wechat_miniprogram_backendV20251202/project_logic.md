# 微信小程序后端项目逻辑文档

## 1. 项目概况
本项目是一个微信小程序后端服务，主要用于客户账户分析。核心功能包括微信用户一键登录、交易账号绑定（通过 Socket 对接柜台/交易系统）、以及生成 BI 报表的免密访问链接。

## 2. 技术栈
- **核心框架**: Spring Boot 2.7.6
- **数据库**: MySQL 8.0 (持久化用户数据), Redis (缓存 Token 及会话状态)
- **ORM**: MyBatis
- **安全**: Spring Security + JWT, BouncyCastle (加密)
- **其他**: Lombok, RestTemplate (HTTP请求), Socket (TCP通信)

## 3. 核心业务逻辑

### 3.1 用户认证模块 (User Authentication)
负责处理微信小程序的登录和用户身份识别。

**流程逻辑**:
1. **认证请求 (`POST /api-wx/users/auth`)**:
   - 前端传入微信 `code` 和 `type` ("openid" 或 "phone_number")。
   - **OpenID 模式**: 调用微信 `jscode2session` 接口获取 OpenID。若用户不存在，则在 `user` 表创建新用户。
   - **手机号模式**: 
     - 检查 Redis 中是否有缓存的 `access_token`，没有则调用微信接口获取。
     - 使用 `access_token` 和 `code` 调用微信接口解密获取手机号。
     - 根据手机号查找或创建用户。
2. **JWT 颁发**:
   - 认证成功后，生成 JWT (JSON Web Token)，包含 `userId`。
   - Token 返回给前端，后续请求需在 Header 中携带 `Authorization: Bearer <token>`。
3. **身份拦截 (`JwtAuthorizationFilter`)**:
   - 拦截所有 API 请求，解析 JWT。
   - 验证 Token 有效性并提取 `userId`，将认证信息注入 Spring Security 上下文。

**涉及类**:
- `UserController`, `UserService`, `WeChatService`, `JwtUtil`, `JwtAuthorizationFilter`

---

### 3.2 交易账号绑定模块 (Trade Account Binding)
负责将微信用户与实际的金融交易账号绑定。该模块使用 Socket 直接与底层交易柜台/网关通信。

**流程逻辑 (`TradeAccountService`)**:
1. **登录请求 (`POST /api-wx/users/tradeaccount/login`)**:
   - 接收加密的密码 (前端使用 RSA 公钥加密)。
   - 后端使用 RSA 私钥解密密码。
2. **Socket 握手协议 (TCP)**:
   - 建立 Socket 连接（超时 5秒）。
   - **Step 1 (6295)**: 查询 APPID 信息，获取网关时间 (`gatewayTime`) 和时间戳 (`timestamp`)。
   - **Step 2 (6296)**: 看穿式认证。使用时间戳和加密后的网关时间进行二次验证。
   - **Step 3 (6011)**: 客户登录。发送账号、密码、IP。
   - **Step 4 (6297)**: 终端信息采集上报。
   - **Step 5 (6061)**: 客户登出（逻辑上只是验证账号密码正确性，验证完即断开 Socket 连接）。
3. **状态缓存**:
   - 验证通过后，将 `trade_account` 存入 Redis，Key 为 `user:trade_account:{userId}`。
   - 设置有效期（如 3 小时）。

**涉及类**:
- `TradeAccountService`, `TradeAccountLoginRequest`

---

### 3.3 报表访问模块 (BI Reporting)
负责生成外部 BI 系统（可能是 FineReport 等）的免密访问链接。

**流程逻辑 (`ReportService`)**:
1. **获取请求 (`POST /api-wx/reports`)**:
   - 前端请求具体的 `reportCode`。
2. **账号校验**:
   - 从 Redis 中获取当前用户绑定的 `trade_account`。如果没有绑定，返回错误。
3. **参数加密**:
   - **账号加密**: 使用 SM4 算法 (CBC模式) 加密 `trade_account`。
   - **SSO Token 生成**:
     - 构造 JSON 数据：`{domainId, externalUserId, timestamp}`。
     - 使用 RSA 私钥加密该 JSON 数据生成 `ssoToken`。
     - 缓存 `ssoToken` 到 Redis (4分钟) 以复用。
4. **URL 拼接**:
   - 根据配置 (`ReportMapping`) 找到报表对应的路径。
   - 拼接完整 URL，包含加密后的账号参数和 SSO Token。

**涉及类**:
- `ReportController`, `ReportService`, `ReportMappingsConfig`, `EncryptionUtil`

## 4. 数据存储

### 4.1 MySQL (`user` 表)
存储微信用户信息。
- `id`: 主键
- `openid`: 微信 OpenID
- `phone_number`: 手机号
- `nickname`: 昵称
- `created_at`, `updated_at`: 时间戳

### 4.2 Redis
用于临时状态存储。
- `wx:access_token`: 微信接口调用凭证 (缓存 2小时)。
- `user:trade_account:{userId}`: 用户绑定的交易账号 (缓存 3小时)。
- `bi_ssotoken`: BI 系统单点登录 Token (缓存 4分钟)。

## 5. 安全机制
1. **通信安全**: 全站 HTTPS (部署层面)。
2. **接口安全**: JWT 用于无状态认证。
3. **密码安全**: 交易账号密码传输使用 RSA 非对称加密。
4. **数据安全**: 对接 BI 系统时使用 SM4 国密算法加密敏感标识。

## 6. 目录结构说明
```text
src/main/java/com/thqh/wechat_miniprogram_backend/
├── config/             # 配置类 (Redis, Security, ReportMapping)
├── controller/         # 控制层 (API 接口定义)
├── service/            # 业务逻辑层 (核心处理)
├── mapper/             # 数据访问层 (MyBatis)
├── entity/             # 数据库实体
├── dto/                # 数据传输对象 (Req/Resp)
├── filter/             # 过滤器 (JwtAuthorizationFilter)
└── util/               # 工具类 (JwtUtil, EncryptionUtil, RsaUtil)
```
