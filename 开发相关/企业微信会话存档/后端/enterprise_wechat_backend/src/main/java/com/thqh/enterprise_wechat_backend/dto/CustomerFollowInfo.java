package com.thqh.enterprise_wechat_backend.dto;

import lombok.Data;
import java.util.List;

/**
 * @ClassName: CustomerFollowInfo
 * @Description:
 * @Author liubin
 * @Date 2025/3/24 15:22
 * @Version V1.0
 */
@Data
public class CustomerFollowInfo {
    //添加了此外部联系人的企业成员userid
    private String userid;
    //该成员对此外部联系人的备注
    private String remark;
    //该成员对此外部联系人的描述
    private String description;
    //该成员添加此外部联系人的时间
    private long createtime;
    // 该成员添加此外部联系人所打企业标签的id，用户自定义类型标签（type=2）不返回
    private List<String> tag_id;
    // 该成员对此客户备注的手机号码
    private List<String> remark_mobiles;
    //该成员添加此客户的来源
    private int add_way;
    //发起添加的userid，如果成员主动添加，为成员的userid；如果是客户主动添加，则为客户的外部联系人userid；如果是内部成员共享/管理员分配，则为对应的成员/管理员userid
    private String oper_userid;
}
