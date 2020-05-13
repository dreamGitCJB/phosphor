package org.linlinjava.litemall.db.service;

import org.linlinjava.litemall.db.vo.UserVo;
import org.linlinjava.litemall.db.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author chenjinbao
 * @since 2020-05-03
 */
public interface IUserService extends IService<User> {
    UserVo findUserVoById(Integer userId);

    List<Map> statUser();

    List<Map> statOrder();

    List<Map> statGoods();
}
