package com.org.bddsserver.dto.response;

import com.org.bddsserver.dto.PersonnelInfo;
import com.org.bddsserver.pojo.sysfunctions;
import lombok.Data;

import java.util.List;

/**
 * @author PY.Lu
 * @date 2025/11/5
 * @Description
 */
@Data
public class roleListResponse {
    private List<com.org.bddsserver.dto.roleList> roleList; //包含已创建角色信息：角色id，角色名称，角色编码，角色已分配的系统功能权限列表、已分配的数据查看权限列表
    private List<sysfunctions> sysfunctionList; // 可授权的所有系统功能权限列表
    private List<PersonnelInfo> dataViewList; // 可授权的所有数据查看权限列表
}
