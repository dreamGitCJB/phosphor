package org.linlinjava.litemall.db.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.linlinjava.litemall.db.entity.Collect;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 收藏表 服务类
 * </p>
 *
 * @author chenjinbao
 * @since 2020-05-03
 */
public interface ICollectService extends IService<Collect> {

    IPage<Collect> queryByType(Integer userId,Integer type,int page, int limit,String sort,String order);

    Collect queryByTypeAndValue(Integer userId,Integer type,Integer valueId);
}
