package org.linlinjava.litemall.db.service.impl;

import org.linlinjava.litemall.db.entity.Admin;
import org.linlinjava.litemall.db.mapper.AdminMapper;
import org.linlinjava.litemall.db.service.IAdminService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 管理员表 服务实现类
 * </p>
 *
 * @author chenjinbao
 * @since 2020-05-03
 */
@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements IAdminService {

}
