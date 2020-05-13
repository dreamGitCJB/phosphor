package org.linlinjava.litemall.db.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.linlinjava.litemall.db.entity.Goods;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 商品基本信息表 服务类
 * </p>
 *
 * @author chenjinbao
 * @since 2020-05-03
 */
public interface IGoodsService extends IService<Goods> {
    IPage<Goods> querySelective(Integer catId, Integer brandId, String keywords, Boolean isHot, Boolean isNew, Integer offset, Integer limit, String sort, String order);

    IPage<Goods> querySelective(Integer goodsId, String goodsSn, String name, Integer page, Integer size, String sort, String order);


    /**
     * 获取分类下的商品
     *
     * @param catList
     * @param offset
     * @param limit
     * @return
     */
    IPage<Goods> queryByCategory(List<Integer> catList, int offset, int limit);


    /**
     * 获取分类下的商品
     *
     * @param catId
     * @param offset
     * @param limit
     * @return
     */
    IPage<Goods> queryByCategory(Integer catId, int offset, int limit);
}
