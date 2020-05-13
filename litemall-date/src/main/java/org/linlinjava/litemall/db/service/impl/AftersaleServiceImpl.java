package org.linlinjava.litemall.db.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.linlinjava.litemall.db.common.constans.DbConstans;
import org.linlinjava.litemall.db.common.util.PageUtil;
import org.linlinjava.litemall.db.entity.Aftersale;
import org.linlinjava.litemall.db.mapper.AftersaleMapper;
import org.linlinjava.litemall.db.service.IAftersaleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

/**
 * <p>
 * 售后表 服务实现类
 * </p>
 *
 * @author chenjinbao
 * @since 2020-05-03
 */
@Service
public class AftersaleServiceImpl extends ServiceImpl<AftersaleMapper, Aftersale> implements IAftersaleService {
    @Override
    public IPage<Aftersale> queryList(Integer userId, Integer status, int current, int limit, String sort, String order) {

        Page page = new Page(current, limit);

        PageUtil.pagetoPage(page,sort,order);

        IPage<Aftersale> aftersalePage = this.page(page, new LambdaQueryWrapper<Aftersale>().eq(Aftersale::getUserId, userId)
                .eq(status != null, Aftersale::getStatus, status));

        return aftersalePage;
    }

    @Override
    public Aftersale findByOrderId(Integer userId, Integer orderId) {

        return this.getOne(new LambdaQueryWrapper<Aftersale>().eq(Aftersale::getUserId, userId).eq(Aftersale::getOrderId,orderId));
    }

    @Override
    public boolean deleteByOrderId(Integer userId, Integer orderId) {

        boolean remove = this.remove(new LambdaQueryWrapper<Aftersale>().eq(Aftersale::getUserId, userId).eq(Aftersale::getOrderId, orderId));

        return remove;
    }

    @Override
    public Aftersale findByUserId(Integer userId, Integer id) {
        Aftersale aftersale = this.getOne(new LambdaQueryWrapper<Aftersale>().eq(Aftersale::getUserId, userId).eq(Aftersale::getId, id));
        return aftersale;
    }
}
