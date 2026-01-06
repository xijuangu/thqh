package com.org.bddsserver.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @author PY.Lu
 * @date 2025/10/27
 * @Description
 */
public interface uploadFileService {
    public Integer uploadFilePersistence (MultipartFile file) throws IOException;
}
