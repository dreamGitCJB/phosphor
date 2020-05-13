package org.linlinjava.litemall.db.service;

import org.linlinjava.litemall.db.entity.Address;
import com.baomidou.mybatisplus.extension.service.IService;

import java.time.LocalDateTime;

/**
 * <p>
 * 收货地址表 服务类
 * </p>
 *
 * @author chenjinbao
 * @since 2020-05-03
 */
public interface IAddressService extends IService<Address> {

    void resetDefault(Integer userId);

    Address findDefault(Integer userId);

    Address query(Integer userId, Integer id);
}
