package org.linlinjava.litemall.db.mapper;

import org.apache.ibatis.annotations.Param;
import org.linlinjava.litemall.db.entity.Order;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.time.LocalDateTime;

/**
 * <p>
 * 订单表 Mapper 接口
 * </p>
 *
 * @author chenjinbao
 * @since 2020-05-03
 */
public interface OrderMapper extends BaseMapper<Order> {

    int updateWithOptimisticLocker(@Param("lastUpdateTime") LocalDateTime lastUpdateTime, @Param("order") Order order);


}
