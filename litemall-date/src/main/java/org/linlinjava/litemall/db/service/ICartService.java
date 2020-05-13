package org.linlinjava.litemall.db.service;

import org.linlinjava.litemall.db.entity.Cart;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 购物车商品表 服务类
 * </p>
 *
 * @author chenjinbao
 * @since 2020-05-03
 */
public interface ICartService extends IService<Cart> {

   List<Cart> queryByUid(Integer userId);


   Cart queryExist(Integer goodsId,Integer productId,Integer userId);

   Cart findById(Integer userId,Integer id);

   boolean updateCheck(Integer userId,List<Integer> idsList, Boolean checked);


   List<Cart> queryByUidAndChecked(Integer userId);
}
