# 企业微信会话存档后端项目逻辑文档

## 1. 项目概览

本项目是一个基于 **Spring Boot 2.7.6** 的后端系统，专门用于对接**企业微信会话内容存档 (Message Audit)** 功能。核心目标是拉取、解密、存储企业微信的聊天记录（包括文本、图片、文件等），并提供 API 供前端查询和展示。

### 1.1 技术栈
- **核心框架**: Spring Boot 2.7.6
- **数据库**: MySQL 8.0 (ORM: MyBatis 2.2.2 + PageHelper)
- **缓存**: Redis (用于缓存 AccessToken 等)
- **对象存储**: MinIO (用于存储图片、语音、视频等大文件)
- **定时任务**: Quartz (用于定时拉取消息和同步通讯录)
- **企微 SDK**: Native C SDK (通过 JNI/JNA 调用 `com.tencent.wework.Finance`，封装在 `WeChatSdkUtil`)
- **加密**: RSA (BouncyCastle, 私钥解密随机密钥) + AES (SDK 内部解密消息体)

---

## 2. 核心业务流程 (The Archiving Pipeline)

这是项目最核心的逻辑，即"从企微拉取消息到最终落库"的全过程。该过程被设计为一个多步骤的流水线。

### 2.1 流程图解
1. **Fetch (拉取)** -> 2. **Transfer (流转)** -> 3. **Decrypt (解密)** -> 4. **Parse (解析)** -> 5. **Media (媒体处理)**

### 2.2 详细步骤

#### Step 1: 原始数据拉取 (Fetch)
- **触发入口**: `DownloadChatMessageJob` (定时任务).
- **执行逻辑**: `WeChatService.getChatData(request)`.
- **过程**:
    1. 获取当前的 `seq` (游标)，如果是自动模式则从数据库 `chat_data` 表获取最大 `seq`。
    2. 调用 Native SDK (`WeChatSdkUtil.getChatData`) 拉取加密的 JSON 数据。
    3. 数据被封装为 `RawChatData` 对象。
    4. **存储**: 批量插入到临时表 `raw_chat_data`。
    5. **循环**: 如果一次拉取满 1000 条，会继续循环拉取，直到数据取完。

#### Step 2: 数据流转 (Transfer)
- **执行逻辑**: `ChatDataService.insertRawChatDataIntoChatData()`.
- **过程**:
    - 将 `raw_chat_data` 表中的数据"搬运"到正式表 `chat_data`。
    - 这一步可能是为了将高频的写入（拉取）与复杂的后续处理（解密/解析）解耦。

#### Step 3: 消息解密 (Decrypt)
- **执行逻辑**: `ChatDataService.decryptChatData()`.
- **过程**:
    1. 查询 `chat_data` 表中 `decrypt_flag = 0` (未解密) 的记录。
    2. 加载 RSA 私钥 (`rsa_private_key.pem`)。
    3. **解密随机密钥**: 使用 RSA 私钥解密 `encrypt_random_key` 得到明文的 `encrypt_key`。
    4. **解密消息体**: 调用 Native SDK (`WeChatSdkUtil.decryptData`)，传入 `encrypt_key` 和 `encrypt_chat_msg`。
    5. **更新**: 将解密后的 JSON 字符串更新回 `chat_data.chat_msg` 字段，并将 `decrypt_flag` 置为 1。

#### Step 4: 消息解析与生成 (Parse)
- **执行逻辑**: `ChatDataService.generateChatMessage()`.
- **过程**:
    1. 查询 `chat_data` 表中 `generate_msg_flag = 0` (未生成) 的记录。
    2. 解析 JSON 字符串，根据 `msgtype` (text, image, revoke, etc.) 提取字段。
    3. **生成消息**: 构造 `ChatMessage` 对象存入 `chat_message` 表 (业务主表)。
    4. **生成媒体索引**: 如果包含媒体文件 (image, voice, video, file, emotion)，构造 `ChatMedia` 对象存入 `chat_media` 表，初始状态为未下载。
    5. **更新状态**: 将 `chat_data.generate_msg_flag` 置为 1。

