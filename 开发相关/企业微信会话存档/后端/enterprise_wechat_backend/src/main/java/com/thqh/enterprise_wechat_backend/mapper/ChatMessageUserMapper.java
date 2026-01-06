package com.thqh.enterprise_wechat_backend.mapper;

import com.thqh.enterprise_wechat_backend.entity.*;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

/**
 * @ClassName: ChatDataMapper
 * @Description:
 * @Author liubin
 * @Date 2025/3/5 17:21
 * @Version V1.0
 */

@Mapper
public interface ChatMessageUserMapper {


    /**
     * 批量插入记录到 chat_message_user 表
     */
    void batchInsertChatMessageUsers(List<ChatMessageUser> chatMessageUser);


}
