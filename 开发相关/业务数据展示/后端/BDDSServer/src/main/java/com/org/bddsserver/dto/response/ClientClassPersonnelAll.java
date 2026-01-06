package com.org.bddsserver.dto.response;

import com.org.bddsserver.pojo.ClientClassInfo;
import com.org.bddsserver.pojo.personnel;
import lombok.Data;

import java.util.List;

/**
 * @author PY.Lu
 * @date 2025/11/7
 * @Description
 */
@Data
public class ClientClassPersonnelAll {
    private List<ClientClassInfo> clientClassInfoList;
    private List<personnel> personnelList;
}
