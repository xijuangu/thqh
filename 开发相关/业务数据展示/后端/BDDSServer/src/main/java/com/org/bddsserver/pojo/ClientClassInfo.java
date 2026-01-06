package com.org.bddsserver.pojo;

import lombok.Data;

/**
 * @author PY.Lu
 * @date 2025/10/28
 * @Description 客户分类信息POJO，承载客户类、子类及关联业务人员的基础数据
 */
@Data
public class ClientClassInfo {
    Integer id; // 主键
    Integer class_code; // 客户类代码
    String class_name; // 客户类名称
    String sub_code; // 子类代码
    String sub_name; // 子类名称
    String personnel_code; // 关联业务人员代码
    String personnel_name; // 关联业务人员名称
    String remark; // 备注

}
