package com.thqh.enterprise_wechat_backend.mapper;

import com.thqh.enterprise_wechat_backend.entity.WechatUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * @author liubin
 */
@Mapper
public interface WechatUserMapper {
    /**
     * 查询数据库中已存在的wechatUser
     * @return wechatUser 列表
     */
    List<WechatUser> getAllWechatUsers();


    /**
     * 批量插入微信用户（新增数据）
     * @param users 新增的微信用户列表
     * @return 插入的记录数
     */
    int batchInsertWechatUsers(List<WechatUser> users);

    /**
     * 批量更新微信用户（修改数据）
     * @param users 修改的微信用户列表
     * @return 更新的记录数
     */
    int batchUpdateWechatUsers(List<WechatUser> users);


    /**
     * 通过用户状态获取相关用户
     * @param status
     * @return
     */
    List<WechatUser> getWechatUsersByStatus(@Param("status") int status);

    /**
     * 分页获取员工列表
     * @param name 员工姓名
     * @return
     */
    List<WechatUser> getEmployeesByPage(@Param("name") String name);

    /**
     * 根据用户id查询用户信息
     * @param fromUserId
     * @return
     */
    WechatUser selectByUserId(String fromUserId);
}
