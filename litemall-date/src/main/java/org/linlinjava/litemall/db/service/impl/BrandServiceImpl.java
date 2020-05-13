package org.linlinjava.litemall.db.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.linlinjava.litemall.db.common.constans.DbConstans;
import org.linlinjava.litemall.db.common.util.PageUtil;
import org.linlinjava.litemall.db.entity.Brand;
import org.linlinjava.litemall.db.service.IBrandService;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import org.linlinjava.litemall.db.mapper.BrandMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * <p>
 * 品牌商表 服务实现类
 * </p>
 *
 * @author chenjinbao
 * @since 2020-05-03
 */
@Service
public class BrandServiceImpl extends ServiceImpl<BrandMapper, Brand> implements IBrandService {
    @Override
    public IPage<Brand> query(Integer current, Integer limit, String sort, String order) {

        Page page = new Page(current, limit);

        PageUtil.pagetoPage(page,sort,order);


        IPage<Brand> brandIPage  = this.page(page);

        return brandIPage;
    }

}
