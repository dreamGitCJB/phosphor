package org.linlinjava.litemall.db.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.linlinjava.litemall.db.common.constans.GrouponConstant;
import org.linlinjava.litemall.db.entity.Groupon;
import org.linlinjava.litemall.db.mapper.GrouponMapper;
import org.linlinjava.litemall.db.service.IGrouponService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 团购活动表 服务实现类
 * </p>
 *
 * @author chenjinbao
 * @since 2020-05-03
 */
@Service
public class GrouponServiceImpl extends ServiceImpl<GrouponMapper, Groupon> implements IGrouponService {
    /**
     * 获取某个团购活动参与的记录
     *
     * @param grouponId
     * @return
     */
    @Override
    public List<Groupon> queryJoinRecord(Integer grouponId) {
        List<Groupon> list = this.list(new LambdaQueryWrapper<Groupon>().eq(Groupon::getGrouponId, grouponId).ne(Groupon::getStatus, GrouponConstant.STATUS_NONE).orderByDesc(Groupon::getAddTime));
        return list;
    }

}