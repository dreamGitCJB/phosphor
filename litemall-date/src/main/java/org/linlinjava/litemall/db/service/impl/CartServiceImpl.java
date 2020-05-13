package org.linlinjava.litemall.db.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.linlinjava.litemall.db.entity.Cart;
import org.linlinjava.litemall.db.mapper.CartMapper;
import org.linlinjava.litemall.db.service.ICartService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 购物车商品表 服务实现类
 * </p>
 *
 * @author chenjinbao
 * @since 2020-05-03
 */
@Service
public class CartServiceImpl extends ServiceImpl<CartMapper, Cart> implements ICartService {
    @Override
    public List<Cart> queryByUid(Integer userId) {
        List<Cart> cartList = this.list(new LambdaQueryWrapper<Cart>().eq(Cart::getUserId, userId));
        return cartList;
    }

    @Override
    public Cart queryExist(Integer goodsId, Integer productId, Integer userId) {
        return this.getOne(new LambdaQueryWrapper<Cart>().eq(Cart::getGoodsId, goodsId).eq(Cart::getProductId, productId).eq(Cart::getUserId, userId));
    }

    @Override
    public Cart findById(Integer userId, Integer id) {
        return this.getOne(new LambdaQueryWrapper<Cart>().eq(Cart::getUserId, userId).eq(Cart::getId, id));
    }

    @Override
    public boolean updateCheck(Integer userId, List<Integer> idsList, Boolean checked) {
        Cart cart = new Cart();
        cart.setChecked(checked);
        boolean update = this.update(cart, new LambdaQueryWrapper<Cart>().eq(Cart::getUserId, userId).in(idsList.size() > 0, Cart::getProductId, idsList));
        return update;
    }

    @Override
    public List<Cart> queryByUidAndChecked(Integer userId) {
        List<Cart> cartList = this.list(new LambdaQueryWrapper<Cart>().eq(Cart::getUserId, userId).eq(Cart::getChecked, true));

        return cartList;
    }
}
