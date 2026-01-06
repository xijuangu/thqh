package com.org.bddsserver.service;

import com.org.bddsserver.dto.response.ClientClassPersonnelAll;
import com.org.bddsserver.pojo.ClientClassInfo;

import java.util.List;

/**
 * @author PY.Lu
 * @date 2025/10/28
 * @Description
 */
public interface ClientClassService {
    ClientClassPersonnelAll getAllClientClassInfo();

    Integer addClientClassInfo(ClientClassInfo clientClassInfo);

    Integer updateClientClassInfo(ClientClassInfo clientClassInfo);

    Integer deleteClientClassInfo(Integer id);
}
