package org.linlinjava.litemall.db.service.impl;

import org.linlinjava.litemall.db.entity.System;
import org.linlinjava.litemall.db.mapper.SystemMapper;
import org.linlinjava.litemall.db.service.ISystemService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 系统配置表 服务实现类
 * </p>
 *
 * @author chenjinbao
 * @since 2020-05-03
 */
@Service
public class SystemServiceImpl extends ServiceImpl<SystemMapper, System> implements ISystemService {

}
