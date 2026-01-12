# 客户账户分析小程序后端项目逻辑梳理

本文档详细记录了客户账户分析小程序后端项目的整体架构、业务逻辑、API 接口定义以及数据库交互细节。

## 1. 项目概览

- **项目名称**: wechat_miniprogram_management_backend
- **技术栈**: Java, Spring Boot, MyBatis
- **主要功能**: 为微信小程序提供用户行为统计和功能使用情况分析，包括用户增长、活跃度、流失情况以及特定功能模块的使用频率。

## 2. 架构设计

项目采用典型的 Spring Boot 分层架构：

- **Controller 层 (`com.thqh.wechat_miniprogram_management_backend.controller`)**: 处理 HTTP 请求，参数校验，返回统一格式的响应。
- **Service 层 (`com.thqh.wechat_miniprogram_management_backend.service`)**: 封装业务逻辑，组装数据，进行计算（如计算平均使用次数、使用率）。
- **Mapper 层 (`com.thqh.wechat_miniprogram_management_backend.mapper`)**: 使用 MyBatis 进行数据库操作，通过 XML 文件定义 SQL 查询。
- **DTO 层 (`com.thqh.wechat_miniprogram_management_backend.dto`)**: 定义前后端交互的数据传输对象。

## 3. 核心业务逻辑详解

### 3.1 用户统计模块

负责统计用户的生命周期数据，包括新增、活跃、流失以及交易登录情况。

**核心指标定义：**

1.  **累计用户数**: 数据库中所有用户的总数。
2.  **累计疑似流失用户数**: 截止到 T-1 日（昨日），过去 30 天内没有任何访问记录的用户。
3.  **上日活跃用户数**: T-1 日（昨日）有访问记录的去重用户数。
4.  **月新增用户数**: 按月份统计注册（创建）的用户数量。
5.  **月活跃用户数**: 按月份统计有访问记录的去重用户数量。
6.  **月交易登录活跃用户数**: 按月份统计成功登录交易账户（`function_code = 'LOGIN_TRADEACCOUNT'`, `access_status = 1`）的去重用户数量。
7.  **交易登录活跃用户占比**:
    - 公式: `(月交易登录用户数 / 月授权登录用户数) * 100`
    - 其中“授权登录用户数”定义为: `function_code = 'LOGIN'` 且 `access_status = 1` 的记录数（注：此处 SQL 实现为记录数求和，而非去重用户数，需注意业务含义）。

### 3.2 功能使用统计模块

负责统计特定功能模块（以 `REPORT_` 开头的功能编码）的使用情况。支持按**月**（yyyy-MM）或按**日**（yyyy-MM-dd）进行查询。

**核心指标定义：**

1.  **功能使用人数**: 指定时间范围内，使用某功能的去重用户数。
2.  **功能使用总次数**: 指定时间范围内，某功能被访问的总日志条数。
3.  **功能平均使用次数**:
    - 公式: `功能使用总次数 / 功能使用人数`
    - 逻辑: 如果使用人数为 0，则默认为 1（避免除以零，代码逻辑 `getOrDefault(..., 1L)`）。
4.  **功能使用率**:
    - 公式: `(功能使用人数 / 交易登录用户数) * 100`
    - 分母: 指定时间范围内，成功登录交易账户（`LOGIN_TRADEACCOUNT`）的去重用户数。

## 4. API 接口文档

所有接口统一前缀: `/api/stats`
跨域支持: `origins = "*"`

### 4.1 获取用户统计数据

- **路径**: `POST /users`
- **请求体 (`UserStatsRequest`)**:
    ```json
    {
      "year": "2025" // String, 必填
    }
    ```
- **响应体 (`ApiResponse<UserStatsResponse>`)**:
    ```json
    {
      "code": 0,
      "msg": "",
      "data": {
        "totalUsers": 1000,           // 累计用户数
        "suspectedLostUsers": 50,     // 疑似流失用户数
        "yesterdayActiveUsers": 120,  // 昨日活跃用户数
        "monthlyNewUsers": { "2025-01": 100, ... },       // 月新增
        "monthlyActiveUsers": { "2025-01": 800, ... },    // 月活跃
        "monthlyTradeLoginUsers": { "2025-01": 500, ... },// 月交易登录
        "monthlyTradeLoginRate": { "2025-01": 60, ... }   // 月交易登录占比(%)
      }
    }
    ```

### 4.2 获取功能使用统计数据

- **路径**: `POST /functions`
- **请求体 (`FunctionUsageRequest`)**:
    ```json
    {
      "date": "2025-11" // String, 必填。支持 "yyyy-MM" 或 "yyyy-MM-dd"
    }
    ```
- **响应体 (`ApiResponse<FunctionUsageResponse>`)**:
    ```json
    {
      "code": 0,
      "msg": "",
      "data": {
        "functionUserCount": { "REPORT_A": 50, "REPORT_B": 30 },  // 功能使用人数
        "functionUseCount": { "REPORT_A": 200, "REPORT_B": 90 },  // 功能使用次数
        "functionAvgUse": { "REPORT_A": 4.0, "REPORT_B": 3.0 },   // 平均使用次数
        "functionUsageRate": { "REPORT_A": 10.0, "REPORT_B": 6.0 } // 功能使用率(%)
      }
    }
    ```

## 5. 数据库设计推断

根据 SQL 查询推断的数据库表结构：

### 5.1 用户表 (`users`)
| 字段名 | 类型 | 描述 |
| :--- | :--- | :--- |
| id | bigint | 用户主键 ID |
| created_at | datetime | 用户创建时间（注册时间） |

### 5.2 用户访问日志表 (`user_access_log`)
| 字段名 | 类型 | 描述 |
| :--- | :--- | :--- |
| user_id | bigint | 关联用户 ID |
| access_time | datetime | 访问时间 |
| function_code | varchar | 功能编码 (如 `LOGIN`, `LOGIN_TRADEACCOUNT`, `REPORT_...`) |
| access_status | int | 访问状态 (1 表示成功) |

## 6. 关键 SQL 逻辑说明

- **流失判定**: 使用 `NOT EXISTS` 子查询排除掉最近 30 天（`DATE_SUB(CURDATE(), INTERVAL 30 DAY)`）有访问记录的用户。
- **时间维度**: 
    - 统计接口多处使用了 `YEAR()`, `DATE_FORMAT(..., '%Y-%m')` 以及 `DATE()` 函数来处理时间分组。
    - 对于“上日”统计，使用 `access_time >= DATE_SUB(CURDATE(), INTERVAL 1 DAY) AND access_time < CURDATE()` 确保精确获取昨天的数据。
- **功能过滤**: 功能统计仅针对 `function_code LIKE 'REPORT_%'` 的记录。
