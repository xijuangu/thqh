package com.org.bddsserver.service.impl;

import com.org.bddsserver.dto.response.ClientClassPersonnelAll;
import com.org.bddsserver.mapper.ClientClassMapper;
import com.org.bddsserver.mapper.PersonnelMapper;
import com.org.bddsserver.pojo.ClientClassInfo;
import com.org.bddsserver.pojo.personnel;
import com.org.bddsserver.service.ClientClassService;
import com.org.bddsserver.service.PersonnelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author PY.Lu
 * @date 2025/10/28
 * @Description
 */
@Service
public class ClientClassServiceImpl implements ClientClassService {
    @Autowired
    private ClientClassMapper clientClassMapper;

    @Autowired
    private PersonnelMapper personnelMapper;

    @Override
    public ClientClassPersonnelAll getAllClientClassInfo() {
        ClientClassPersonnelAll clientClassPersonnelAll = new ClientClassPersonnelAll();
        List<ClientClassInfo> clientClassInfoList = clientClassMapper.selectAll();

        clientClassPersonnelAll.setClientClassInfoList(clientClassInfoList);
        List<personnel> personnelList = personnelMapper.selectAllPersonnel();
        clientClassPersonnelAll.setPersonnelList(personnelList);
        return clientClassPersonnelAll;
    }

    @Override
    public Integer addClientClassInfo(ClientClassInfo clientClassInfo) {
        return clientClassMapper.insertOne(clientClassInfo);
    }

    @Override
    public Integer updateClientClassInfo(ClientClassInfo clientClassInfo) {
        return clientClassMapper.updateById(clientClassInfo);
    }

    @Override
    public Integer deleteClientClassInfo(Integer id) {
        return clientClassMapper.deleteById(id);
    }
}
