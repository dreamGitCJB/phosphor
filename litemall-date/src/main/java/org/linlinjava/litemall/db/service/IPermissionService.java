package org.linlinjava.litemall.db.service;

import org.linlinjava.litemall.db.entity.Permission;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Set;

/**
 * <p>
 * 权限表 服务类
 * </p>
 *
 * @author chenjinbao
 * @since 2020-05-03
 */
public interface IPermissionService extends IService<Permission> {
    Set<String> queryByRoleIds(Integer[] roleIds);

}
