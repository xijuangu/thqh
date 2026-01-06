package com.org.bddsserver.mapper;

import com.org.bddsserver.dto.UploadFileData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author PY.Lu
 * @date 2025/10/27
 * @Description
 */
@Mapper
public interface uploadFileMapper {
    public int insertFileDataList(@Param("uploadFileDataList")List<UploadFileData> uploadFileDataList);

    int truncateFileData();

//    int update_upload_log();
}
