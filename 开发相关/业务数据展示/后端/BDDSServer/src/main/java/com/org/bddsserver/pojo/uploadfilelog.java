package com.org.bddsserver.pojo;

import lombok.Data;

/**
 * @author PY.Lu
 * @date 2025/10/27
 * @Description
 */
@Data
public class uploadfilelog {
    Integer id;
    String fileName;
    String fileType;
    Double fileSize;
    Integer persistenceState;
    String uploadTime;
    String sysUserCode;
}
