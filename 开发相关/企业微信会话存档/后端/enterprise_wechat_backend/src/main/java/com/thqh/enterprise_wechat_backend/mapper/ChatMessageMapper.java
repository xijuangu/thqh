package com.thqh.enterprise_wechat_backend.mapper;


import com.thqh.enterprise_wechat_backend.dto.ConversationDetailQueryDTO;
import com.thqh.enterprise_wechat_backend.entity.*;
import org.apache.ibatis.annotations.*;
import java.util.List;

/**
 * @ClassName: ChatDataMapper
 * @Description:
 * @Author liubin
 * @Date 2025/3/5 17:21
 * @Version V1.0
 */

@Mapper
public interface ChatMessageMapper {


    @Insert("INSERT INTO chat_message(" +
            " msgid, action, from_user, tolist, roomid, msgtime, msgtype, chat_msg" +
            ") VALUES (" +
            " #{msgid}, #{action}, #{fromUser}, #{toList}, #{roomid}, #{msgTime}, #{msgType}, #{chatMsg}" +
            ")")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(ChatMessage chatMessage);


    @Select("SELECT DISTINCT roomid FROM chat_message WHERE roomid IS NOT NULL")
    List<String> getDistinctRoomIds();

    @Select("SELECT * FROM chat_message WHERE msgid = #{preMsgid}")
    ChatMessage selectByPreMsgid(String preMsgid);


    /**
     * 获取所有ChatMessage记录
     * @return
     */
    List<ChatMessage> getAllChatMessages();

    /**
     * 查询员工类型对话列表
     * @param id 用户或客户id
     * @return
     */
    List<WechatUser> findEmployeeConversations(@Param("id")String id,@Param("searchName")String searchName);
    List<WechatUser> findEmployeeConversations_hasConversation(@Param("id")String id,@Param("searchName")String searchName);

    /**
     * 查询客户类型对话列表
     * @param id 用户或客户id
     * @return
     */
    List<Customer> findCustomerConversations(@Param("id")String id,@Param("searchName")String searchName);
    List<Customer> findCustomerConversations_hasConversation(@Param("id")String id,@Param("searchName")String searchName);


    /**
     * 查询群聊类型对话列表
     * @param id 用户或客户id
     * @return
     */
    List<GroupChat> findGroupChatConversations(@Param("id")String id,@Param("searchName")String searchName);



    /**
     * 查询指定群聊的全部消息（用于 type = groupchat）
//     * @param roomId 群聊ID
     * @return 消息列表
     */
    List<ChatMessage> selectByGroupChatId(@Param("roomId") String roomId, @Param("searchName") String searchName,@Param("filter_msgtype")String filter_msgtype);

    /**
     * 查询指定两人之间的非群聊消息（用于 type ！= groupchat）
//     * @param fromUser 发送方ID
//     * @param toUser 接收方ID
//     * @param searchName 搜索关键字
     * @return 消息列表
     */
    List<ChatMessage> selectByParticipants(@Param("fromUser") String fromUser, @Param("toUser") String toUser, @Param("searchName") String searchName,@Param("filter_msgtype")String filter_msgtype);



}
