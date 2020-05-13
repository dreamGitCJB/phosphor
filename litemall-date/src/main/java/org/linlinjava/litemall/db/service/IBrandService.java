package org.linlinjava.litemall.db.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.linlinjava.litemall.db.entity.Brand;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 品牌商表 服务类
 * </p>
 *
 * @author chenjinbao
 * @since 2020-05-03
 */
public interface IBrandService extends IService<Brand> {

    IPage<Brand> query(Integer page,Integer limit,String sort,String order);
}
