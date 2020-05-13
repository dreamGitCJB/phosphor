package org.linlinjava.litemall.db.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.linlinjava.litemall.db.entity.Address;
import org.linlinjava.litemall.db.mapper.AddressMapper;
import org.linlinjava.litemall.db.service.IAddressService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * <p>
 * 收货地址表 服务实现类
 * </p>
 *
 * @author chenjinbao
 * @since 2020-05-03
 */
@Service
public class AddressServiceImpl extends ServiceImpl<AddressMapper, Address> implements IAddressService {

    @Override
    public void resetDefault(Integer userId) {
        Address address = new Address();
        address.setIsDefault(false);
        this.update(address, new LambdaQueryWrapper<Address>().eq(Address::getUserId, userId));
    }

    @Override
    public Address findDefault(Integer userId) {
        return this.getOne(new LambdaQueryWrapper<Address>().eq(Address::getUserId, userId).eq(Address::getIsDefault,true));
    }

    @Override
    public Address query(Integer userId, Integer id) {
        return this.getOne(new LambdaQueryWrapper<Address>().eq(Address::getUserId, userId).eq(Address::getId,id));
    }
}
