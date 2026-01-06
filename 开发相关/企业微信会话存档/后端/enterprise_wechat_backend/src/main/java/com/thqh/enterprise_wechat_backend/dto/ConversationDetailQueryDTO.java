package com.thqh.enterprise_wechat_backend.dto;

import lombok.Data;

/**
 * @ClassName: ConversationDetailQueryDTO
 * @Description:
 * @Author liubin
 * @Date 2025/4/ 16:04
 * @Version V1.0
 */
@Data
public class ConversationDetailQueryDTO {
    /**
     * 查询类型：groupchat（群聊）或 非群聊
     */
    private String type;

    /**
     * 会话发起方：
     * 如果 type = groupchat，为群聊ID；
     * 如果 type 为非群聊时，为发送方ID（员工或客户）
     */
    private String from;

    /**
     * 会话接收方：
     * 如果 type = groupchat，为 null；
     * 如果 type 为非群聊时，为接收方ID
     */
    private String to;

    /**
     * 搜索关键字，可选
     */
    private String searchName;

    /**
     * 当前页码，默认为1
     */
    private int page = 1;

    /**
     * 每页记录数，默认为20
     */
    private int limit = 20;

    /**
     * 快捷筛选条件:
     * filter_msgtype=null：拉取所有类型消息；
     * filter_msgtype=file：拉取文件；类型消息；
     * filter_msgtype=picture：拉取图片类型会话；
     * filter_msgtype=text：拉取语音类型会话；
     */
    private String filter_msgtype;

}