package com.org.bddsserver.service.impl;

import com.org.bddsserver.dto.UploadFileData;
import com.org.bddsserver.exception.BusinessException;
import com.org.bddsserver.mapper.uploadFileMapper;
import com.org.bddsserver.service.uploadFileService;
import com.org.bddsserver.utils.FileParserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author PY.Lu
 * @date 2025/10/27
 * @Description
 */
@Service
@Transactional(rollbackFor = {BusinessException.class, SQLException.class, Exception.class})
public class uploadFileServiceImpl implements uploadFileService {
    @Autowired
    private FileParserUtil fileParserUtil;
    @Autowired
    private uploadFileMapper fileMapper;

    @Override
    public Integer uploadFilePersistence(MultipartFile file) throws IOException {
        List<UploadFileData> fileDataList = fileParserUtil.parseFile(file);
        Integer truncate_result = fileMapper.truncateFileData();
        Integer result = fileMapper.insertFileDataList(fileDataList);
//        fileMapper.update_upload_log();
        return result;

    }
}
