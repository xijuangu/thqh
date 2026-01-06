package com.thqh.enterprise_wechat_backend.mapper;

import com.thqh.enterprise_wechat_backend.entity.ChatMedia;
import org.apache.ibatis.annotations.*;
import java.util.List;

/**
 * @ClassName: ChatMediaMapper
 * @Description:
 * @Author liubin
 * @Date 2025/3/10 10:18
 * @Version V1.0
 */
@Mapper
public interface ChatMediaMapper {


    @Insert("INSERT INTO chat_media(" +
            " msgid, sdkfileid, mediatype,md5sum,media_size,play_length,media_name,width,height,type," +
            " created_at, updated_at" +
            ") VALUES (" +
            " #{msgid}, #{sdkfileid},#{mediaType}, #{md5Sum}, #{mediaSize},#{playLength},#{mediaName},#{width},#{height},#{type}, " +
            " NOW(), NOW()" +
            ")")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(ChatMedia media);

    @Select("SELECT * FROM chat_media WHERE download_status = #{downloadStatus}")
    List<ChatMedia> findByDownloadStatus(@Param("downloadStatus") int downloadStatus);


    @Select("SELECT * FROM chat_media WHERE download_status != 1 ")
    List<ChatMedia> selectUnDownloaddMedia();


    @Update("UPDATE chat_media " +
            "SET download_status = #{downloadStatus}, " +
            "    download_url = #{downloadUrl}, " +
            "    error_log = #{errorLog}, " +
            "    updated_at = NOW() " +
            "WHERE id = #{id}")
    int updateDownloadInfo(ChatMedia media);

    // 如果需要查某一条消息对应所有媒体
    @Select("SELECT * FROM chat_media WHERE msgid = #{msgid}")
    List<ChatMedia> findByChatMessageId(@Param("msgid") Long msgid);

    ChatMedia selectBySdkfileid(@Param("sdkfileid") String sdkfileid);
}
