package com.thqh.enterprise_wechat_backend.mapper;

import com.thqh.enterprise_wechat_backend.entity.GroupChat;
import com.thqh.enterprise_wechat_backend.entity.GroupChatMember;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface GroupChatMapper {

    /**
     * 先将所有群聊置为失效状态
     * @return
     */
    int markAllGroupChatsInactive();

    /**
     * 批量合并群聊主表数据：
     * 如果 chat_id 存在则更新，否则插入
     */
    int batchMergeGroupChats(List<GroupChat> groupChats);


    /**
     * 先将群聊成员置为失效状态
     * @return
     */
    int markAllGroupChatMembersInactive();

    /**
     * 批量合并群成员数据：
     * 根据联合唯一键 (chat_id, userid) 判断记录是否存在，
     * 存在则更新，不存在则插入
     */
    int batchMergeGroupChatMembers(List<GroupChatMember> members);


    /**
     * 批量处理拉取失败的群聊
     * 如果 chat_id 存在则更新，否则插入
     */
    int batchMergeErrorGroupChats(List<GroupChat> groupChats);

    /**
     * 分页获取群聊列表
     * @param name
     * @return
     */
    List<GroupChat> getGroupChatsByPage(@Param("name") String name);


}
