package org.linlinjava.litemall.db.service;

import org.linlinjava.litemall.db.entity.Role;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Set;

/**
 * <p>
 * 角色表 服务类
 * </p>
 *
 * @author chenjinbao
 * @since 2020-05-03
 */
public interface IRoleService extends IService<Role> {

    Set<String> queryByIds(Integer[] roleIds);

}
