package com.org.bddsserver.service;

import com.org.bddsserver.dto.request.roleUpdateRequest;
import com.org.bddsserver.dto.response.roleListResponse;
import com.org.bddsserver.pojo.role;

/**
 * @author PY.Lu
 * @date 2025/11/5
 * @Description
 */
public interface roleService {

    roleListResponse getAllInfo();

    Boolean addNewRole(role role);

    String updateRole(roleUpdateRequest roleUpdateRequest);

    String deleteRole(role role);
}
