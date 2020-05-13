package org.linlinjava.litemall.wx.service;

import org.linlinjava.litemall.db.entity.User;
import org.linlinjava.litemall.db.service.IUserService;
import org.linlinjava.litemall.wx.dto.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Service
public class UserInfoService {
    @Autowired
    private IUserService userService;


    public UserInfo getInfo(Integer userId) {
        User user = userService.getById(userId);
        Assert.state(user != null, "用户不存在");
        UserInfo userInfo = new UserInfo();
        userInfo.setNickName(user.getNickname());
        userInfo.setAvatarUrl(user.getAvatar());
        return userInfo;
    }
}
