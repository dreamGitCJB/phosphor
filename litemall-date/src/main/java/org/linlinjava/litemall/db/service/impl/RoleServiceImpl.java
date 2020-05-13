package org.linlinjava.litemall.db.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.linlinjava.litemall.db.entity.Role;
import org.linlinjava.litemall.db.mapper.RoleMapper;
import org.linlinjava.litemall.db.service.IRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * <p>
 * 角色表 服务实现类
 * </p>
 *
 * @author chenjinbao
 * @since 2020-05-03
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements IRoleService {

    @Override
    public Set<String> queryByIds(Integer[] roleIds) {
        Set<String> roles = new HashSet<String>();
        if(roleIds.length == 0){
            return roles;
        }

        List<Role> list = this.list(new LambdaQueryWrapper<Role>().eq(Role::getEnabled, true).in(Role::getId, Arrays.asList(roleIds)));


        return list.stream().map(Role::getName).collect(Collectors.toSet());

    }
}
