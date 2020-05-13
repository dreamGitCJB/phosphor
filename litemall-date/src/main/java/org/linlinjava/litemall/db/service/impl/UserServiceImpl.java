package org.linlinjava.litemall.db.service.impl;

import org.linlinjava.litemall.db.vo.UserVo;
import org.linlinjava.litemall.db.entity.User;
import org.linlinjava.litemall.db.mapper.UserMapper;
import org.linlinjava.litemall.db.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author chenjinbao
 * @since 2020-05-03
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {
    @Override
    public UserVo findUserVoById(Integer userId) {
        User user = this.getById(userId);
        UserVo userVo = new UserVo();
        userVo.setNickname(user.getNickname());
        userVo.setAvatar(user.getAvatar());
        return userVo;
    }

    @Override
    public List<Map> statUser() {
        return null;
    }

    @Override
    public List<Map> statOrder() {
        return null;
    }

    @Override
    public List<Map> statGoods() {
        return null;
    }
}
