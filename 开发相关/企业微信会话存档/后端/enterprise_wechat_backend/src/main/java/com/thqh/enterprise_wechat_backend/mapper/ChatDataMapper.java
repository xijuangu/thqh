package com.thqh.enterprise_wechat_backend.mapper;

import com.thqh.enterprise_wechat_backend.entity.ChatData;
import com.thqh.enterprise_wechat_backend.entity.RawChatData;
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
public interface ChatDataMapper {

    @Insert({
            "<script>",
            "INSERT INTO raw_chat_data (seq, msgid, publickey_ver, encrypt_random_key, encrypt_chat_msg) VALUES ",
            "<foreach collection='rawChatDatas' item='rawChatData' separator=','>",
            "(#{rawChatData.seq}, #{rawChatData.msgid}, #{rawChatData.publickeyVer}, #{rawChatData.encryptRandomKey}, #{rawChatData.encryptChatMsg})",
            "</foreach>",
            "</script>"
    })
    void batchInsert(@Param("rawChatDatas") List<RawChatData> rawChatDatas);

    @Insert("INSERT INTO chat_data (seq, msgid,publickey_ver,encrypt_random_key,encrypt_chat_msg) " +
            "SELECT distinct rcd.seq, rcd.msgid,rcd.publickey_ver,rcd.encrypt_random_key, rcd.encrypt_chat_msg " +
            "FROM raw_chat_data rcd " +
            "WHERE NOT EXISTS (" +
            "SELECT 1 FROM chat_data cd WHERE cd.msgid = rcd.msgid)")
    void insertRawChatDataIntoChatData();

    /**
     * 通过是否解密字段获取chatdata数据
     * @param decryptFlag
     * @return
     */
    List<ChatData> selectByDecryptFlag(@Param("decryptFlag") int decryptFlag);


    /**
     * 通过是否生成消息字段获取chatdata数据
     * @param generateMsgFlag
     * @return
     */
    List<ChatData> selectByGenerateMsgFlag(@Param("generateMsgFlag") int generateMsgFlag);

    /**
     * 获取所有未生成chatMessage的chatdata数据
     * @return
     */
    List<ChatData> selectNoGeneratedMsg();


    // 新增：更新ChatData记录
    @Update("UPDATE chat_data SET chat_msg = #{chatMsg}, decrypt_flag = #{decryptFlag} ,updated_at = #{updatedAt} WHERE id = #{id}")
    void updateChatData(ChatData chatData);


    /**
     * 更新 生产会话消息标记
     * @param chatData
     */
    @Update("UPDATE chat_data SET generate_msg_flag = #{generateMsgFlag},error_log = #{errorLog},updated_at = #{updatedAt} WHERE id = #{id}")
    void updateGenerateMsgFlag(ChatData chatData);

    // 新增：查询最大seq值
    @Select("SELECT COALESCE(MAX(seq), 0) FROM chat_data")
    long getMaxSeqFromChatData();

}
