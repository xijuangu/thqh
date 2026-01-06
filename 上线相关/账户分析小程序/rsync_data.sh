

# 获取昨日日期
YESTERDAY=$(date -d "yesterday" +%Y%m%d)

# 定义路径
RSYNC_PATH="/usr/local/Thqh/rsyncData"
FOLDER_PATH="/$RSYNC_PATH/rsync_data/${YESTERDAY}/"
OUTPUT_FILE="$FOLDER_PATH/S_KINGSTAR_${YESTERDAY}.list"
SOURCE_DIR="$FOLDER_PATH"
TAR_FILE="/$RSYNC_PATH/encryptfile_rsync/ODS_KINGSTAR_${YESTERDAY}.tar.gz"
LOG_FILE="/$RSYNC_PATH/rsync_log/rsync_data.log"

# 初始化日志文件（如果不存在则创建）
if [ ! -f "$LOG_FILE" ]; then
    echo "====== 全局日志文件初始化 - $(date) ======" > "$LOG_FILE"
fi

# 添加运行记录分隔
echo "====== 脚本运行开始 - $(date) ======" >> "$LOG_FILE"

# Step 1: 生成检查文件
echo "Step 1: 生成检查文件" >> "$LOG_FILE"
if [ ! -d "$FOLDER_PATH" ]; then
    echo "错误: 目标文件夹不存在 - $FOLDER_PATH" >> "$LOG_FILE"
    exit 1
fi


FIRST_ENTRY=true
for FILE_PATH in "$FOLDER_PATH"/*.data; do
    [ -e "$FILE_PATH" ] || continue
    FILE_NAME=$(basename "$FILE_PATH")
    SIZE=$(stat -c%s "$FILE_PATH")
    ROW_COUNT=$(wc -l < "$FILE_PATH")
    MD5=$(md5sum "$FILE_PATH" | awk '{print $1}')
    if [ "$FIRST_ENTRY" = true ]; then
        FIRST_ENTRY=false
    fi
    echo "$FILE_NAME|$SIZE|$ROW_COUNT|$MD5" >> "$OUTPUT_FILE"
    echo "处理文件: $FILE_NAME" >> "$LOG_FILE"
    echo "  大小: $SIZE 字节" >> "$LOG_FILE"
    echo "  行数: $ROW_COUNT" >> "$LOG_FILE"
    echo "  MD5: $MD5" >> "$LOG_FILE"
done

if [ "$FIRST_ENTRY" = true ]; then
    echo "文件夹中没有找到 CSV 文件，未生成有效检查文件。" >> "$LOG_FILE"
    echo "{}" > "$OUTPUT_FILE"
    exit 1
else
    echo "检查文件生成成功: $OUTPUT_FILE" >> "$LOG_FILE"
fi

# Step 2: 压缩目录中的文件，不保留目录结构
echo "Step 2: 压缩目录中的文件到文件 $TAR_FILE" >> "$LOG_FILE"
cd "$FOLDER_PATH" || { echo "无法切换到目录 $FOLDER_PATH"; exit 1; }
if tar -czf "$TAR_FILE" * >> "$LOG_FILE" 2>&1; then
    echo "压缩成功: $TAR_FILE" >> "$LOG_FILE"
else
    echo "压缩失败: $SOURCE_DIR" >> "$LOG_FILE"
    exit 1
fi

# Step 3: 加密文件
echo "Step 3: 加密文件 $TAR_FILE" >> "$LOG_FILE"
if gpg --batch --yes --passphrase "$(cat /etc/gpg.password)" --cipher-algo AES256 --force-mdc -c "$TAR_FILE"; then
    echo "加密成功: ${TAR_FILE}.gpg" >> "$LOG_FILE"
else
    echo "加密失败: $TAR_FILE" >> "$LOG_FILE"
    exit 1
fi

# 添加结束标志
echo "====== 脚本运行结束 - $(date) ======" >> "$LOG_FILE"

# 控制台提示完成
echo "脚本执行完成，详细日志请查看: $LOG_FILE"

# Step 4: 传输加密文件
echo "Step 4: 传输加密文件 ${TAR_FILE}.gpg" >> "$LOG_FILE"
if rsync -avz --no-group "${TAR_FILE}.gpg" etluser@172.20.33.2::datashare/${YESTERDAY}/ --password-file=/etc/rsync.password; then
    echo "传输成功：${TAR_FILE}.gpg" >> "$LOG_FILE"
else
    echo "传输失败：${TAR_FILE}.gpg" >> "$LOG_FILE"
    exit 1
fi
