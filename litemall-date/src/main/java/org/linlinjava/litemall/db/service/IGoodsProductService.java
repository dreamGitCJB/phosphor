package org.linlinjava.litemall.db.service;

import org.linlinjava.litemall.db.entity.GoodsProduct;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 商品货品表 服务类
 * </p>
 *
 * @author chenjinbao
 * @since 2020-05-03
 */
public interface IGoodsProductService extends IService<GoodsProduct> {
    int reduceStock(Integer id, Integer num);

    boolean addStock(Integer id, Integer num);

    List<GoodsProduct> queryByGid(Integer goodsId);
}
