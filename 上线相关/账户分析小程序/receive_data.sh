#!/bin/bash

# 获取昨日日期（格式：yyyymmdd）
YESTERDAY=$(date -d "yesterday" +"%Y%m%d")

# 定义相关路径
BASE_DIR="/usr/local/Thqh/receiveData"
CSV_DIR="$BASE_DIR/recieve_data/$YESTERDAY"
GPG_FILE="$BASE_DIR/encryptfile_recieve/$YESTERDAY/ADS_KINGSTAR_EXP_$YESTERDAY.tar.gz.gpg"
DECRYPTED_FILE_NAME="$BASE_DIR/decryptfile_recieve/ADS_KINGSTAR_EXP_$YESTERDAY.tar.gz"
DECRYPTED_FILE="$BASE_DIR/decryptfile_recieve"
LOG_FILE="$BASE_DIR/recieve_log/recieve_data.log"

# 初始化日志文件（如果不存在则创建）
if [ ! -f "$LOG_FILE" ]; then
    echo "====== 全局日志文件初始化 - $(date) ======" > "$LOG_FILE"
fi


# 添加运行记录分隔
echo "====== 脚本运行开始 - $(date) ======" >> "$LOG_FILE"


# 定义日志记录函数
log_message() {
    echo "$(date +"%Y-%m-%d %H:%M:%S") - $1" >> "$LOG_FILE"
}

# 第一步：解密文件
log_message "开始解密文件 $GPG_FILE"
gpg --batch --yes --passphrase-file /etc/gpg.password --output "$DECRYPTED_FILE_NAME" --decrypt "$GPG_FILE"
if [ $? -ne 0 ]; then
    log_message "解密失败！停止后续操作。"
    exit 1
fi
log_message "解密成功：$DECRYPTED_FILE_NAME"

# 第二步：切换到解密文件所在目录
log_message "切换到目录 $DECRYPTED_FILE"
cd "$DECRYPTED_FILE" || { log_message "无法切换到目录 $DECRYPTED_FILE"; exit 1; }

mkdir "$CSV_DIR"


# 第三步：解压文件并将所有内容放入当前目录
log_message "开始解压文件 ADS_KINGSTAR_EXP_$YESTERDAY.tar.gz 到目录 $CSV_DIR"
tar -xzvf "ADS_KINGSTAR_EXP_$YESTERDAY.tar.gz" -C "$CSV_DIR" >> "$LOG_FILE" 2>&1
if [ $? -ne 0 ]; then
    log_message "解压失败！停止后续操作。"
    exit 1
fi
log_message "解压成功：所有内容已解压到 $CSV_DIR"

# 添加结束标志
echo "====== 脚本运行结束 - $(date) ======" >> "$LOG_FILE"

# 控制台提示完成
echo "脚本执行完成，详细日志请查看: $LOG_FILE"
