package org.linlinjava.litemall.wx.dto;

import io.swagger.models.auth.In;
import lombok.Data;

@Data
public class UserInfo {
    private String nickName;
    private String avatarUrl;
    private String country;
    private String province;
    private String city;
    private String language;
    private Integer gender;
}
