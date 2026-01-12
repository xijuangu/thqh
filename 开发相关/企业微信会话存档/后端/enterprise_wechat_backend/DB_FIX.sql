-- 修复数据截断问题 (Data too long for column 'encrypt_chat_msg')
-- 建议在数据库管理工具中执行以下语句

-- 1. 修改临时表 raw_chat_data 的加密消息字段为 LONGTEXT (最大支持4GB，足以容纳任何消息)
ALTER TABLE raw_chat_data MODIFY COLUMN encrypt_chat_msg LONGTEXT;

-- 2. 修改正式表 chat_data 的加密消息字段为 LONGTEXT
ALTER TABLE chat_data MODIFY COLUMN encrypt_chat_msg LONGTEXT;

-- 3. 修改正式表 chat_data 的解密消息字段为 LONGTEXT (解密后的JSON同样会很长)
ALTER TABLE chat_data MODIFY COLUMN chat_msg LONGTEXT;


-- 查询raw_chat_data表中encrypt_chat_msg字段最长的数据
SELECT * 
FROM raw_chat_data 
ORDER BY LENGTH(encrypt_chat_msg) DESC 
LIMIT 1;