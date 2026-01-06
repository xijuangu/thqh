package com.thqh.enterprise_wechat_backend.mapper;

import com.thqh.enterprise_wechat_backend.entity.Customer;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CustomerMapper {

    /**
     * 通过id查找客户
     * @param externalUserId 客户id
     * @return
     */
    Customer getCustomerByExternalUserId(String externalUserId);

    /**
     * 删除 raw_customer 表中创建日期为今天的数据
     */
    @Delete("DELETE FROM raw_customer WHERE DATE(created_at) = CURDATE()")
    void deleteTodayRawCustomers();


    /**
     * 批量插入临时客户数据到 raw_customer 表
     */
    int batchInsertRawCustomers(List<Customer> customers);


    /**
     * 将 raw_customer 表的数据合并到正式表 customer 中，
     * 如果 customer 中存在对应 external_userid 的记录则更新，否则插入。
     * 此方法的具体 SQL 定义在 XML 映射文件中
     * @return 受影响的记录数
     */
    int mergeRawToCustomer();


    /**
     * 将 raw_customer 表的数据合并到正式表 customer_follow 中，
     * 如果 customer_follow 中存在对应 external_userid&follow_userid 的记录则更新，否则插入。
     * 此方法的具体 SQL 定义在 XML 映射文件中
     * @return 受影响的记录数
     */
    int mergeRawToCustomerFollow();


    List<Customer> getCustomersByPage(@Param("name") String name);


    // 更新 customer 表的 status 为 1
    void updateCustomerStatusToInactive();

    // 更新 customer_follow 表的 status 为 1
    void updateCustomerFollowStatusToInactive();

    // 根据 from_user_id 查询 customer 表
    Customer selectByUserId(String fromUserId);

    @Select("SELECT COUNT(*) FROM group_chat_member WHERE chat_id = #{chatId}")
    int getgroupMenberCountBychatId(String chatId);
}