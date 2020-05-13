package org.linlinjava.litemall.db.mapper;

import org.apache.ibatis.annotations.Param;
import org.linlinjava.litemall.db.entity.GoodsProduct;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 商品货品表 Mapper 接口
 * </p>
 *
 * @author chenjinbao
 * @since 2020-05-03
 */
public interface GoodsProductMapper extends BaseMapper<GoodsProduct> {

    int addStock(@Param("id") Integer id, @Param("num") Integer num);
    int reduceStock(@Param("id") Integer id, @Param("num") Integer num);
}
