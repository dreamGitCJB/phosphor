package org.linlinjava.litemall.db.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.linlinjava.litemall.db.common.constans.DbConstans;
import org.linlinjava.litemall.db.common.util.PageUtil;
import org.linlinjava.litemall.db.entity.Collect;
import org.linlinjava.litemall.db.mapper.CollectMapper;
import org.linlinjava.litemall.db.service.ICollectService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * <p>
 * 收藏表 服务实现类
 * </p>
 *
 * @author chenjinbao
 * @since 2020-05-03
 */
@Service
public class CollectServiceImpl extends ServiceImpl<CollectMapper, Collect> implements ICollectService {
    @Override
    public IPage<Collect> queryByType(Integer userId, Integer type, int current, int limit, String sort, String order) {
        Page page = new Page(current, limit);
        PageUtil.pagetoPage(page,sort,order);

        IPage<Collect> collectIPage = this.page(page, new LambdaQueryWrapper<Collect>().eq(type != null, Collect::getType, type)
                .eq(Collect::getUserId, userId));

        return collectIPage;
    }

    @Override
    public Collect queryByTypeAndValue(Integer userId, Integer type, Integer valueId) {
        return this.getOne(new LambdaQueryWrapper<Collect>().eq(Collect::getUserId, userId)
                .eq(Collect::getType, type)
                .eq(Collect::getValueId, valueId));
    }
}
