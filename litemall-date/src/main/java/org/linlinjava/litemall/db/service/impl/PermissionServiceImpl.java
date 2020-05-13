package org.linlinjava.litemall.db.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.linlinjava.litemall.db.entity.Permission;
import org.linlinjava.litemall.db.mapper.PermissionMapper;
import org.linlinjava.litemall.db.service.IPermissionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 * 权限表 服务实现类
 * </p>
 *
 * @author chenjinbao
 * @since 2020-05-03
 */
@Service
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, Permission> implements IPermissionService {
    @Override
    public Set<String> queryByRoleIds(Integer[] roleIds) {
        if(roleIds.length == 0){
            return new HashSet<>();
        }

        List<Permission> list = this.list(new LambdaQueryWrapper<Permission>().in(Permission::getRoleId, Arrays.asList(roleIds)));

        return  list.stream().map(Permission::getPermission).collect(Collectors.toSet());
    }
}
