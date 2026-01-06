package com.thqh.enterprise_wechat_backend.dto;

import lombok.Data;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: WeChatUserDetailResponse
 * @Description:
 * @Author liubin
 * @Date 2025/3/20 15:58
 * @Version V1.0
 */
@Data
public class WeChatUserDetailResponse {
    private int errcode;
    private String errmsg;
    private String userid;
    private String name;
    private List<Integer> department;
    private List<Integer> order;
    private String position;
    private String mobile;
    private String gender;
    private String email;
    private String biz_mail;
    private List<Integer> is_leader_in_dept;
    private List<String> direct_leader;
    private String avatar;
    private String thumb_avatar;
    private String telephone;
    private String alias;
    private String address;
    private String open_userid;
    private int main_department;
    private Map<String, Object> extattr;
    private int status;
    private String qr_code;
    private String external_position;
    private Map<String, Object> external_profile;
}
