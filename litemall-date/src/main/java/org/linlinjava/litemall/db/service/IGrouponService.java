package org.linlinjava.litemall.db.service;

import org.linlinjava.litemall.db.entity.Groupon;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 团购活动表 服务类
 * </p>
 *
 * @author chenjinbao
 * @since 2020-05-03
 */
public interface IGrouponService extends IService<Groupon> {
    /**
     * 获取某个团购活动参与的记录
     *
     * @param grouponId
     * @return
     */
    List<Groupon> queryJoinRecord(Integer grouponId);
}
