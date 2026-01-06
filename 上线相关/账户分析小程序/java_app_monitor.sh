#!/bin/bash

# 配置区（根据实际情况修改）
# datax服务占用端口
PORT1=9527
PORT2=9999

# 加密程序占用端口
PORT3=8083
# 小程序后端程序占用端口
PORT4=8080

LOG_DIR="/usr/local/my/log/java_app_monitor"    # 日志存放目录
MAX_RETRY=1              # 最大重试次数

# 创建日志目录（如果不存在）
mkdir -p "$LOG_DIR"

# 定义当日日志文件路径
LOG_FILE="$LOG_DIR/app_monitor_$(date +%Y%m%d).log"

CALLBACK_DATAX="/usr/local/datax/data/applogs/executor/jobhandler"

# 日志记录函数
log() {
    local timestamp=$(date "+%Y-%m-%d %H:%M:%S")
    echo "[$timestamp] $1" >> "$LOG_FILE"
}

# 检查端口是否正在被非 Java 程序监听
is_port_occupied_by_non_java() {
    local port=$1
    # 使用 netstat 命令查找监听指定端口的进程信息
    local process_info=$(netstat -tulnp 2>/dev/null | grep ":$port " | awk '{print $7}' | sed 's/\/.*//')
    if [ -n "$process_info" ]; then
        # 提取进程 ID
        local pid=$(echo "$process_info" | awk -F '/' '{print $1}')
        # 根据进程 ID 获取进程的命令名
        local cmd=$(ps -p $pid -o comm=)
        if [ "$cmd" != "java" ]; then
            # 若命令名不是 java，则认为端口被非 Java 程序占用
            return 0
        fi
    fi
    # 端口未被占用或被 Java 程序占用
    return 1
}

# 检查端口是否正在监听
is_app_running() {
    local port=$1
    # 使用 grep -q 避免输出匹配信息，仅根据返回状态判断
    if netstat -tulnp 2>/dev/null | grep -q ":$port "; then
        return 0  # 端口存在，返回成功
    else
        return 1  # 端口不存在，返回失败
    fi
}

# 检查/usr/local/datax/data...下是否有callback文件夹，有的话删掉
check_and_delete_folder() {

    local path="/usr/local/datax/data/applogs/executor/jobhandler"
    local folder_name="callback"
    local target="${path%/}/${folder_name}"

    # 检查目标是否存在并且是一个目录
    if [ -d "$target" ]; then
	log "找到文件夹：$target"
	log "正在删除...."
	rm -irf "$target"
	if [$? -eq 0]; then
		log "成功删除文件夹：$target"
		return 0
	fi
    else
	log "文件夹不存在：$target"
	return 0
    fi
}
    


# 尝试启动应用程序
start_application() {
    local port=$1
    local app_num=$2
    local datax_admin="datax-admin start"
    local datax_executor="datax-executor start"
    local JAVA_APP_CMD1="java -jar /usr/local/my/ProcessServer-0.0.1-SNAPSHOT.jar"
    local JAVA_APP_CMD2="java -jar /usr/local/wxServer/wechat_miniprogram_backend-0.0.1-SNAPSHOT.jar"
    local retry_count=0
    
    if check_and_delete_folder ; then
	log "datax的callback已删除或不存在。"
    else
	echo "datax的callback文件夹删除失败，后续脚本终止执行"
	return 1
    fi


    while [ $retry_count -lt $MAX_RETRY ]; do
        ((retry_count++))
        log "第 $retry_count 次启动尝试（端口：$port）..."

        if is_port_occupied_by_non_java $port; then
            log "无法启动应用程序（端口：$port），端口被非 Java 程序占用"
            return 1
        fi

        log "尝试启动应用程序（端口：$port）..."


        log "执行命令：eval $JAVA_APP_CMD1 "
	if [  "$app_num" -eq 1 ]; then
	     ($datax_admin >> $LOG_FILE 2>&1) &
        elif [  "$app_num" -eq 2 ]; then
             ($datax_executor >> $LOG_FILE 2>&1) &

        elif [  "$app_num" -eq 3 ]; then 
             ($JAVA_APP_CMD1 >> $LOG_FILE 2>&1) &
	elif [ "$app_num" -eq 4 ]; then
             ($JAVA_APP_CMD2 >> $LOG_FILE 2>&1) &
	else
	     log "app_num 参数无效，必须是1、2、3、4"
	     return 1
	fi

        # 等待10秒让程序初始化
        sleep 10

        if is_app_running $port; then
            log "应用程序（端口：$port）启动成功，共尝试 $retry_count 次"
            return 0
        else
            log "应用程序（端口：$port）第 $retry_count 次启动失败"
            if [ $retry_count -lt $MAX_RETRY ]; then
                sleep 5  # 等待5秒后重试
            fi
        fi
    done

    log "错误：经过 $MAX_RETRY 次尝试仍无法启动应用程序（端口：$port）！"
    return 1
}

# 主监控逻辑
monitor_app() {
    local port=$1
    local app_num=$2
    log "========== 开始检查端口 $port 应用程序 =========="

    if is_app_running $port; then
        log "检测到应用程序正在运行（端口：$port）"
        log "检查完成"
        return 0
    fi

    log "应用程序（端口：$port）未运行，开始启动流程..."
    if start_application $port $app_num; then
        return 0
    else
        return 1
    fi
}

# 执行监控
monitor_app $PORT1 1
monitor_app $PORT2 2
monitor_app $PORT3 3
monitor_app $PORT4 4  
