package org.linlinjava.litemall.db.service;

import org.linlinjava.litemall.db.entity.GoodsSpecification;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 商品规格表 服务类
 * </p>
 *
 * @author chenjinbao
 * @since 2020-05-03
 */
public interface IGoodsSpecificationService extends IService<GoodsSpecification> {
    Object getSpecificationVoList(Integer id);
}
