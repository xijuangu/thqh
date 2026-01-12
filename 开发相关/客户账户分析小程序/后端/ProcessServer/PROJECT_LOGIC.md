# ProcessServer 项目逻辑文档

本文档详细记录了 `ProcessServer` 项目的核心逻辑、组件关系及数据流向。

## 1. 项目概览

**ProcessServer** 是一个基于 Spring Boot 的后端服务，主要用于处理客户账户分析相关的数据。其核心功能包括：

1.  **数据加密处理**：对敏感数据（如客户ID、资金账号）进行 SM4 加密。
2.  **数据导出 (生成 CSV)**：将数据库中的数据（全量或增量）导出为特定格式的 CSV/Data 文件。
3.  **数据接收与持久化**：接收并解析外部传入的 CSV/Data 文件，将其存入数据库。
4.  **数据一致性检查**：校验不同数据库之间（MySQL vs Oracle）以及文件与数据库之间的数据量一致性。

## 2. 核心组件与架构

### 2.1 控制层 (Controller)

*   **`mainExecute.java`**: 主要的 API 入口，暴露以下核心接口：
    *   `/execute_processData`: 触发数据加密处理（全量或增量）。
    *   `/execute_generateData`: 触发 CSV/Data 文件生成。
    *   `/execute_dataPersistence`: 触发接收文件的解析与入库。
    *   `/checkAmount`: 触发跨数据库的数据量一致性检查。

### 2.2 核心业务服务 (Service/Utils)

*   **`Process_Init.java`**: 处理**初始化全量**数据的加密逻辑。
*   **`Process_Incre.java`**: 处理**增量**数据的加密逻辑。
*   **`buildCSV.java` & `buildCSV_init.java`**: 负责将数据库表数据导出为文件。
*   **`receiveCSV.java`**: 负责读取外部文件并写入备库 (`ads_` 开头的表)。
*   **`CheckAmountUtil.java`**: 负责执行数据一致性校验。
*   **`SM4Util.java`**: 提供 SM4 加密/解密算法实现。

### 2.3 数据访问层 (Mapper/DB)

*   **MySQL (db1)**: 项目的主数据库，存储业务数据（`t_` 表）和接收的数仓数据（`ads_` 表）。
*   **Oracle (db2)**: 外部数据源（如 CRM 或柜台系统），用于数据同步比对。
*   **MyBatis Mappers**: 定义了与数据库交互的 SQL 操作。

## 3. 详细业务逻辑

### 3.1 数据加密处理流程
**目标**: 对本地表中的敏感字段（`client_id`, `fund_account_id`, `trade_account_id`）进行加密脱敏。

**流程**:
1.  **请求入口**: 调用 `/execute_processData?type={1|2}`。
    *   `type=1`: 初始全量处理。
    *   `type=2`: 增量处理。
2.  **分发处理**: 根据表名（如 `t_client_sett`）调用 `Process_Init` 或 `Process_Incre` 中的对应方法。
3.  **核心步骤**:
    *   **提取**: 从表中提取去重后的待加密字段值。
    *   **加密**: 使用 `SM4Util.cipherTextCBC` 对字段值进行加密。
    *   **临时存储**: 将原文与密文的映射关系存入临时表（如 `TCS_encrypt_temp`）。
    *   **回写 (Overwrite)**:
        *   **全量**: 分批（每批 10,000 条）更新原表，将敏感字段替换为密文。
        *   **增量**: 仅更新增量数据部分的敏感字段。

**涉及表**:
*   `t_client_sett` (资金对账表)
*   `t_close_detail` (平仓明细表)
*   `t_delivery` (交割表)
*   `t_execute_result` (行权表)
*   `t_hold_balance` (持仓汇总表)
*   `t_fund_jour` (资金流水表)
*   `t_entrust` (委托流水表)
*   `t_done` (成交表)
*   `t_hold_detail` (持仓明细表)

---

### 3.2 数据导出 (文件生成) 流程
**目标**: 将处理后的数据生成 CSV/Data 文件，供下游系统（如数仓）使用。

**流程**:
1.  **请求入口**: 调用 `/execute_generateData?type={1|2}`。
2.  **文件构建**: `buildCSV` 服务根据日期创建目录结构（如 `.../YYYYMMDD/`）。
3.  **数据写入**:
    *   调用 `buildCSV_init` 中的方法。
    *   查询数据库表数据（全量或增量）。
    *   使用 `CSVWriter` 写入文件。
    *   **格式特征**: 分隔符为 `\u0001`，无引号，空值替换为 `""`。
4.  **校验文件生成**: 生成 `check.json`，包含每个生成文件的文件名、行数 (`rowCount`)、文件大小 (`size`) 和 MD5 值 (`md5`)。

---

### 3.3 数据接收与持久化流程
**目标**: 接收来自数仓或外部系统的分析结果文件，并存入本地备库。

**流程**:
1.  **请求入口**: 调用 `/execute_dataPersistence`。
2.  **文件定位**: 根据日期在指定接收目录下查找 `.data` 文件。
3.  **前置清理**: 清空目标表（`truncateTable`），如 `ads_cfs`。
4.  **解析入库**:
    *   使用 `CSVReader` 读取文件。
    *   按自定义分隔符 `\u0001` 解析每行数据。
    *   映射到 POJO 对象（如 `ads_cfs`）。
    *   分批（每批 50,000 条）插入数据库。
5.  **后置校验**: 调用 `CheckAmountUtil.CheckAmount_DW`，对比 `check.json` 中的行数与数据库实际插入的行数，确保无数据丢失。

**涉及表 (备库)**:
*   `ads_cfs`
*   `ads_faa`
*   `ads_pta`
*   `ads_cfsd`
*   `ads_ptad`

---

### 3.4 数据一致性检查流程
**目标**: 监控数据同步状态，确保本地 MySQL 数据与源头 Oracle 数据一致。

**流程**:
1.  **请求入口**: 调用 `/checkAmount?type={1|2}`。
2.  **读取配置**: 从 `SyncStateTable` 表中获取需要检查的表列表。
3.  **双库查询**:
    *   同时查询 MySQL (Mapper1) 和 Oracle (Mapper2) 中对应表的记录数。
    *   支持全量总数对比和增量记录数对比。
4.  **结果记录**:
    *   比较两边的数量。
    *   将检查结果（0:一致, 1:不一致）、检查时间、两边数量写入 `SyncStateTable`。

## 4. 关键配置

*   **端口**: `8083`
*   **数据库**:
    *   `spring.datasource.db1`: Local MySQL (`thqh`)
    *   `spring.datasource.db2`: Remote Oracle (`ASP`/`wolf`)
*   **加密算法**: SM4 (CBC模式)
