package org.linlinjava.litemall.db.service.impl;

import org.linlinjava.litemall.db.entity.Ad;
import org.linlinjava.litemall.db.mapper.AdMapper;
import org.linlinjava.litemall.db.service.IAdService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 广告表 服务实现类
 * </p>
 *
 * @author chenjinbao
 * @since 2020-05-03
 */
@Service
public class AdServiceImpl extends ServiceImpl<AdMapper, Ad> implements IAdService {

}