#### Step 5: 媒体文件处理 (Media Handling)
- **执行逻辑**: `ChatMediaService.downloadMediaDataAndUpload()`.
- **过程**:
    1. 查询 `chat_media` 表中 `download_status = 0` (未下载) 的记录。
    2. **下载**: 调用 SDK (`WeChatSdkUtil.downloadMedia`) 通过 `sdkfileid` 将文件下载到本地临时目录 (`/tmp/...`)。
    3. **校验**: 如果有 MD5，计算本地文件 MD5 并校验。
    4. **上传**: 调用 `MinioUtil.uploadFile` 将文件上传至 MinIO 对象存储。
    5. **更新**: 将 MinIO 的 URL 更新到 `chat_media.download_url`，并将 `download_status` 置为 1。
    6. **清理**: 删除本地临时文件。

---

## 3. 数据同步逻辑 (Sync)

除了消息存档，系统还需要同步组织架构和客户信息，以便在聊天记录中显示正确的人名和部门。

### 3.1 内部员工同步
- **入口**: `WeChatService.getWechatUser()`.
- **逻辑**:
    1. 获取通讯录 AccessToken。
    2. 获取成员 ID 列表 (`getMemberIdList`)。
    3. 遍历 ID 获取详细信息 (`getUserDetail`)。
    4. 获取开启会话存档的成员列表 (`getPermitUserList`) 并标记。
    5. 对比数据库，执行**增量更新** (Insert/Update/离职标记)。

### 3.2 外部联系人/客户同步
- **入口**: `WeChatService.getCustomer()`.
- **逻辑**:
    1. 获取所有在职员工 ID。
    2. 分批 (Batch 100) 调用 `batchGetCustomerDetail` 接口。
    3. 存入临时表 `raw_customer`。
    4. 通过 SQL 逻辑合并到 `customer` 和 `customer_follow` 表。

### 3.3 群聊信息同步
- **入口**: `WeChatService.getGroupChat()`.
- **逻辑**:
    1. 从 `chat_message` 表中提取所有不重复的 `roomid`。
    2. 尝试调用**客户群详情接口** (`getGroupChatCustomerInfo`)。
    3. 如果失败 (非客户群)，尝试调用**内部群详情接口** (`getGroupChatInternalInfo`)。
    4. 更新 `group_chat` 和 `group_chat_member` 表。

---

## 4. API 接口 (Controller Layer)

提供给前端用于展示会话数据的接口。主要集中在 `ChatMessageController`。

- **获取会话列表**: `/api/chatmessage/conversations`
    - 根据时间范围、关键词查询会话线程。
- **获取会话详情**: `/api/chatmessage/detail`
    - 获取指定会话内的具体消息流。
- **获取联系人列表**: `/api/chatmessage/chatter`
    - 获取内部员工或外部联系人列表，用于筛选。
- **媒体文件下载**: `/api/chatmessage/chatmedia/download`
    - 代理下载功能，前端通过 `sdkfileid` 请求，后端读取文件流返回 (注意：这里直接返回二进制流，不是重定向到 MinIO)。

---

## 5. 关键数据库表设计 (Entity Mapping)

- **`raw_chat_data`**: 原始加密数据的临时缓冲区。
- **`chat_data`**: 核心存档表，包含加密数据 (`encrypt_chat_msg`) 和解密后的 JSON (`chat_msg`)。
- **`chat_message`**: 解析后的结构化消息表 (发送者、接收者、内容、时间)。
- **`chat_media`**: 媒体文件索引表，关联 `sdkfileid` 和 MinIO URL。
- **`wechat_user`**: 企业内部员工表。
- **`customer`**: 外部客户表。
- **`group_chat`**: 群聊基本信息表。

## 6. 配置项 (Configuration)

位于 `application.properties` 或 `WeChatApiConfig.java`：
- **`wechat.qyapi`**: 包含 corpid, secrets (通讯录、会话存档、自建应用)。
- **`minio`**: Endpoint, accessKey, secretKey, bucketName。
- **`rsa`**: 私钥文件路径 (用于解密会话密钥)。
