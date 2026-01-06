package com.org.bddsserver.mapper;

import com.org.bddsserver.dto.PersonnelInfo;
import com.org.bddsserver.pojo.ClientClassInfo;
import com.org.bddsserver.pojo.personnel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;
import java.util.List;

/**
 * @author PY.Lu
 * @date 2025/10/16
 * @Description
 */
@Mapper
public interface PersonnelMapper {
    List<PersonnelInfo> getAllPersonnelDetail();

    List<PersonnelInfo> getPersonnelDetailBySysUserId(@Param("sysUserId") Integer sysUserId);

    /**
     * TODO:基于业务人员编号查找其下所有所属客户类信息
     * @Param: String personnel_code 业务人员编码
     * @Return: List<ClientClassInfo> 所属客户类信息列表
     */
    List<ClientClassInfo> queryClientCodeByPersonnelCode(@Param("personnelCode") String personnelCode);

    /**
     * TODO:基于客户类列表、客户类型查找对应客户号列表(统计周期结束时间前开户且在这个结束时间前未销户的客户)
     * @param clinetCodeList
     * @param clientType
     * @param endDate
     * @return
     */
    String[] queryClientIdListByClinetCodeList_active(@Param("clinetCodeList") List<ClientClassInfo> clinetCodeList,@Param("client_type")String clientType,@Param("endDate")String endDate);

    /**
     * TODO:基于客户类列表、客户类型查找对应客户号列表(不刨除销户)
     * @param clinetCodeList
     * @return
     */
    String[] queryClientIdListByClinetCodeList_all(@Param("clinetCodeList") List<ClientClassInfo> clinetCodeList,@Param("client_type")String clientType);

    List<PersonnelInfo> getBusinessPersonInfo();

    Integer updateBusinessPersonInfo(@Param("personnelInfo") PersonnelInfo personnelInfo);

    Integer deleteBusinessPersonInfo(Integer id);

    PersonnelInfo personnelName_dupilcateCheck(@Param("personnelInfo") PersonnelInfo personnelInfo);
    PersonnelInfo personnelCode_dupilcateCheck(@Param("personnelInfo") PersonnelInfo personnelInfo);

    Integer insertBusinessPersonInfo(@Param("personnelInfo") PersonnelInfo personnelInfo);

    List<personnel> selectAllPersonnel();
}
