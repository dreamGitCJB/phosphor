package org.linlinjava.litemall.db.mapper;

import org.linlinjava.litemall.db.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 用户表 Mapper 接口
 * </p>
 *
 * @author chenjinbao
 * @since 2020-05-03
 */
public interface UserMapper extends BaseMapper<User> {
    List<Map> statUser();

    List<Map> statOrder();

    List<Map> statGoods();
}
