package org.linlinjava.litemall.db.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.linlinjava.litemall.db.entity.Aftersale;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 售后表 服务类
 * </p>
 *
 * @author chenjinbao
 * @since 2020-05-03
 */
public interface IAftersaleService extends IService<Aftersale> {

    IPage<Aftersale> queryList(Integer userId, Integer status, int current, int limit,String sort,String order);


    Aftersale findByOrderId(Integer userId,Integer orderId);

    Aftersale findByUserId(Integer userId,Integer id);

    boolean deleteByOrderId(Integer userId,Integer orderId);
}
